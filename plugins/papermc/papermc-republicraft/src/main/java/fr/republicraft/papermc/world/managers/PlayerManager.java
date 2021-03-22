package fr.republicraft.papermc.world.managers;

import com.destroystokyo.paper.Title;
import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.channel.events.PluginChannelCommand;
import fr.republicraft.common.api.dao.player.PlayerDao;
import fr.republicraft.common.api.dao.syncinv.InventoryData;
import fr.republicraft.common.api.dao.syncinv.SyncInvDao;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.api.channels.PaperChannelListener;
import fr.republicraft.papermc.world.api.craftbukkit.PlayerData;
import fr.republicraft.papermc.world.commands.BookCommand;
import fr.republicraft.papermc.world.listeners.PlayerListener;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static fr.republicraft.papermc.world.api.inventory.InventoryUtils.setInventory;

public class PlayerManager extends Manager implements PaperChannelListener {

    private final SyncInvDao syncInvDao;
    private final PlayerDao playerDao;
    private final RepublicraftPlugin plugin;

    public PlayerManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        syncInvDao = new SyncInvDao(plugin.getClient());
        playerDao = new PlayerDao(plugin.getClient());
    }

    @Override
    public void start() {
        plugin.getChannel().register(this, ChannelEventName.SERVER_COMMAND);
        plugin.getCommand("book").setExecutor(new BookCommand(plugin));
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), plugin);

    }

    @Override
    public void stop() {
        // TODO unregister event
    }

    public void playerQuit(Player p) {
        plugin.getLogger().info("on player quit event player=" + p.getName() + " starting sync..");

        p.saveData();
        InventoryData data = new InventoryData();
        PlayerData pd = new PlayerData().setPlayer(p);
        plugin.getSLF4JLogger().info("serialized player={}", pd.toString());
        data.setData(pd.get());

        if (syncInvDao.get(p.getUniqueId()).isPresent()) {
            syncInvDao.update(p.getUniqueId(), data);
        } else {
            syncInvDao.insert(p.getUniqueId(), data);
        }
        plugin.getLogger().info("inventory synchronized before disconnect. player=" + p.getName() + "data=" + data);

    }

    public void playerPreLogin(UUID player) {
        syncInvDao.get(player).ifPresent(inventoryData -> {
            // new inventory system, load data before player join
            if (inventoryData.getData() != null && !inventoryData.getData().isEmpty()) {
                PlayerData.apply(inventoryData.getData(), player);
            }
        });
    }


    @Override
    public void onChannelMessage(Player player, String channel, String event, List<String> input) {
        String command = input.get(0);
        if (command.equals(PluginChannelCommand.TITLE.toString())) {
            player.sendTitle(Title.builder().title(input.get(1)).subtitle(input.get(2)).build());
        }
    }

    public void playerJoin(Player player) {
        player.loadData();
        
        /**
         * @deprecated remove when players are migrated
         */
        syncInvDao.get(player.getUniqueId()).ifPresent(inventoryData -> {
            if (inventoryData.getData() == null || inventoryData.getData().isEmpty()) {
                plugin.getSLF4JLogger().info("set legacy inventory");
                // legacy inventory on join, will be migrated when player quit
                setInventory(player, inventoryData);
                player.setFoodLevel(inventoryData.getFoodLevel());
                player.setHealth(inventoryData.getHealth());
                player.setTotalExperience(inventoryData.getTotalExperience());
                player.setLevel(inventoryData.getLevels());
                player.setExp(inventoryData.getExp());
                player.setGameMode(GameMode.valueOf(inventoryData.getGameMode()));
            }
        });
    }
}
