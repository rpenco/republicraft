package fr.republicraft.papermc.world.managers;

import fr.republicraft.common.api.dao.economy.EconomyDao;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.world.RepublicraftPlugin;

import java.util.UUID;

public class EconomyManager extends Manager {

    private final RepublicraftPlugin plugin;
    private EconomyDao economyDao;

    public EconomyManager(RepublicraftPlugin plugin){
        this.plugin = plugin;
    }

    public void pay(UUID buyer, UUID seller, double amount) {
        economyDao.updateBalance(seller, "+" + amount);
        economyDao.updateBalance(buyer, "-" + amount);
    }

    @Override
    public void start() {
        economyDao = new EconomyDao(plugin.getClient());
    }

    public double getBalance(UUID uniqueId) {
        return economyDao.getBalance(uniqueId);
    }
}
