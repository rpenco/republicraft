package fr.republicraft.papermc.world.managers;

import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.channel.events.HomeChannelCommand;
import fr.republicraft.common.api.dao.home.Home;
import fr.republicraft.common.api.dao.home.HomeDao;
import fr.republicraft.common.api.dao.player.PlayerDao;
import fr.republicraft.common.api.helper.FullLocation;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.api.channels.PaperChannelListener;
import fr.republicraft.papermc.world.api.channels.events.RemoteTeleportEvent;
import fr.republicraft.papermc.world.api.inventories.InventoryButton;
import fr.republicraft.papermc.world.api.inventories.InventoryMenuBuilder;
import fr.republicraft.papermc.world.listeners.HomeListener;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.javatuples.Triplet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.*;
import static org.bukkit.Bukkit.getLogger;

public class HomeManager extends Manager implements PaperChannelListener {
    private final RepublicraftPlugin plugin;
    private final PlayerDao playerDao;
    HomeDao homeDao;
    NamespacedKey nsHomeId;

    public HomeManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        nsHomeId = new NamespacedKey(plugin, "location");
        this.homeDao = new HomeDao(plugin.getClient());
        this.playerDao = new PlayerDao(plugin.getClient());
//        migrate();
    }

    static void setHome(HomeDao dao, String name, String server, Player player, Material material) {
        Optional<Home> optionalHome = dao.get(player.getUniqueId(), name);
        if (!optionalHome.isPresent()) {
            Home home = new Home();
            home.setUuid(player.getUniqueId());
            home.setName(name);
            home.setUsername(player.getName());
            home.setCreationDate(LocalDateTime.now());
            home.setServer(server);
            home.setWorld(player.getWorld().getName());
            home.setLocationX(player.getLocation().getBlockX());
            home.setLocationY(player.getLocation().getBlockY());
            home.setLocationZ(player.getLocation().getBlockZ());
            home.setYaw(player.getLocation().getYaw());
            home.setMaterial(material.name());
            dao.insert(home);
            getLogger().info("player=" + home.getUsername() + " set home=" + home.getName());
            chatServerToPlayer(player, s("Le home " + g(name) + " a été créé!"));
        } else {
            chatServerToPlayer(player, e("Le home " + h(name) + " existe déjà."));
        }
    }

    void migrate() {
        for (Home home : homeDao.all()) {
            if (home.getServer() == null || home.getServer().isEmpty()) {
                playerDao.get(home.getUuid()).ifPresent(profile -> {
                    home.setUsername(profile.getUsername());
                    home.setFullLocation(home.getLegacyFulLocation());
                    if (home.getMaterial() == null || home.getMaterial().isEmpty()) {
                        home.setMaterial(Material.GRASS_BLOCK.name());
                    }
                    homeDao.update(home);
                });
            }
        }
    }

    @Override
    public void start() {
        plugin.getChannel().register(this, ChannelEventName.SERVER_COMMAND);
        plugin.getServer().getPluginManager().registerEvents(new HomeListener(plugin), plugin);
    }

    @Override
    public void onChannelMessage(Player player, String channel, String event, List<String> input) {
        String command = input.get(0);
        plugin.getLogger().info("home manager receive channel message command=" + command);
        //TODO improve channel events recieve title command here and cause null pointer exception..
        switch (HomeChannelCommand.get(command)) {
            case SHOW:
                plugin.getServer().getScheduler().runTask(plugin, () -> showHomeMenu(homeDao, player));
                break;
            case SET_HOME:
                setHome(homeDao, input.get(1), input.get(2), player, player.getLocation().add(0, -1, 0).getBlock().getBlockData().getMaterial());
                break;
            case DEL_HOME:
                delHome(homeDao, input.get(1), player);
                break;
            default:
                getLogger().severe("home command: invalid channel command=" + command);
        }
    }

    /**
     * Show Home menu.
     *
     * @param dao
     * @param player
     */
    void showHomeMenu(HomeDao dao, Player player) {
        plugin.getLogger().info("show homes for player=" + player.getName());
        List<Home> homes = dao.all(player.getUniqueId());
        InventoryMenuBuilder menu = new InventoryMenuBuilder("Homes", 9);
        menu.disableEmptyItem(true);
        for (int i = 0; i < homes.size(); i++) {
            Home home = homes.get(i);
            menu.addButton(new InventoryButton(Material.getMaterial(home.getFullLocation().getMaterial()), g(home.getName()))
                            .addDescription(s(home.getFullLocation().getServer()))
                            .addData(Triplet.with(nsHomeId, PersistentDataType.STRING, home.getFullLocation().toJson())),
                    i);
        }
        plugin.getLogger().info("open inventory for player=" + player.getName());
        player.openInventory(menu.build());
    }

    private void delHome(HomeDao dao, String name, Player player) {
        Optional<Home> optionalHome = dao.get(player.getUniqueId(), name);
        if (optionalHome.isPresent()) {
            dao.delete(player.getUniqueId(), name);
            chatServerToPlayer(player, s("Le home " + g(name) + " a été supprimé!"));
        } else {
            chatServerToPlayer(player, e("Le home " + h(name) + " n'existe pas."));
        }

    }


    public void goToHome(Player player, ItemStack clicked) {
        RemoteTeleportEvent ev = new RemoteTeleportEvent();
        ev.setPlayer(player);
        ev.setFullLocation(FullLocation.fromJSON(clicked.getItemMeta().getPersistentDataContainer().get(nsHomeId, PersistentDataType.STRING)));
        plugin.getChannel().sendChannelEvent(ev);
    }
}
