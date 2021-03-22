package fr.republicraft.papermc.resources.listeners;

import fr.republicraft.papermc.resources.ResourcesPlugin;
import fr.republicraft.papermc.resources.managers.AntiXRayManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ServerListener implements Listener {
    private final ResourcesPlugin plugin;
    private AntiXRayManager antiXRayManager;

    public ServerListener(ResourcesPlugin plugin) {
        this.plugin = plugin;
//        antiXRayManager = (AntiXRayManager) plugin.getManagers().get(AntiXRayManager.class);
    }


    @EventHandler
    void onInteract(PlayerMoveEvent event) {
//        antiXRayManager.revalVisibleBlock(event.getTo().getChunk());
//        antiXRayManager.hideVisibleBlock(event.getFrom().getChunk());
    }


    @EventHandler
    void onChunkLoad(ChunkLoadEvent event) {
//        antiXRayManager.loadChunk(event.getChunk(), event.getWorld());
    }

    @EventHandler
    void onChunkUnload(ChunkUnloadEvent event) {
//        antiXRayManager.unloadChunk(event.getChunk(), event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (!event.getBlock().getType().equals(Material.AIR)) {
            Block b = event.getBlock();
            String type = b.getType().getKey().toString();
            plugin.getRepublicraftPlugin().getReporter()
                    .create(plugin)
                    .setEventName(event.getEventName())
                    .setPlayer(event.getPlayer())
                    .setProperty("block.experience", event.getExpToDrop())
                    .setProperty("block.break_naturally", b.breakNaturally())
                    .setProperty("block.location", b.getLocation())
                    .setProperty("block.type", type)
                    .send();
        }
    }

}
