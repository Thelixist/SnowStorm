package com.sps.snowstorm;

import com.sps.snowstorm.logic.SnowStormScheduler;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import static com.sps.snowstorm.logic.SnowStormScheduler.scheduleWeeklyTask;

@Mod(SnowStorm.MODID)
public class SnowStorm {
    public static final String MODID = "snowstorm";

    public SnowStorm() {
        // Event registration handled by NeoForge
        NeoForge.EVENT_BUS.addListener(SnowStorm::onServerStart);
        NeoForge.EVENT_BUS.addListener(SnowStorm::onServerStop);
    }

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        scheduleWeeklyTask(server);
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent event) {
        SnowStormScheduler.shutdownScheduler();
    }
}
