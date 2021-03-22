package fr.republicraft.papermc.world.api.chat;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatHelper {

    /**
     * Simple text
     *
     * @param text
     * @return
     */
    public static String t(String text) {
        return ChatColor.GRAY + text;
    }

    /**
     * Error/Invalid text
     *
     * @param text
     * @return
     */
    public static String e(String text) {
        return ChatColor.RED + text;
    }


    /**
     * Success text
     *
     * @param text
     * @return
     */
    public static String s(String text) {
        return ChatColor.GREEN + text;
    }


    /**
     * Highligh text
     *
     * @param text
     * @return
     */
    public static String h(String text) {
        return ChatColor.AQUA + text;
    }


    /**
     * Gold text
     *
     * @param text
     * @return
     */
    public static String g(String text) {
        return ChatColor.GOLD + text;
    }

    /**
     * Money formatted text
     *
     * @param amount
     * @return
     */
    public static String m(double amount) {
        return ChatColor.GOLD + Double.toString(amount) + ChatColor.AQUA + " pièces";
    }

    @Deprecated
    public static void chatPlayerUnauthorized(Player player) {
        chatServerToPlayer(player, e("Tu n'as pas la permission d'effectuer cette action."));
    }

    public static void chatPlayerUnauthorized(Player player, String p) {
        chatServerToPlayer(player, e("Tu n'as pas la permission '"+p+"' pour effectuer cette action."));
    }

    /**
     * Chat line for one specific player
     *
     * @param plugin
     * @param fromPlayer
     * @param message
     */
    public static void chatPlayerToAll(Plugin plugin, Player fromPlayer, String message) {
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage(
                    formatDonator(fromPlayer) +
                            formatJob(fromPlayer) +
                            ChatColor.WHITE + fromPlayer.getName() + " " +
                            formatStaff(fromPlayer) +
                            ChatColor.GRAY + "• " +
                            message);
        });
    }


    public static String formatJob(Player player) {
        if (player.hasPermission("group.job1")) {
            return "job1 ";
        } else if (player.hasPermission("group.job2")) {
            return "job2 ";

        } else if (player.hasPermission("group.job3")) {
            return "job3 ";

        } else if (player.hasPermission("group.job4")) {
            return "job4 ";

        } else if (player.hasPermission("group.job5")) {
            return "job5 ";
        }
        return ChatColor.GRAY + "";
    }

    public static String formatDonator(Player player) {
        if (player.hasPermission("group.diamond")) {
            return h("⚹ ");
        } else if (player.hasPermission("group.gold")) {
            return g("⚹ ");
        } else if (player.hasPermission("group.silver")) {
            return t("⚹ ");
        }
        return ChatColor.GRAY + "  ";
    }

    public static String formatStaff(Player player) {
        if (player.hasPermission("group.founder")) {
            return ChatColor.DARK_RED + "Fondateur ";
        } else if (player.hasPermission("group.admin")) {
            return ChatColor.GOLD + "Admin ";
        } else if (player.hasPermission("group.Modo")) {
            return ChatColor.BLUE + "Modo ";
        } else if (player.hasPermission("group.helper")) {
            return ChatColor.GREEN + "Guide ";
        } else if (player.hasPermission("group.tester")) {
            return ChatColor.AQUA + "Testeur ";
        }
        return ChatColor.GRAY + "";
    }

    /**
     * Chat line for one specific player
     *
     * @param player
     * @param message
     */
    public static void chatServerToPlayer(Player player, String message) {
        player.sendMessage(
                ChatColor.GRAY + "serveur" +
                        ChatColor.GRAY + " • " +
                        message
        );
    }

    public static void chatServerToAll(JavaPlugin plugin, String message) {
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            chatServerToPlayer(player, message);
        });
    }
}
