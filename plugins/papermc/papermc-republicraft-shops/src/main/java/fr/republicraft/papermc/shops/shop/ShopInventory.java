package fr.republicraft.papermc.shops.shop;

import fr.republicraft.papermc.world.api.inventories.InventoryMenuBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ShopInventory {
    List<ItemStack> stacks;
    int SIZE = 9*4;

    public Inventory open(JavaPlugin plugin){
        Inventory inventory = new InventoryMenuBuilder( "Shop!")
                .build();
        return inventory;
    }
}
