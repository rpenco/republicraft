package fr.republicraft.papermc.world.listeners;

import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.config.portals.PortalConfig;
import fr.republicraft.papermc.world.managers.TelePortalManager;
import fr.republicraft.papermc.world.managers.portal.Portal;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.chatServerToPlayer;
import static fr.republicraft.papermc.world.api.chat.ChatHelper.e;

public class PortalListener implements Listener {

    private final TelePortalManager manager;
    private RepublicraftPlugin plugin;

    public PortalListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        this.manager = ((TelePortalManager) plugin.getManagers().get(TelePortalManager.class));
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        plugin.getReporter().create()
                .setEventName(event.getEventName())
                .setPlayer(event.getPlayer())
                .setProperty("source.location", event.getFrom())
//                .setProperty("target.location", event.getTo())
                .setProperty("event.cause", event.getCause().name())
                .send();

        plugin.getLogger().info("PlayerPortalEvent player=" + event.getPlayer().getName() +
                " name=" + event.getEventName() +
                " cause=" + event.getCause() +
                " from=" + event.getFrom() +
                " to=" + event.getFrom());
    }

    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent event) {
        plugin.getLogger().info("EntityPortalEvent event=" + event.getEntity() + " " + event.getEventName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        manager.spawn(event.getPlayer());
    }

    /**
     * Evenement sur une entité en charge d'un portail
     *
     * @param event
     */
    @EventHandler
    void onInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        if (event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) event.getRightClicked();
            Portal portal = manager.getPortal(villager.getUniqueId());
            if (portal != null) {
                event.setCancelled(true);
                PortalConfig config = portal.getConfig();
                manager.remoteTeleport(p, config.getRemoteFullLocation());
            }
        }
    }

    @EventHandler
    void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager villager = (Villager) event.getEntity();
            Portal portal = manager.getPortal(villager.getUniqueId());
            if (portal != null) {
                event.setCancelled(true);
                villager.setHealth(40);
                if (event.getDamager() instanceof Player) {
                    chatServerToPlayer((Player) event.getDamager(), e("Pas la peine de le taper, il n'a rien fait.."));
                }
            }
        }
    }
}
