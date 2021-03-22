package fr.republicraft.papermc.world.listeners;

import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.managers.HomeManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HomeListener implements Listener {


    private final HomeManager manager;
    private RepublicraftPlugin plugin;

    public HomeListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        manager = (HomeManager) plugin.getManagers().get(HomeManager.class);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (event.getView().getTitle().equals("Homes")) {
            event.setCancelled(true);
            if (event.getSlot() < event.getInventory().getSize() && event.getInventory().getItem(event.getSlot()) != null
                    && !"".equals(clicked.getItemMeta().getDisplayName())) {
                if (clicked.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    player.closeInventory();
                    manager.goToHome(player, clicked);
                }
            }
        }
    }
}
