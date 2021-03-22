package fr.republicraft.papermc.world.api.inventories;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

public class InventoryButton {
    List<Triplet<NamespacedKey, PersistentDataType, Object>> data = new ArrayList<>();
    Material material;
    String title;
    List<String> descriptions = new ArrayList<>();

    public InventoryButton() {
    }

    public InventoryButton(Material material, String title) {
        this.material = material;
        this.title = title;
    }

    public InventoryButton material(Material material) {
        this.material = material;
        return this;
    }

    public InventoryButton material(String material) {
        this.material = Material.getMaterial(material.toUpperCase());
        return this;
    }

    public InventoryButton displayName(String displayName) {
        this.title = displayName;
        return this;
    }

    public InventoryButton addData(Triplet<NamespacedKey, PersistentDataType, Object> data) {
        this.data.add(data);
        return this;
    }

    public InventoryButton addDescription(String line) {
        this.descriptions.add(line);
        return this;
    }


    public ItemStack build() {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            data.forEach(pData -> meta.getPersistentDataContainer().set(pData.getValue0(), pData.getValue1(), pData.getValue2()));
            meta.setLore(descriptions);
            item.setItemMeta(meta);
        }
        return item;
    }
}
