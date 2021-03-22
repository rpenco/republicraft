package fr.republicraft.velocity.commands;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.common.api.dao.home.Home;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import fr.republicraft.velocity.managers.HomeManager;
import lombok.NonNull;
import net.kyori.text.TextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class HomeCommand implements Command {
    private final HomeManager manager;
    private final RepublicraftPlugin plugin;

    public HomeCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        manager = ((HomeManager) plugin.getManagers().get(HomeManager.class));
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        if (currentArgs.length == 0) {
            return ImmutableList.of();
        } else if (currentArgs.length == 1) {
            List<String> list = manager.getAll(((Player) source).getUniqueId()).stream()
                    .map(Home::getName)
                    .filter(s -> s.contains(currentArgs[0]))
                    .sorted()
                    .collect(Collectors.toList());
            list.addAll(ImmutableList.of("add", "del", "help"));
            return list;
        } else if (currentArgs.length == 2 && (currentArgs[0].equals("add") || currentArgs[0].equals("del"))) {
            return manager.getAll(((Player) source).getUniqueId()).stream()
                    .map(Home::getName)
                    .filter(s -> s.contains(currentArgs[1]))
                    .sorted()
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (!player.hasPermission(RepublicraftPermission.HOME)) {
                chat.sendUnauthorizedMessage(player);
                return;
            }

            if (args.length == 0) {
                manager.showHome(player);
            } else if (args.length == 1) {
                if (args[0].equals("help")) {
                    chat.sendPrivateServerToPlayerMessage(player, usage());
                } else {
                    manager.teleport(player, args[0]);
                }
            } else if (args.length == 2) {
                switch (args[0]) {
                    case "add":
                        if (manager.getAll(((Player) source).getUniqueId()).size() < 9) {
                            manager.addHome(player, args[1]);
                            plugin.getReporter().create()
                                    .setEventName("CommandEvent")
                                    .setPlayer(player)
                                    .setCommand("home", args)
                                    .send();
                        } else {
                            chat.sendPrivateServerToPlayerMessage(player, ChatFormat.e("Tu as atteint le nombre maximun de home."));
                        }
                        break;
                    case "del":
                        // TODO delete does not returns true/false
                        manager.delHome(player, args[1]);
                        plugin.getReporter().create()
                                .setEventName("CommandEvent")
                                .setPlayer(player)
                                .setCommand("home", args)
                                .send();
                        break;
                    default:
                        chat.sendPrivateServerToPlayerMessage(player, ChatFormat.e("l'option '" + args[1] + "' n'existe pas."));
                }
            }
        }
    }

    TextComponent usage() {
        return TextComponent.builder()
                .append("§e-------------------------------- §fhomes §e--------------------------------\n")
                .append("§7Sauvegarder et se téléporter dans des §3homes§7 personnalisés\n")
                .append("§6/home : §fAfficher les homes sauvegardés, et se téléporter\n")
                .append("§6/home <§enom§6> : §fSe téléporter au home <nom>\n")
                .append("§6/home <§eadd§6|§edel§6> <§enom§6> : §fAjouter/Supprimer le home <nom>\n")
                .append("§e--------------------------------       --------------------------------")
                .build();
    }
}
