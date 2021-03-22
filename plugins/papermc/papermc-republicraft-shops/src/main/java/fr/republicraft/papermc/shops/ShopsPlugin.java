package fr.republicraft.papermc.shops;

import fr.republicraft.common.api.helper.ConfigHelper;
import fr.republicraft.papermc.shops.config.EconomyConfig;
import fr.republicraft.papermc.shops.managers.HdvManager;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class ShopsPlugin extends JavaPlugin {

    @Getter
    EconomyConfig economyConfig;

    @Getter
    RepublicraftPlugin republicraftPlugin;

    @Override
    public void onEnable() {
        getLogger().info("enable plugin.");
        economyConfig = ConfigHelper.loadConfig(getDataFolder().toPath(), EconomyConfig.class);

        RegisteredServiceProvider<RepublicraftPlugin> provider = Bukkit.getServicesManager().getRegistration(RepublicraftPlugin.class);
        if (provider == null) {
            getSLF4JLogger().info("failed to get Republicraft provider. Disable plugin.");
            this.setEnabled(false);
            return;
        } else {
            getSLF4JLogger().info("provider Republicraft plugin found.");
            republicraftPlugin = provider.getProvider();
            republicraftPlugin.getManagers()
                    .add(new HdvManager(this, economyConfig.getShops()))
                    .start();
        }

        republicraftPlugin.getReporter().create(this)
                .setEventName("onEnable")
                .send();
        getLogger().info("Successfully enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable plugin");
        republicraftPlugin.getReporter().create(this)
                .setEventName("onDisable")
                .send();
    }
}
