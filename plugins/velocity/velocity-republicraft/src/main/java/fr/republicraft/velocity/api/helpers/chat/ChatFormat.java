package fr.republicraft.velocity.api.helpers.chat;

import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class ChatFormat {
    /**
     * Money formatted text
     *
     * @param amount
     * @return
     */
    public static TextComponent m(double amount) {
        return TextComponent.builder()
                .append(Double.toString(amount), TextColor.GOLD)
                .append(" pièces", TextColor.GOLD)
                .build();
    }

    /**
     * Simple text
     *
     * @param text
     * @return
     */
    public static TextComponent t(String text) {
        return TextComponent.of(text).color(TextColor.GRAY);
    }

    /**
     * Error/Invalid text
     *
     * @param text
     * @return
     */
    public static TextComponent e(String text) {
        return TextComponent.of(text).color(TextColor.RED);
    }

    /**
     * Success text
     *
     * @param text
     * @return
     */
    public static TextComponent s(String text) {
        return TextComponent.of(text).color(TextColor.GREEN);
    }

    /**
     * Highligh text
     *
     * @param text
     * @return
     */
    public static TextComponent h(String text) {
        return TextComponent.of(text).color(TextColor.AQUA);
    }

    /**
     * Gold text
     *
     * @param text
     * @return
     */
    public static TextComponent g(String text) {
        return TextComponent.of(text).color(TextColor.GOLD);
    }

    public static TextComponent p(String text) {
        return TextComponent.of(text).color(TextColor.LIGHT_PURPLE);
    }

    public static TextComponent formatJob(String playerName){
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(playerName);
        // TODO implement has permssions..
        return TextComponent.of("");
    }

    public static TextComponent formatDonator(String playerName){
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(playerName);
        // TODO implement has permssions..
        return TextComponent.of("");
    }

    public static TextComponent formatStaff(String playerName){
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(playerName);
        // TODO implement has permssions..
        return TextComponent.of("");
    }


    public static TextComponent formatJob(Player player) {
        if (player.hasPermission("group.job1")) {
            return TextComponent.of("job1 ");
        } else if (player.hasPermission("group.job2")) {
            return TextComponent.of("job2 ");

        } else if (player.hasPermission("group.job3")) {
            return TextComponent.of("job3 ");

        } else if (player.hasPermission("group.job4")) {
            return TextComponent.of("job4 ");

        } else if (player.hasPermission("group.job5")) {
            return TextComponent.of("job5 ");
        }
        return TextComponent.of("").color(TextColor.GRAY);
    }

    public static TextComponent formatDonator(Player player) {
        if (player.hasPermission("group.diamond")) {
            return TextComponent.builder()
                    .append(h("⚹ ").hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT,
                            TextComponent.of("Membre: Diamant")
                    )))
                    .build();
        } else if (player.hasPermission("group.gold")) {
            return TextComponent.builder()
                    .append(g("⚹ ").hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT,
                            TextComponent.of("Membre: Or")
                    )))
                    .build();
        } else if (player.hasPermission("group.silver")) {
            return TextComponent.builder()
                    .append(t("⚹ ").hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT,
                            TextComponent.of("Membre: Argent")
                    )))
                    .build();
        }
        return TextComponent.of("  ").color(TextColor.GRAY);
    }

    public static TextComponent formatStaff(Player player) {
        if (player.hasPermission("group.founder")) {
            return TextComponent.of("Fondateur ").color(TextColor.DARK_RED);
        } else if (player.hasPermission("group.admin")) {
            return TextComponent.of("Admin ").color(TextColor.GOLD);
        } else if (player.hasPermission("group.Modo")) {
            return TextComponent.of("Modo ").color(TextColor.BLUE);
        } else if (player.hasPermission("group.helper")) {
            return TextComponent.of("Guide ").color(TextColor.GREEN);
        } else if (player.hasPermission("group.tester")) {
            return TextComponent.of("Testeur ").color(TextColor.DARK_AQUA);
        }
        return TextComponent.of("").color(TextColor.GRAY);
    }
}
