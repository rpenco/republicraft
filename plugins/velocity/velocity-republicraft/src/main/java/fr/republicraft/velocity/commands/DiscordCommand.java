package fr.republicraft.velocity.commands;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;

import java.util.List;

import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.g;
import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.h;

public class DiscordCommand implements Command {

    @Getter
    RepublicraftPlugin plugin;

    public DiscordCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (!player.hasPermission(RepublicraftPermission.DISCORD)) {
                chat.sendUnauthorizedMessage(player);
                return;
            }

            if (args.length != 0) {
                chat.sendPrivateServerToPlayerMessage(player, usage());
            } else {

                getPlugin().getReporter().create()
                        .setEventName("CommandEvent")
                        .setPlayer(player)
                        .setCommand("discord", args)
                        .send();

                chat.sendPrivateServerToPlayerMessage(player,
                        TextComponent.builder()
                                .append(g("Discord: "))
                                .append(h(getPlugin().getConfig().getDiscord().getLink()).clickEvent(ClickEvent.openUrl(getPlugin().getConfig().getDiscord().getLink())))
                                .build());
            }
        }
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        return ImmutableList.of();
    }

    TextComponent usage() {
        return TextComponent.builder()
                .append("§e-------------------------------- §fdiscord §e--------------------------------\n")
                .append("§7Afficher les liens §3Discord§7\n")
                .append("§7Tu peux parler depuis §3Discord §7(§e#in-game§7) directement dans le jeu! \n")
                .append("§6/discord : §fAfficher le lien du Discord\n")
                .append("§e--------------------------------       --------------------------------")
                .build();
    }
}
