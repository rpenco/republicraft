package fr.republicraft.papermc.world.api.inventory;


import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public class InventorySyncData {

    private ItemStack[] backupInv;
    private ItemStack[] backupAr;
    private Boolean syncComplete;

    public InventorySyncData() {
        this.backupInv = null;
        this.backupAr = null;
        this.syncComplete = false;
    }
}
