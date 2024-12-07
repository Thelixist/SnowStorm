package com.sps.snowstorm.logic;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SnowStormScheduler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void scheduleWeeklyTask(MinecraftServer server) {
        LocalTime targetTime = LocalTime.of(12, 0); // 12:00 PM
        long initialDelay = calculateInitialDelayToNextSaturday(targetTime);

        LocalTime reminderTime = LocalTime.of(9, 22); // 12:00 PM
        long reminderDelay = calculateInitialDelayToNextSaturday(reminderTime);

        // Log the initial delay
        LOGGER.info("Weekly task will execute in " + formatDelay(initialDelay) + ".");
        LOGGER.info((String.valueOf(initialDelay)));

        // Schedule the task
        scheduler.scheduleAtFixedRate(() -> {
            server.execute(() -> {
                executeServerCommand(server, "say All Radiation has been Removed!");
                executeServerCommand(server, "mek radiation removeall");
                executeServerCommand(server, "mek radiation heal @e");
            });
        }, initialDelay, 7 * 24 * 60 * 60, TimeUnit.SECONDS); // Repeat every 7 days

        scheduler.scheduleAtFixedRate(() -> {
            server.execute(() -> {
                executeServerCommand(server, "say Radiation will be removed in 1 hour!");
            });
        }, reminderDelay, 7 * 24 * 60 * 60, TimeUnit.SECONDS); // Repeat every 7 days
    }

    private static long calculateInitialDelayToNextSaturday(LocalTime targetTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextSaturday = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SATURDAY))
                .with(targetTime);

        long delayInSeconds = java.time.Duration.between(now, nextSaturday).getSeconds();
        return Math.max(delayInSeconds, 0); // Ensure no negative delay
    }

    private static String formatDelay(long delayInSeconds) {
        long days = delayInSeconds / (24 * 3600);
        long hours = (delayInSeconds % (24 * 3600)) / 3600;
        long minutes = (delayInSeconds % 3600) / 60;
        long seconds = delayInSeconds % 60;

        return String.format("%d days, %02d hours, %02d minutes, %02d seconds", days, hours, minutes, seconds);
    }

    private static void executeServerCommand(MinecraftServer server, String command) {
        try {
            server.getCommands().getDispatcher().execute(command, server.createCommandSourceStack());
            LOGGER.info("Executed command: " + command);
        } catch (Exception e) {
            LOGGER.error("Error executing command: " + command, e);
        }
    }

    public static void shutdownScheduler() {
        scheduler.shutdown();
    }
}
