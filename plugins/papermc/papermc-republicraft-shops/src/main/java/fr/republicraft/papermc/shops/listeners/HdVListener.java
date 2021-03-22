package fr.republicraft.papermc.shops.listeners;

import fr.republicraft.papermc.shops.ShopsPlugin;
import fr.republicraft.papermc.shops.managers.HdvManager;
import fr.republicraft.papermc.shops.persistance.ShopInventoryMenu;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.chatServerToPlayer;
import static fr.republicraft.papermc.world.api.chat.ChatHelper.e;

public class HdVListener implements Listener {
    private final HdvManager hdvManager;
    private ShopsPlugin plugin;

    public HdVListener(ShopsPlugin plugin) {
        this.plugin = plugin;
        hdvManager = (HdvManager) plugin.getRepublicraftPlugin().getManagers().get(HdvManager.class);
    }

    /**
     * Evenement sur une entité en charge d'un portail
     *
     * @param event
     */
    @EventHandler
    void onInteract(PlayerInteractEntityEvent event) {
        hdvManager.isShopEntity(event.getRightClicked()).ifPresent(shop -> {
            event.setCancelled(true);
            hdvManager.openShop(event.getPlayer(), shop);
        });
    }

    @EventHandler
    void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        hdvManager.isShopEntity(event.getEntity()).ifPresent(shop -> {
            event.setCancelled(true);
            if (event.getDamager() instanceof Player) {
                chatServerToPlayer((Player) event.getDamager(), e("Pas la peine de le taper, il n'a rien fait.."));
            }
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked(); // The player that clicked the item
        ItemStack clicked = event.getCurrentItem(); // The item that was clicked

        if (event.getView().getTitle().equals(ShopInventoryMenu.getHomePageName())) {
            event.setCancelled(true); // Make it so the dirt is back in its original spot
            if (event.getSlot() == 0) {
                player.closeInventory(InventoryCloseEvent.Reason.PLAYER);
                return;
            }

            if (event.getSlot() == 8 || event.getSlot() == 36 || event.getSlot() == 44) {
                chatServerToPlayer(player, e("Fonctionnalité pas encore disponible."));
                return;
            }

            if (clicked.getItemMeta().getDisplayName().equals("Inactive")) {
                return;
            }

            player.openInventory(ShopInventoryMenu.subCategoryPage(hdvManager, clicked));

        } else if (event.getView().getTitle().equals(ShopInventoryMenu.getCategoryPageName())) {
            event.setCancelled(true); // Make it so the dirt is back in its original spot
            if (event.getSlot() == 0) {
                player.closeInventory(InventoryCloseEvent.Reason.PLAYER);
                return;
            }

            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta != null) {
                NamespacedKey id = new NamespacedKey(plugin, "trade_item_id");
                int tradeItemId = meta.getPersistentDataContainer().get(id, PersistentDataType.INTEGER);
                plugin.getLogger().info("select trade wit item_id=" + tradeItemId);
            }
//            player.openInventory(ShopInventoryMenu.itemsPage(this, clicked));
            player.openInventory(ShopInventoryMenu.confirm(hdvManager, clicked));
        } else if (event.getView().getTitle().equals(ShopInventoryMenu.getConfirmPageName())) {
            event.setCancelled(true); // Make it so the dirt is back in its original spot
            if (event.getSlot() == 0) {
                player.closeInventory(InventoryCloseEvent.Reason.PLAYER);
                return;
            }

            if (clicked.getItemMeta().getDisplayName().equals("Inactive")) {
                return;
            }

            ItemMeta itemMeta = clicked.getItemMeta();
            if (itemMeta != null && !itemMeta.getPersistentDataContainer().isEmpty()) {
                String tradeAction = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "trade_action"),
                        PersistentDataType.STRING);
                if (tradeAction.equals("cancel")) {
                    player.closeInventory();
                } else if (tradeAction.equals("confirm")) {
                    int tradeItemId = itemMeta.getPersistentDataContainer()
                            .get(new NamespacedKey(plugin, "trade_item_id"), PersistentDataType.INTEGER);
                    hdvManager.buy(player, tradeItemId);
                    player.closeInventory();
                }
            }

        }
    }
}
