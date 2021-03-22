package fr.republicraft.common.api.dao.trade;

import java.util.Arrays;

public class TradeCategory {
    public static final String WOOD = "wood";
    public static final String STONE = "stone";
    public static final String CONCRETE = "concrete";
    public static final String WOOL = "whool";
    public static final String GLASS = "glass";
    public static final String NATURE = "nature";
    public static final String TRANSPORTATION = "transportation";
    public static final String FOOD = "food";
    public static final String ORE = "ore";
    public static final String COMBAT = "combat";
    public static final String CHEMISTRY = "chemistry";
    public static final String DYE = "dye";
    public static final String DECORATIONS = "decorations";
    public static final String DIVERS = "misc";

    /* matchers*/
    private static final String[] wood = {"_log", "_planks", "_wood", "oak", "acacia", "jungle"};
    private static final String[] stone = {"andesite", "stone", "prismarine", "sand", "brick", "_coral_block",
            "_coral", "cobblestone", "diorite", "granite", "gravel", "purpur", "quartz"};
    private static final String[] concrete = {"_concrete", "terracotta"};
    private static final String[] whool = {"_wool", "_carpet"};
    private static final String[] glass = {"glass"};
    private static final String[] nature = {"_leaves", "_sapling", "allium", "_coral", "dirt", "grass", "pumpkin"};
    private static final String[] transportation = {"_button", "_door", "_pressure", "_trapdoor", "_rail"};
    private static final String[] food = {"potatoes", "cooked_"};
    private static final String[] ore = {"_ore", "diamon"};
    private static final String[] combat = {"stick", "_boots", "_chestplate", "_helmet", "_leggings", "bow", "_axe", "_hoe", "_pickaxe", "_shovel", "_sword"};
    private static final String[] chemistry = {"glass_bottle", "potion"};
    private static final String[] dye = {"dye"};
    private static final String[] decorations = {"_fence", "_sign", "_banner", "potted_", "_glazed", "_bed"};
    private static final String[] misc = {"zombie_", "music_", "_egg"};

    public static String fromMaterial(String material) {
        if (isWood(material)) return WOOD;
        if (isStone(material)) return STONE;
        if (isConcrete(material)) return CONCRETE;
        if (isWhool(material)) return WOOL;
        if (isGlass(material)) return GLASS;
        if (isNature(material)) return NATURE;
        if (isTransportation(material)) return TRANSPORTATION;
        if (isFood(material)) return FOOD;
        if (isOre(material)) return ORE;
        if (isCombat(material)) return COMBAT;
        if (isChemistry(material)) return CHEMISTRY;
        if (isDye(material)) return DYE;
        if (isDecorations(material)) return DECORATIONS;
//        if (isDisc(material)) return DIVERS;
        System.err.println("message=\"category not found for item " + material + "\" material=\"" + material + "\"");
        return DIVERS;

    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }

    public static boolean isWood(String name) {
        return stringContainsItemFromList(name, wood);
    }

    public static boolean isStone(String name) {
        return stringContainsItemFromList(name, stone);
    }

    public static boolean isConcrete(String name) {
        return stringContainsItemFromList(name, concrete);
    }

    public static boolean isWhool(String name) {
        return stringContainsItemFromList(name, whool);
    }

    public static boolean isGlass(String name) {
        return stringContainsItemFromList(name, glass);
    }

    public static boolean isNature(String name) {
        return stringContainsItemFromList(name, nature);
    }

    public static boolean isTransportation(String name) {
        return stringContainsItemFromList(name, transportation);
    }

    public static boolean isFood(String name) {
        return stringContainsItemFromList(name, food);
    }

    public static boolean isOre(String name) {
        return stringContainsItemFromList(name, ore);
    }

    public static boolean isCombat(String name) {
        return stringContainsItemFromList(name, combat);
    }

    public static boolean isChemistry(String name) {
        return stringContainsItemFromList(name, chemistry);
    }

    public static boolean isDye(String name) {
        return stringContainsItemFromList(name, dye);
    }

    public static boolean isDecorations(String name) {
        return stringContainsItemFromList(name, decorations);
    }

    public static boolean isDisc(String name) {
        return stringContainsItemFromList(name, misc);
    }
}
