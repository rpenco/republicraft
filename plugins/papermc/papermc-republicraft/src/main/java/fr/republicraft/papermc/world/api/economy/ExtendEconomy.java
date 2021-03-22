package fr.republicraft.papermc.world.api.economy;

import fr.republicraft.common.api.dao.economy.EconomyDao;
import fr.republicraft.common.api.dao.player.PlayerDao;
import fr.republicraft.common.api.dao.player.Profile;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class ExtendEconomy implements Economy {

    @Getter
    private final PlayerDao playerDao;
    private final EconomyDao economyDao;

    @Getter
    private Plugin plugin;
    private BukkitTask bukkitTask;

    public ExtendEconomy(PlayerDao playerDao, EconomyDao economyDao) {
        this.playerDao = playerDao;
        this.economyDao = economyDao;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Republicraft Vault Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return Double.toString(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "pièces";
    }

    @Override
    public String currencyNameSingular() {
        return "pièce";
    }

    @Deprecated
    @Override
    public boolean hasAccount(String playerName) {
        return playerDao.get(playerName).isPresent();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return playerDao.get(player.getUniqueId()).isPresent();
    }

    @Deprecated
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return playerDao.get(playerName).isPresent();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return playerDao.get(player.getUniqueId()).isPresent();
    }

    @Deprecated
    @Override
    public double getBalance(String playerName) {
        return playerDao.get(playerName).map(Profile::getBalance).orElse(0.0);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return playerDao.get(player.getUniqueId()).map(Profile::getBalance).orElse(0.0);
    }

    @Deprecated
    @Override
    public double getBalance(String playerName, String world) {
        return playerDao.get(playerName).map(Profile::getBalance).orElse(0.0);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return playerDao.get(player.getUniqueId()).map(Profile::getBalance).orElse(0.0);
    }

    @Deprecated
    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Deprecated
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return getBalance(player) >= amount;
    }

    @Deprecated
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        economyDao.updateBalance(playerName, "-" + amount);
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        economyDao.updateBalance(player.getUniqueId(), "-" + amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Deprecated
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        economyDao.updateBalance(playerName, "-" + amount);
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        economyDao.updateBalance(player.getUniqueId(), "-" + amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Deprecated
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        economyDao.updateBalance(playerName, "+" + amount);
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        economyDao.updateBalance(player.getUniqueId(), "+" + amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Deprecated
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        economyDao.updateBalance(playerName, "+" + amount);
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        economyDao.updateBalance(player.getUniqueId(), "+" + amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Deprecated
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Deprecated
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Deprecated
    @Override
    public boolean createPlayerAccount(String playerName) {

        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {

        return false;
    }

    @Deprecated
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {

        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }

}
