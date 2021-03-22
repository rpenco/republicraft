package fr.republicraft.papermc.world.managers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.config.hologram.HologramConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

import static fr.republicraft.papermc.world.api.utils.StringUtils.parseLocation;

public class HoloManager extends Manager {
    final List<HologramConfig> hologramConfigs;
    List<Hologram> holograms = new ArrayList<>();
    RepublicraftPlugin plugin;

    public HoloManager(RepublicraftPlugin plugin, List<HologramConfig> hologramConfigs) {
        this.plugin = plugin;
        this.hologramConfigs = hologramConfigs;
    }


    @Override
    public void stop() {
        for (Hologram hologram : holograms) {
            hologram.delete();
        }
    }

    @Override
    public void start() {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            if (hologramConfigs != null) {
                for (HologramConfig config : hologramConfigs) {
                    Location location = parseLocation(plugin.getWorld(), config.getLocation());
                    location.setYaw(config.getYaw());
                    plugin.getLogger().info("display hologram at=" + config.getLocation());
                    Hologram hologram = HologramsAPI.createHologram(plugin, location);
                    holograms.add(hologram);
                    if (config.getLines() != null) {
                        for (String line : config.getLines()) {
                            hologram.appendTextLine(line);
                        }
                    }
                }
            }
        }, 20L);
    }
}
