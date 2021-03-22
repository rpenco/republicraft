package fr.republicraft.papermc.shops.managers;

import fr.republicraft.common.api.config.EntityConfig;
import fr.republicraft.common.api.dao.trade.CurrentTradeItem;
import fr.republicraft.common.api.dao.trade.TradeCategory;
import fr.republicraft.common.api.dao.trade.TradeCategoryStats;
import fr.republicraft.common.api.dao.trade.TradeDao;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.shops.ShopsPlugin;
import fr.republicraft.papermc.shops.commands.HdvCommand;
import fr.republicraft.papermc.shops.config.ShopConfig;
import fr.republicraft.papermc.shops.listeners.HdVListener;
import fr.republicraft.papermc.shops.persistance.ShopInventoryMenu;
import fr.republicraft.papermc.world.managers.EconomyManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.chatServerToPlayer;
import static fr.republicraft.papermc.world.api.chat.ChatHelper.e;
import static fr.republicraft.papermc.world.api.utils.StringUtils.parseLocation;
import static fr.republicraft.papermc.world.api.utils.StringUtils.unparseLocation;

/**
 * Manage economy across world
 */
public class HdvManager extends Manager {

    private final int MAX_HEALTH = 40;
    private final List<ShopConfig> shopConfigs;
    @Getter
    private final ShopsPlugin plugin;
    private final EconomyManager economyManager;
    private final TradeDao tradeDao;
    Map<UUID, Shop> shops;
    private BukkitTask bukkitTask;


    @Getter
    private List<CurrentTradeItem> sales = new ArrayList<>();

    public HdvManager(ShopsPlugin plugin, List<ShopConfig> shopConfigs) {
        this.plugin = plugin;
        this.shopConfigs = shopConfigs;
        tradeDao = new TradeDao(plugin.getRepublicraftPlugin().getClient());
        economyManager = (EconomyManager) plugin.getRepublicraftPlugin().getManagers().get(EconomyManager.class);
    }

    @Override
    public void stop() {
        for (Map.Entry<UUID, Shop> entry : shops.entrySet()) {
            Entity entity = entry.getValue().getEntity();
            plugin.getLogger().info("remove shop name=" + entity.getCustomName());
            entity.remove();
        }
        if (bukkitTask != null && !bukkitTask.isCancelled()) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
    }

    @Override
    public void start() {
        plugin.getServer().getCommandMap().register("hdv", "rc", new HdvCommand(plugin));
        plugin.getServer().getPluginManager().registerEvents(new HdVListener(plugin), plugin);


        shops = new HashMap<>();


        if (shopConfigs != null) {
            for (ShopConfig shopConfig : shopConfigs) {
                Entity entity = spawnEntity(plugin.getServer().getWorld(shopConfig.getWorld()), shopConfig);
                if (entity != null) {
                    shops.put(entity.getUniqueId(), new Shop(entity, shopConfig));
                }
            }
        } else {
            plugin.getSLF4JLogger().info("no shops configuration");
        }
        syncTrading();
    }

    private void syncTrading() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<CurrentTradeItem> sales = tradeDao.getCurrentSales();
            // update sales cache
            this.sales = sales;

