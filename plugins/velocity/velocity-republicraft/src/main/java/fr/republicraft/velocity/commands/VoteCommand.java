package fr.republicraft.velocity.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.config.votes.providers.ServerPriveProviderConfig;
import fr.republicraft.velocity.config.votes.providers.VotesProviderConfig;
import fr.republicraft.velocity.managers.ChatManager;
import lombok.Getter;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;

import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.g;
import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.h;

/**
 * Liste les liens des pages de vote
 *
 * @author romain
 */
public class VoteCommand implements Command {

    @Getter
    final RepublicraftPlugin plugin;

    public VoteCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        plugin.getProxy().getCommandManager().register(this, "vote");
    }


    private void displayProvider(Player player, VotesProviderConfig provider) {
        if (provider.isEnabled()) {
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (provider.getMoneyEarned() > 0) {
                chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                        .append(ChatFormat.t(provider.getName() + " ("))
                        .append(ChatFormat.m(provider.getMoneyEarned()))
                        .append(ChatFormat.t("): ")).build());
                chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                        .append(ChatFormat.h("[ lien ici ]").clickEvent(ClickEvent.openUrl(provider.getPublicUrl()))).build());
            } else {
                chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                        .append(ChatFormat.t(provider.getName()))
                        .append(ChatFormat.t(": "))
                        .append(ChatFormat.h("[ lien ici ]").clickEvent(ClickEvent.openUrl(provider.getPublicUrl()))).build());
            }
        }
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (player.hasPermission(RepublicraftPermission.VOTE)) {
                if (args.length == 1 && "help".equals(args[0])) {
                    chat.sendPrivateServerToPlayerMessage(player, usage());
                } else {
                    if (getPlugin().getConfig().getVotes().isEnabled()) {

                        chat.sendPrivateServerToPlayerMessage(player,
                                TextComponent.builder()
                                        .append("Merci de soutenir §9§lRépu§f§lblic§4§lraft §r§f: ")
                                        .append(h("https://republicraft.fr/votes").clickEvent(ClickEvent.openUrl("https://republicraft.fr/votes")))
                                        .build());

//                        chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
//                                .append(ChatFormat.g("Votez pour "))
//                                .append(ChatFormat.e("Républicraft"))
//                                .append(ChatFormat.g("!")).build());
//
//                        ServerPriveProviderConfig spProvider = getPlugin().getConfig().getVotes().getProviders().getServerprive();
//                        displayProvider(player, spProvider);
                        plugin.getReporter().create()
                                .setEventName("CommandEvent")
                                .setPlayer(player)
                                .setCommand("votes", args)
                                .send();
                    } else {
                        chat.sendPrivateServerToPlayerMessage(player, ChatFormat.e("Les votes sont actuellement désactivés."));
                    }
                }
            } else {
                chat.sendUnauthorizedMessage(player);
            }
        }
    }

    TextComponent usage() {
        return TextComponent.builder()
                .append("§e-------------------------------- §fvotes §e--------------------------------\n")
                .append("§7Afficher les liens pour voter et soutenir §9§lRépu§f§lblic§4§lraft §r§7\n")
                .append("§6/votes : §fAfficher le lien vers la page de votes\n")
                .append("§e--------------------------------       --------------------------------")
                .build();
    }
}
