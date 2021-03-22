package fr.republicraft.velocity.commands;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import fr.republicraft.velocity.managers.EconomyManager;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.text.TextComponent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Liste les liens des pages de vote
 *
 * @author romain
 */
public class MoneyCommand implements Command {

    @Getter
    final RepublicraftPlugin plugin;
    @Getter
    private final EconomyManager manager;

    public MoneyCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        manager = (EconomyManager) getPlugin().getManagers().get(EconomyManager.class);
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        if (source instanceof Player) {
            if (currentArgs.length == 0) {
                return ImmutableList.of();
            } else if (currentArgs.length == 1) {
                if (source.hasPermission(RepublicraftPermission.MONEY)) {
                    return ImmutableList.of("get", "set", "all", "help");
                } else {
                    return ImmutableList.of("get", "help");
                }
            } else if (currentArgs.length == 2) {
                return getPlugin().getProxy().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
            }
        }
        return ImmutableList.of();
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            double balance = getManager().getBalance(player.getUniqueId());
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (args.length == 0) {
                plugin.getReporter().create()
                        .setEventName("CommandEvent")
                        .setPlayer(player)
                        .setCommand("money", args)
                        .setProperty("player.balance", balance)
                        .send();
                chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                        .append(ChatFormat.s("Tu possèdes "))
                        .append(ChatFormat.m(balance))
                        .build());
            } else if (args.length == 1 && "help".equals(args[0])) {
                chat.sendPrivateServerToPlayerMessage(player, usage());
            } else if (args.length > 1) {
                if (player.hasPermission(RepublicraftPermission.MONEY)) {
                    String playerName = args[1];
                    switch (args[0]) {
                        case "get":
                            chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                                    .append(ChatFormat.h(playerName))
                                    .append(ChatFormat.s(" possède "))
                                    .append(ChatFormat.m(balance))
                                    .build());
                            plugin.getReporter().create()
                                    .setEventName("CommandEvent")
                                    .setPlayer(player)
                                    .setCommand("money", args)
                                    .setProperty("player.balance", balance)
                                    .send();
                            break;
                        case "set":
                            if (args.length == 3) {
                                double amount = Double.parseDouble(args[2]);
                                plugin.getReporter().create()
                                        .setEventName("CommandEvent")
                                        .setPlayer(player)
                                        .setCommand("money", args)
                                        .setProperty("balance", amount)
                                        .send();
                                getPlugin().getProxy().getPlayer(playerName).ifPresent(luckyPlayer -> {
                                    getManager().setBalance(luckyPlayer.getUniqueId(), amount);

                                    chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                                            .append(ChatFormat.h(playerName))
                                            .append(ChatFormat.s(" possède maintenant "))
                                            .append(ChatFormat.m(amount))
                                            .build());

                                    chat.sendPrivateServerToPlayerMessage(luckyPlayer, TextComponent.builder()
                                            .append(ChatFormat.g("Républicraft "))
                                            .append(ChatFormat.h("a modifié ton solde."))
                                            .build());
                                    chat.sendPrivateServerToPlayerMessage(luckyPlayer, TextComponent.builder()
                                            .append(ChatFormat.s("Tu possèdes maintenant "))
                                            .append(ChatFormat.m(amount))
                                            .build());

                                });

                            } else {
                                chat.sendPrivateServerToPlayerMessage(player, ChatFormat.e("Commande invalide. Usage: /money set <playerName> <amount>"));
                            }
                            break;
                        case "all":

                            double amount = Double.parseDouble(args[1]);
                            plugin.getReporter().create()
                                    .setEventName("CommandEvent")
                                    .setPlayer(player)
                                    .setCommand("money", args)
                                    .setProperty("balance", amount)
                                    .send();
                            getPlugin().getProxy().getAllPlayers().forEach(luckyPlayer -> {

                                getManager().setBalance(luckyPlayer.getUniqueId(), amount);

                                chat.sendPrivateServerToPlayerMessage(luckyPlayer, TextComponent.builder()
                                        .append(ChatFormat.g("Républicraft "))
                                        .append(ChatFormat.h("a modifié ton solde."))
                                        .build());
                                chat.sendPrivateServerToPlayerMessage(luckyPlayer, TextComponent.builder()
                                        .append(ChatFormat.s("Tu possèdes maintenant "))
                                        .append(ChatFormat.m(amount))
                                        .build());
                            });
                            break;
                        default:
                            chat.sendPrivateServerToPlayerMessage(player, ChatFormat.e("Argument '" + args[0] + "' invalide."));
                    }
                } else {
                    plugin.getReporter().create()
                            .setEventName("CommandEvent")
                            .setPlayer(player)
                            .setCommand("money", args, false)
                            .send();
                    chat.sendUnauthorizedMessage(player);
                }
            } else {
                chat.sendPrivateServerToPlayerMessage(player, ChatFormat.e("Commande invalide."));
            }
        }
    }


    TextComponent usage() {
        return TextComponent.builder()
                .append("§e-------------------------------- §fmoney §e--------------------------------\n")
                .append("§7Gestion de ton portefeuille\n")
                .append("§6/money : §fAfficher ton solde\n")
                .append("§e--------------------------------       --------------------------------")
                .build();
    }
}
