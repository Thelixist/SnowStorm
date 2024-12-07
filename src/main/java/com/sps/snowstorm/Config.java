package com.sps.snowstorm;

import java.time.LocalTime;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod(com.sps.snowstorm.SnowStorm.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Define a command to be executed
    public static final ModConfigSpec.ConfigValue<String> SCHEDULED_COMMAND = BUILDER
            .comment("The command to execute at the scheduled time.")
            .define("scheduledCommand", "say Scheduled command executed!");

    // Define the target time in hours and minutes
    public static final ModConfigSpec.IntValue TARGET_HOUR = BUILDER
            .comment("The hour at which the scheduled command should run (24-hour format).")
            .defineInRange("targetHour", 12, 0, 23);

    public static final ModConfigSpec.IntValue TARGET_MINUTE = BUILDER
            .comment("The minute at which the scheduled command should run.")
            .defineInRange("targetMinute", 0, 0, 59);

    static final ModConfigSpec SPEC = BUILDER.build();

    // Config values
    public static String scheduledCommand;
    public static LocalTime targetTime;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        scheduledCommand = SCHEDULED_COMMAND.get();
        int hour = TARGET_HOUR.get();
        int minute = TARGET_MINUTE.get();
        targetTime = LocalTime.of(hour, minute);
    }
}
