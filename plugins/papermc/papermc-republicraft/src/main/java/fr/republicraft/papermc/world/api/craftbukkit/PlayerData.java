package fr.republicraft.papermc.world.api.craftbukkit;

import fr.republicraft.common.api.helper.JsonHelper;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

@Data
public class PlayerData implements Serializable {

    private String dat;
    private String advancements;
    private String stats;

    public static void apply(String dataFile, UUID uuid) {
        PlayerData data = JsonHelper.fromJson(dataFile, PlayerData.class);
        try {
            if (data != null) {
                NBTFile.setEncodedPlayerDataFile((CraftServer) Bukkit.getServer(), uuid, data.getDat());
                NBTFile.setEncodedPlayerStats((CraftServer) Bukkit.getServer(), uuid, data.getStats());
                NBTFile.setEncodedPlayerAdvancements((CraftServer) Bukkit.getServer(), uuid, data.getAdvancements());
                Bukkit.reloadData();
            } else {
                System.err.println("data=" + data + " for player=" + uuid + " datafile=" + dataFile);
                System.err.println("failed to apply player data, data is null !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get() {
        return JsonHelper.toJson(this);
    }

    @Override
    public String toString() {
        return "dat=" + (dat != null ? dat.length() : "null") +
                " advancements=" + (advancements != null ? advancements.length() : "null") + " stats=" + (stats != null ? stats.length() : "null");
    }

    public PlayerData setPlayer(Player player) {
        try {
            dat = NBTFile.getEncodedPlayerDataFile(player);
            advancements = NBTFile.getEncodedPlayerAdvancements(player);
            stats = NBTFile.getEncodedPlayerStats(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
