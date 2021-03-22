package fr.republicraft.velocity.commands;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import fr.republicraft.velocity.managers.JailManager;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.text.TextComponent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Envoi un joueur en prison
 * /jail <player> <time (#mon|#d|#h|#m)> <reason>
 *
 * @author romain
 */

public class JailCommand implements Command {

    private final JailManager manager;

    @Getter
    RepublicraftPlugin plugin;


    public JailCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        manager = ((JailManager) plugin.getManagers().get(JailManager.class));
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        if (source instanceof Player) {
            if (currentArgs.length == 0) {
                return ImmutableList.of();
            } else if (currentArgs.length == 1) {
                return ImmutableList.of("add", "del", "help");
            } else if (currentArgs.length == 2) {
                return getPlugin().getProxy().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
            }
        }
        return ImmutableList.of();
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (!player.hasPermission(RepublicraftPermission.JAIL) || !getPlugin().getConfig().getJail().isEnabled()) {
                chat.sendUnauthorizedMessage(player);
                plugin.getReporter().create()
                        .setEventName("CommandEvent")
                        .setPlayer(player)
                        .setCommand("jail", args, false)
                        .send();
                return;
            }

            if (args.length == 2) {
                Optional<Player> jp = getPlugin().getProxy().getPlayer(args[1]);
                if (!jp.isPresent()) {
                    chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder().append(ChatFormat.e("le joueur n'existe pas ou n'est pas connecté.")).build());
                    return;
                }

                switch (args[0]) {
                    case "add":
                        if (!manager.putInJail(player,jp.get())) {
                            chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder().append(ChatFormat.e("le joueur est déjà en prison!")).build());
                        }else{
                            plugin.getReporter().create()
                                    .setEventName("CommandEvent")
                                    .setPlayer(player)
                                    .setCommand("jail", args)
                                    .send();
                        }
                        break;
                    case "del":
                        if (!manager.releaseFromJail(jp.get())) {
                            chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder().append(ChatFormat.e("le joueur n'est pas en prison!")).build());
                        }else{
                            plugin.getReporter().create()
                                    .setEventName("CommandEvent")
                                    .setPlayer(player)
                                    .setCommand("jail", args)
                                    .send();
                        }
                        break;
                    default:
                        chat.sendPrivateServerToPlayerMessage(player, ChatFormat.e("Commande introuvable."));
                }
            } else {
                chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder().append(ChatFormat.e("/jail [add|del] <player>")).build());
            }

        }

    }

}
