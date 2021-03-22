package fr.republicraft.papermc.shops.persistance;

import fr.republicraft.common.api.dao.trade.CurrentTradeItem;
import fr.republicraft.common.api.dao.trade.TradeCategory;
import fr.republicraft.common.api.dao.trade.TradeCategoryStats;
import fr.republicraft.papermc.shops.managers.HdvManager;
import fr.republicraft.papermc.world.api.inventories.InventoryButton;
import fr.republicraft.papermc.world.api.inventories.InventoryMenuBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.javatuples.Triplet;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.*;

/**
 * | 0  | 1  | 2  | 3  | 4  | 5  | 6  | 7  | 8  |
 * | 9  | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 |
 * | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26 |
 * | 27 | 28 | 29 | 30 | 31 | 32 | 33 | 34 | 35 |
 * | 36 | 37 | 38 | 39 | 40 | 41 | 42 | 43 | 44 |
 * | 45 | 46 | 47 | 48 | 49 | 50 | 51 | 52 | 53 |
 */
public class ShopInventoryMenu {

    public static Inventory homePage(HdvManager manager, Map<String, TradeCategoryStats> stats, double solde) {
        InventoryMenuBuilder builder = new InventoryMenuBuilder(getHomePageName(), 9 * 6);
        NamespacedKey id = new NamespacedKey(manager.getPlugin(), "trade_category_id");

        builder.addCloseButton();
        builder.disableEmptyItem(true);
//        builder.addButton(Material.HOPPER, "Rechercher", 8);

        builder.addButton(new InventoryButton(Material.GOLD_INGOT, "SOLDE")
                .addDescription(m(solde))
                .addData(Triplet.with(id, PersistentDataType.STRING, "balance")), 45);

//        builder.addButton(Material.BOOK, "Ventes", 44);

        /*----------- line 1 */

        TradeCategoryStats stat = stats.get(TradeCategory.WOOD);
        if (stat != null) {
            builder.addButton(new InventoryButton(Material.OAK_LOG, "BOIS " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.WOOD)), 19);
        }

        stat = stats.get(TradeCategory.STONE);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.STONE, "ROCHES " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.STONE)), 20);
        }
        stat = stats.get(TradeCategory.CONCRETE);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.LIGHT_GRAY_CONCRETE, "BETON & TERRES CUITES " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.CONCRETE)), 21);
        }
        stat = stats.get(TradeCategory.WOOL);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.WHITE_WOOL, "LAINES " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.WOOL)), 22);
        }
        stat = stats.get(TradeCategory.GLASS);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.GLASS, "VERRES & VITRES " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.GLASS)), 23);
        }
        stat = stats.get(TradeCategory.NATURE);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.OAK_SAPLING, "NATURE " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.NATURE)), 24);
        }
        stat = stats.get(TradeCategory.TRANSPORTATION);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.POWERED_RAIL, "TRANSPORT " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.TRANSPORTATION)), 25);
        }
        /*----------- line 2 */
        stat = stats.get(TradeCategory.FOOD);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.APPLE, "NOURRITURE " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.FOOD)), 28);
        }
        stat = stats.get(TradeCategory.ORE);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.GOLD_ORE, "MINERAIS " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.ORE)), 29);
        }
        stat = stats.get(TradeCategory.COMBAT);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.DIAMOND_SWORD, "COMBAT & OUTILS " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.COMBAT)), 30);
        }
        stat = stats.get(TradeCategory.CHEMISTRY);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.POTION, "ALCHIMIE " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.CHEMISTRY)), 31);
        }
        stat = stats.get(TradeCategory.DYE);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.RED_DYE, "TEINTURES " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.DYE)), 32);
        }
        stat = stats.get(TradeCategory.DECORATIONS);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.BOOKSHELF, "DECORATION " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.DECORATIONS)), 33);
        }
        stat = stats.get(TradeCategory.DIVERS);
        if (stat != null) {

            builder.addButton(new InventoryButton(Material.CREEPER_HEAD, "DIVERS " + g(stat.getTotal() + ""))
                    .addData(Triplet.with(id, PersistentDataType.STRING, TradeCategory.DIVERS)), 34);
        }
        return builder.build();
    }

    public static String getHomePageName() {
        return "Hôtel des ventes: Menu";
    }

    public static String getConfirmPageName() {
        return "Hôtel des ventes: Confirmer";
    }

    public static String getCategoryPageName() {
        return "Hôtel des ventes: Catégories";
    }

    public static Inventory subCategoryPage(HdvManager manager, ItemStack clicked) {
        InventoryMenuBuilder builder = new InventoryMenuBuilder(getCategoryPageName(), 9 * 6);
        builder.addCloseButton();
        builder.disableEmptyItem(true);
        int i = 9;

        NamespacedKey itemId = new NamespacedKey(manager.getPlugin(), "trade_item_id");
        NamespacedKey categoryId = new NamespacedKey(manager.getPlugin(), "trade_category_id");
        String category = clicked.getItemMeta().getPersistentDataContainer().get(categoryId, PersistentDataType.STRING);
        List<CurrentTradeItem> sales = manager.getSales().parallelStream()
                .filter(t -> t.getCategory().equals(category)).collect(Collectors.toList());
        sales.sort(Comparator.comparingDouble(CurrentTradeItem::getPrice));

        for (CurrentTradeItem currentTradeItem : sales) {
            String[] parts = currentTradeItem.getItemId().split(":");
            InventoryButton button = new InventoryButton()
                    .material(parts[1])
                    .displayName(parts[1].toUpperCase())
                    .addDescription("quantité: " + currentTradeItem.getQuantity())
                    .addDescription("prix: " + currentTradeItem.getPrice())
                    .addData(Triplet.with(itemId, PersistentDataType.INTEGER, currentTradeItem.getId()));
            builder.addButton(button, i);
            i += 1;
        }
        return builder.build();
    }


    public static Inventory confirm(HdvManager manager, ItemStack clicked) {
        InventoryMenuBuilder builder = new InventoryMenuBuilder(getConfirmPageName(), 9 * 6);
        builder.addCloseButton();
        builder.disableEmptyItem(true);
        builder.addButton(clicked, 13);

        int tradeItemId = clicked.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(manager.getPlugin(), "trade_item_id"),
                PersistentDataType.INTEGER);
        InventoryButton noButton = new InventoryButton(Material.RED_STAINED_GLASS_PANE, "ANNULER")
                .addDescription(e("Cliquez pour annuler l'achat"))
                .addData(Triplet.with(new NamespacedKey(manager.getPlugin(), "trade_action"), PersistentDataType.STRING, "cancel"));
        builder.addButton(noButton, 29);

        InventoryButton yesButton = new InventoryButton(Material.GREEN_STAINED_GLASS_PANE, "VALIDER")
                .addDescription(g("Cliquez pour confirmer l'achat"))
                .addData(Triplet.with(new NamespacedKey(manager.getPlugin(), "trade_item_id"), PersistentDataType.INTEGER, tradeItemId))
                .addData(Triplet.with(new NamespacedKey(manager.getPlugin(), "trade_action"), PersistentDataType.STRING, "confirm"));
        builder.addButton(yesButton, 33);

        return builder.build();
    }

}
