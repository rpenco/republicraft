package fr.republicraft.common.api.dao.syncinv;

import lombok.Data;

@Data
public class InventoryData {
    /**
     * @deprecated
     */
    @Deprecated
    private String rawInventory;

    @Deprecated
    private String rawArmor;

    @Deprecated
    private String gameMode;

    @Deprecated
    private double health;

    @Deprecated
    private int foodLevel;

    @Deprecated
    private int totalExperience;

    @Deprecated
    private int levels;

    @Deprecated
    private float exp;

    /**
     * Full player data based on NBT (.dat) file
     */
    private String data;

    @Override
    public String toString() {
        return "InventoryData{" +
                "data='" + (data != null ? data.length() : "null") + '\'' +
                ", rawInventory='" + (rawInventory != null ? rawInventory.length() : "null") + '\'' +
                ", rawArmor='" + (rawArmor != null ? rawArmor.length() : "null") + '\'' +
                ", gameMode='" + gameMode + '\'' +
                ", health=" + health +
                ", foodLevel=" + foodLevel +
                ", totalExperience=" + totalExperience +
                ", levels=" + levels +
                ", points=" + exp +
                '}';
    }
}
