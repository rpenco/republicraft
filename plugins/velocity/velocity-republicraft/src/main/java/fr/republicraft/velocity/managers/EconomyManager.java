package fr.republicraft.velocity.managers;

import fr.republicraft.common.api.dao.economy.EconomyDao;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.commands.MoneyCommand;
import lombok.Getter;

import java.util.UUID;

public class EconomyManager extends Manager {

    @Getter
    final RepublicraftPlugin plugin;
    private EconomyDao economyDao;

    public EconomyManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        plugin.getProxy().getCommandManager().register(new MoneyCommand(getPlugin()), "money");
        economyDao = new EconomyDao(plugin.getClient());
    }

    @Override
    public void stop() {
        plugin.getProxy().getCommandManager().unregister("money");
        economyDao.close();
    }

    public double getBalance(UUID uniqueId) {
        return economyDao.getBalance(uniqueId);
    }

    public void setBalance(UUID uniqueId, double amount) {
        economyDao.setBalance(uniqueId, amount);
    }
}
