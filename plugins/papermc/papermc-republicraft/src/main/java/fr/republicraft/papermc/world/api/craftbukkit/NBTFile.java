package fr.republicraft.papermc.world.api.craftbukkit;

import net.minecraft.server.v1_15_R1.WorldNBTStorage;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class NBTFile {


    public static File getPlayerDataFile(Player p) {
        //        NBTTagCompound f  = ((WorldNBTStorage)((CraftServer) p.getServer()).getHandle().playerFileData).getPlayerData(p.getUniqueId().toString());
        p.saveData();
        File f = ((WorldNBTStorage) ((CraftServer) p.getServer()).getHandle().playerFileData).getPlayerDir();
        File playerDat = new File(f, p.getUniqueId() + ".dat");
        return playerDat;
    }

    public static String getEncodedDataFile(File f) throws IOException {
        return Base64Coder.encodeLines(Files.readAllBytes(Paths.get(f.getPath())));
    }

    public static String getEncodedPlayerDataFile(Player p) throws IOException {
        return getEncodedDataFile(getPlayerDataFile(p));
    }


    public static void setPlayerDataFile(Player player, String datafile) {
        try {
            File f = ((WorldNBTStorage) ((CraftServer) player.getServer()).getHandle().playerFileData).getPlayerDir();
            File playerDat = new File(f, player.getUniqueId() + ".dat");
            Files.write(Paths.get(playerDat.getPath()), datafile.getBytes());
            player.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setEncodedPlayerDataFile(CraftServer server, UUID uuid, String datafile) throws IOException {
        File f = ((WorldNBTStorage) server.getHandle().playerFileData).getPlayerDir();
        File playerDat = new File(f, uuid + ".dat");
        Files.write(Paths.get(playerDat.getPath()), Base64Coder.decodeLines(datafile));
    }

    public static byte[] getPlayerAdvancements(Player p) throws IOException {
        File f = ((WorldNBTStorage) ((CraftServer) p.getServer()).getHandle().playerFileData).getDirectory();
        return Files.readAllBytes(Paths.get(f.getAbsolutePath(), "advancements", p.getUniqueId() + ".json"));
    }

    public static String getEncodedPlayerAdvancements(Player p) throws IOException {
        return Base64.getEncoder().encodeToString(getPlayerAdvancements(p));
    }

    public static byte[] getPlayerStats(Player p) throws IOException {
        File f = ((WorldNBTStorage) ((CraftServer) p.getServer()).getHandle().playerFileData).getDirectory();
        return Files.readAllBytes(Paths.get(f.getAbsolutePath(), "stats", p.getUniqueId() + ".json"));
    }

    public static String getEncodedPlayerStats(Player p) throws IOException {
        return Base64.getEncoder().encodeToString(getPlayerStats(p));
    }


    public static void setPlayerAdvancements(CraftServer server, UUID uuid, byte[] advancements) throws IOException {
        File f = ((WorldNBTStorage) server.getHandle().playerFileData).getDirectory();
        Files.write(Paths.get(f.getAbsolutePath(), "advancements", uuid + ".json"), advancements);
    }

    public static void setEncodedPlayerAdvancements(CraftServer server, UUID uuid, String advancements) throws IOException {
        setPlayerAdvancements(server, uuid, Base64.getDecoder().decode(advancements));
    }

    public static void setPlayerStats(CraftServer server, UUID uuid, byte[] stats) throws IOException {
        File f = ((WorldNBTStorage) server.getHandle().playerFileData).getDirectory();
        Files.write(Paths.get(f.getAbsolutePath(), "stats", uuid + ".json"), stats);
    }

    public static void setEncodedPlayerStats(CraftServer server, UUID uuid, String stats) throws IOException {
        setPlayerStats(server, uuid, Base64.getDecoder().decode(stats.getBytes(Charset.defaultCharset())));
    }

}
