package fr.republicraft.papermc.world.commands;


import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.api.inventories.InventoryButton;
import fr.republicraft.papermc.world.api.inventories.InventoryMenuBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.g;
import static fr.republicraft.papermc.world.api.chat.ChatHelper.s;

public class BookCommand implements CommandExecutor {

    private final RepublicraftPlugin plugin;

    public BookCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(RepublicraftPermission.BOOK)) {
                if (args.length == 1 && "help".equals(args[0])) {
                    player.sendMessage(usage());
                } else {
                    InventoryMenuBuilder menu = new InventoryMenuBuilder("Aides", 9);
                    menu.disableEmptyItem(true);
                    NamespacedKey nsId = new NamespacedKey(plugin, "books");
                    menu.addButton(new InventoryButton(Material.ENCHANTED_BOOK, g("Commandes de bases"))
                                    .addDescription(s("Toutes les commandes disponibles"))
                                    .addData(Triplet.with(nsId, PersistentDataType.STRING, "commands")),
                            0);

                    player.openInventory(menu.build());
                }
                return true;
            }
        }
        return false;
    }

    String usage() {
        return "§e-------------------------------- §fbooks §e--------------------------------\n" +
                "§7Obtenir les livres d'§3aide§7 du serveur\n" +
                "§6/book : §fObtenir les livres d'aide du serveur\n" +
                "§e--------------------------------       --------------------------------";
    }
}