            // update villager title
            shops.forEach((uuid, shop) -> {
                shop.getEntity().setCustomName(shop.getConfig().getEntity().getName() + " [" + sales.size() + "]");
                getPlugin().getRepublicraftPlugin().getReporter().create(getPlugin())
                        .setEventName("ShopStats")
                        .setProperty("shop.id", shop.getConfig().getId())
                        .setProperty("shop.location", shop.getConfig().getLocation())
                        .setProperty("shop.sales", sales.size());
//                        .send();
            });
        }, 0L, 50L);
    }

    /**
     * Creation d'une entité
     *
     * @param world
     * @param shopConfig
     * @return
     */
    Entity spawnEntity(World world, ShopConfig shopConfig) {
        Location location = parseLocation(world, shopConfig.getLocation());
        switch (shopConfig.getEntity().getType()) {
            case "villager":
                cleanupEntityLocation(world, location, Villager.class);
                return spawnVillager(world, location, shopConfig.getEntity());
            default:
                plugin.getLogger().severe("Failed to spawn shop for entity type=" + shopConfig.getEntity().getType());
                return null;
        }
    }

    private void cleanupEntityLocation(World world, Location location, Class type) {
        if (world != null) {
            Collection<LivingEntity> entities = location.getNearbyEntitiesByType(type, 10);
            for (LivingEntity entity : entities) {
                plugin.getSLF4JLogger().info("remove previous entity={}", entity.getName());
                entity.remove();
            }
        }
    }


    /**
     * Creation d'une entité de type villageois
     *
     * @param world
     * @param location
     * @param entity
     * @return
     */
    Entity spawnVillager(World world, Location location, EntityConfig entity) {
        location.setYaw(entity.getYaw());
        Villager v = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
        v.setAI(false);
        v.setCustomName(entity.getName());
        v.setProfession(Villager.Profession.FLETCHER);
        v.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(MAX_HEALTH);
        v.setHealth(MAX_HEALTH);
        v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3560000, 356000));
        plugin.getLogger().info("create villager name=" + v.getCustomName() + " at location=" + unparseLocation(location));
        return v;
    }


    public void buyItem(UUID sender, UUID buyer, double amount, int quantity, Material material) {
        plugin.getLogger().info("message=\"player transaction item\" " +
                "sender=\"" + sender + "\" buyer=\"" + buyer + "\" itemId=\"" + material + "\" amout=\"" + amount + "\" quantity=\"" + quantity + "\"");
        economyManager.pay(buyer, sender, amount);
    }

    public void buy(Player buyer, int tradeItemId) {
        Optional<CurrentTradeItem> item = tradeDao.getItem(tradeItemId);
        if (item.isPresent()) {
            if(item.get().getPrice() < economyManager.getBalance(buyer.getUniqueId())) {
                tradeDao.buyItem(buyer.getUniqueId(), item.get().getId());
                // TODO utiliser base64stack encoder/decoder plutot que ça..
                buyItem(UUID.fromString(item.get().getPlayerUniqueId()), buyer.getUniqueId(),
                        item.get().getPrice(), item.get().getQuantity(), Material.getMaterial(item.get().getItemId().split(":")[1].toUpperCase()));
                buyer.getInventory()
                        .addItem(new ItemStack(Material.getMaterial(item.get().getItemId().split(":")[1].toUpperCase()), item.get().getQuantity()));

                getPlugin().getRepublicraftPlugin().getReporter().create(getPlugin())
                        .setEventName("ShopBuyItem")
                        .setPlayer(buyer)
                        .setProperty("seller.uuid", item.get().getPlayerUniqueId())
                        .setProperty("item.id", item.get().getId())
                        .setProperty("item.name", item.get().getItemId())
                        .setProperty("item.category", item.get().getCategory())
                        .setProperty("price", item.get().getPrice())
                        .setProperty("quantity", item.get().getQuantity())
                        .setProperty("sale_date", item.get().getSaleDate())
                        .send();
            }else{
                chatServerToPlayer(buyer, e("Tu ne possèdes pas assez d'argent !"));
            }
        } else {
            chatServerToPlayer(buyer, e("L'élément n'existe plus !"));
        }
    }

    public void sellItem(Player player, String itemName, int quantity, double amount, String type) {
        String category = TradeCategory.fromMaterial(type);
        tradeDao.sellItems(
                player.getUniqueId(),
                itemName,
                quantity,
                amount,
                category);

        getPlugin().getRepublicraftPlugin().getReporter().create(getPlugin())
                .setEventName("ShopSellItem")
                .setPlayer(player)
                .setProperty("item.name", itemName)
                .setProperty("item.category", category)
                .setProperty("price", amount)
                .setProperty("quantity", quantity)
                .send();
    }

    public Optional<Shop> isShopEntity(Entity rightClicked) {
        if (rightClicked instanceof Villager) {
            return Optional.ofNullable(shops.get(rightClicked.getUniqueId()));
        }
        return Optional.empty();
    }

    public void openShop(Player player, Shop shop) {
        // TODO open specific shop..
        double balance = economyManager.getBalance(player.getUniqueId());
        Map<String, TradeCategoryStats> stats = tradeDao.getCategoriesStats();
        player.openInventory(ShopInventoryMenu.homePage(this, stats, balance));
        plugin.getLogger().info("open trade inventory for player=" + player.getName());
    }

}
