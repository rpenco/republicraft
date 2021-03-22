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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MpCommand implements Command {

    @Getter
    RepublicraftPlugin plugin;

    public MpCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (player.hasPermission(RepublicraftPermission.MP)) {
                if (args.length > 1) {
                    chat.chatPlayerToPlayer(player, args[0], Arrays.copyOfRange(args, 1, args.length));
                } else if (args.length == 1 && "help".equals(args[0])) {
                    chat.sendPrivateServerToPlayerMessage(player, usage());
                }
            } else {
                chat.sendUnauthorizedMessage(player);
            }
        }
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        if (source instanceof Player) {
            if (currentArgs.length == 0) {
                return ImmutableList.of();
            } else if (currentArgs.length == 1) {
                return getPlugin().getProxy().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
            }
        }
        return ImmutableList.of();
    }

    TextComponent usage() {
        return TextComponent.builder()
                .append("§e--------------------------------- §fmp §e----------------------------------\n")
                .append("§7Envoi des messages privés à un autre joueur connecté\n")
                .append("§6/mp <§ejoueur§6> <§emessage§6> : §fEnvoi le message au joueur\n")
                .append("§e--------------------------------       --------------------------------")
                .build();
    }
}
