package fr.republicraft.papermc.world.listeners;

import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.api.items.BookBuilder;
import fr.republicraft.papermc.world.managers.NightManager;
import fr.republicraft.papermc.world.managers.PlayerManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    private final PlayerManager playerManager;
    private final RepublicraftPlugin plugin;

    public PlayerListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = ((PlayerManager) plugin.getManagers().get(PlayerManager.class));

    }

    @EventHandler()
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        playerManager.playerPreLogin(event.getPlayerProfile().getId());
        plugin.getReporter().create().setEventName(event.getEventName())
                .setPlayerUsername(event.getPlayerProfile().getName())
                .setPlayerUniqueId(event.getPlayerProfile().getId())
                .send();
    }

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        playerManager.playerJoin(event.getPlayer());
        ((NightManager) plugin.getManagers().get(NightManager.class)).playerJoin(event.getPlayer());
        plugin.getReporter().create().setEventName(event.getEventName()).setPlayer(event.getPlayer()).send();
    }

    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player p = event.getPlayer();
        playerManager.playerQuit(p);
        ((NightManager) plugin.getManagers().get(NightManager.class)).playerQuit(p);
        plugin.getReporter().create().setEventName(event.getEventName()).setPlayer(p).send();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        NamespacedKey nsId = new NamespacedKey(plugin, "books");
        if (clicked != null && clicked.getItemMeta() != null && clicked.getItemMeta().getPersistentDataContainer().has(nsId, PersistentDataType.STRING)) {
            Player player = (Player) event.getWhoClicked();
            String bookType = clicked.getItemMeta().getPersistentDataContainer().get(nsId, PersistentDataType.STRING);
            event.setCancelled(true);
            switch (bookType) {
                case "commands":
                    plugin.getReporter().create().setEventName("GetCommandsBook").setPlayer(player).send();
                    ItemStack book = BookBuilder.GiveCommandsBook(player);
                    player.getInventory().addItem(book);
                    break;
                default:
                    plugin.getSLF4JLogger().error("failed to find help book type={}", bookType);
            }
        }
    }

    @EventHandler
    public void PlayerBedEnter(PlayerBedEnterEvent e) {
        plugin.getSLF4JLogger().info("player={} enter in the bed result={}", e.getPlayer().getName(), e.getBedEnterResult());
        if (e.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) {
            ((NightManager) plugin.getManagers().get(NightManager.class)).onPlayerEnterBed(e.getPlayer());
        }
    }

}
