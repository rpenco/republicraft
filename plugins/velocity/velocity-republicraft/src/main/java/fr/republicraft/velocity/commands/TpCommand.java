package fr.republicraft.velocity.commands;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import fr.republicraft.velocity.managers.PlayerManager;
import fr.republicraft.velocity.managers.PortalManager;
import lombok.NonNull;
import net.kyori.text.TextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class TpCommand implements Command {
    private final PortalManager portalManager;
    private final RepublicraftPlugin plugin;
    private final PlayerManager playerManager;

    public TpCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        portalManager = ((PortalManager) plugin.getManagers().get(PortalManager.class));
        playerManager = ((PlayerManager) plugin.getManagers().get(PlayerManager.class));
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        if (currentArgs.length == 0) {
            return ImmutableList.of();
        } else if (currentArgs.length == 1) {
            List<String> list = plugin.getProxy().getAllPlayers().stream()
                    .map(Player::getUsername)
                    .filter(s -> s.contains(currentArgs[0]))
                    .sorted()
                    .collect(Collectors.toList());
            list.addAll(ImmutableList.of("accept", "deny"));
            return list;
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
            if (!player.hasPermission(RepublicraftPermission.TP)) {
                chat.sendUnauthorizedMessage(player);
                return;
            }

            if(args.length == 1 ){
                if(args[0].equals("accept")){
                    portalManager.tpAccept(player);
                }else if(args[0].equals("deny")){
                    portalManager.tpDeny(player);
                }else {
                    plugin.getProxy().getPlayer(args[0]).ifPresent(remotePlayer -> portalManager.requestTp(player, remotePlayer));
                }
            }else{
                chat.sendPrivateServerToPlayerMessage(player, usage());
            }
        }
    }

    TextComponent usage() {
        return TextComponent.builder()
                .append("§e-------------------------------- §fTeleport §e--------------------------------\n")
                .append("§7Se téléporter vers d'autres joueurs\n")
                .append("§6/tpa <§enom§6>: §fDemander de se téléporter au joueur\n")
                .append("§6/tpa <§eaccept§6|§edeny§6> : §fAccepter/Refuser la demande de téléportation\n")
                .append("§e--------------------------------       --------------------------------")
                .build();
    }
}
