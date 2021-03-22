package fr.republicraft.papermc.world.api.inventories;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryMenuBuilder {
    private static Inventory inventory;
    int size = 9;
    @Getter
    private String title;
    private boolean disableEmpty = true;

    public InventoryMenuBuilder(String title, int size) {
        getInstance(title, size);
    }

    public InventoryMenuBuilder(String title) {
        this.title = title;
        getInstance(title, 9);
    }

    private static int lineModulo(int size) {
        int i = size % 9;
        if (i == 0) {
            return size;
        } else {
            return size + 9 - i;
        }
    }

    private void getInstance(String title, int size) {
        inventory = Bukkit.createInventory(null, lineModulo(size), title);
    }

    public InventoryMenuBuilder addCloseButton() {
        addButton(Material.BARRIER, "Fermer", 0);
        return this;
    }

    public InventoryMenuBuilder addButton(Material material, String title, int position) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            item.setItemMeta(meta);
        }
        inventory.setItem(position, item);
        return this;
    }

    public InventoryMenuBuilder addButton(ItemStack stack, int position) {
        inventory.setItem(position, stack);
        return this;
    }

    public InventoryMenuBuilder addButton(InventoryButton button, int position) {
        inventory.setItem(position, button.build());
        return this;
    }


    public Inventory build() {
        if (disableEmpty) {

            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName("Inactive");
                    item.setItemMeta(itemMeta);
                    inventory.setItem(i, item);
                }
            }
        }

        return inventory;
    }

    public void disableEmptyItem(boolean d) {
        disableEmpty = d;
    }

}
