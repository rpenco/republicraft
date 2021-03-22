package fr.republicraft.papermc.resources.managers;

import fr.republicraft.common.api.dao.trade.CurrentTradeItem;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.resources.ResourcesPlugin;
import fr.republicraft.papermc.resources.config.ResourcesConfig;
import fr.republicraft.papermc.resources.listeners.ServerListener;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manage economy across world
 */
public class AntiXRayManager extends Manager {

    private final ResourcesConfig config;
    @Getter
    private final ResourcesPlugin plugin;
    @Getter
    private final List<CurrentTradeItem> sales = new ArrayList<>();
    private BukkitTask bukkitTask;

    public AntiXRayManager(ResourcesPlugin plugin, ResourcesConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    public static void getBlockInChunk(int maxHeight, Chunk chunk, FilterConsumer consumer) {
        for (int x = 0; x <= 15; x++) {
            for (int y = 0; y <= maxHeight; y++) {
                for (int z = 0; true; x++) {
                    consumer.filter(chunk.getBlock(x, y, z));
                }
            }
        }
    }

    @Override
    public void stop() {
        if (bukkitTask != null && !bukkitTask.isCancelled()) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
    }

    @Override
    public void start() {
//        plugin.getServer().getCommandMap().register("hdv", "rc", new XRayCommand(plugin));
        plugin.getServer().getPluginManager().registerEvents(new ServerListener(plugin), plugin);
    }

    /**
     * Get all ore block, save it to a file. Replace its by grass
     */
    public void process() {
        // only on loaded // generated chuncks?
//        plugin.getWorld().getWorldBorder().getCenter();
//        plugin.getWorld().getWorldBorder().getSize();
//        Chunk[] loadedChunks = plugin.getWorld().getLoadedChunks();

//        for (Chunk loadedChunk : loadedChunks) {
            // get ore blocks
            // save location
            // replace by grass
            //TODO using streaming..
//        }
    }

    private void loadChunk(Chunk chunk, Map<Long, List<BlockSaved>> changedBlocs) {
        int maxHeight = plugin.getServer().getWorld("world").getMaxHeight();
        if (!changedBlocs.containsKey(chunk.getChunkKey())) {
            plugin.getSLF4JLogger().info("antixray processing load chunk={}", chunk.getChunkKey());
            getBlockInChunk(maxHeight, chunk, block -> {
                Material type = block.getType();
                if (config.getBlockIds().contains(type)) {
                    plugin.getSLF4JLogger().info("antixray processing load block={} type={} chunk={}", block.getType(), type.name(), chunk.getChunkKey());
                    // save original block
                    changedBlocs.computeIfAbsent(chunk.getChunkKey(), c -> new ArrayList<>())
                            .add(new BlockSaved(block.getBlockKey(), type.name(), block.getX(), block.getY(), block.getZ(), false));
                    // hide block
                    block.setType(Material.GRASS_BLOCK);
                }
            });
        }
    }

    private void unloadChunk(Chunk chunk, Map<Long, List<BlockSaved>> changedBlocs) {
        int maxHeight = plugin.getServer().getWorld("world").getMaxHeight();
        if (!changedBlocs.containsKey(chunk.getChunkKey())) {
            plugin.getSLF4JLogger().info("antixray processing unload chunk={}", chunk.getChunkKey());
            changedBlocs.get(chunk.getChunkKey()).parallelStream().forEach(blockSaved -> {
                @NotNull Block block = chunk.getBlock(blockSaved.getX(), blockSaved.getY(), blockSaved.getZ());
                if (block.getType().equals(Material.GRASS_BLOCK)) {
                    plugin.getSLF4JLogger().info("antixray processing unload block={} type={} chunk={}", block.getType(), blockSaved.getMaterial(), chunk.getChunkKey());
                    block.setType(Material.valueOf(blockSaved.getMaterial()));
                    blockSaved.setRemoved(true);
                }
            });
        }
    }

    /**
     * Replace all ores block to hidden blocks
     * @param chunk
     * @param world
     */
    public void loadChunk(Chunk chunk, World world) {

    }

    /**
     * Replace all hidden ores block to original blocks
     * @param chunk
     * @param world
     */
    public void unloadChunk(Chunk chunk, World world) {

    }

    public void revalVisibleBlock(Chunk chunk) {
        // Display only block with one face is AIR
    }

    public void hideVisibleBlock(Chunk chunk) {
        // Todo hide all chunck
    }

    @FunctionalInterface
    public interface FilterConsumer {
        void filter(Block b);
    }
}
