package fr.republicraft.velocity.api.helpers.tp;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import fr.republicraft.common.api.helper.FullLocation;
import fr.republicraft.velocity.api.channels.events.ServerTeleportEvent;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import net.kyori.text.TextComponent;

import java.util.Optional;

public class TeleportHelper {

    public static void tp(RepublicraftPlugin plugin, Player player, Player remotePlayer) {
        remotePlayer.getCurrentServer().ifPresent(registeredServer -> {
            ChatManager chat = (ChatManager) plugin.getManagers().get(ChatManager.class);
            chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                    .append(ChatFormat.t("Téléportation vers "))
                    .append(ChatFormat.h(remotePlayer.getUsername()))
                    .append(ChatFormat.t(" ..."))
                    .build());

            if (player.getCurrentServer().get().getServer().equals(registeredServer.getServer())) {
                plugin.getLogger().info("local tpa on same server");
                ServerTeleportEvent teleportEvent = new ServerTeleportEvent();
                teleportEvent.setPlayer(player);
                teleportEvent.setRemotePlayer(remotePlayer);
                plugin.getChannel().sendChannelEvent(teleportEvent);

            } else {
                plugin.getLogger().info("remote tpa teleport on other server");
                player.createConnectionRequest(registeredServer.getServer()).connectWithIndication().whenComplete((connect, error) -> {
                    if (connect) {
                        plugin.getLogger().info("remote tp player connected. send local teleport.");
                        ServerTeleportEvent teleportEvent = new ServerTeleportEvent();
                        teleportEvent.setPlayer(player);
                        teleportEvent.setRemotePlayer(remotePlayer);
                        plugin.getChannel().sendChannelEvent(teleportEvent);
                    } else {
                            chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                                    .append(ChatFormat.e("La téléportation a échouée!"))
                                    .build());
                    }
                });
            }
        });
    }

    public static void tp(RepublicraftPlugin plugin, Player player, FullLocation location) {
        tp(plugin, player, location, null);
    }

    /**
     * Local and remote transportation
     *
     * @param plugin   Velocity plugin
     * @param player   player to teleport
     * @param location FullLocation location
     */
    public static void tp(RepublicraftPlugin plugin, Player player, FullLocation location, String name) {
        ChatManager chat = (ChatManager) plugin.getManagers().get(ChatManager.class);
        Optional<RegisteredServer> info = plugin.getProxy().getServer(location.getServer());
        info.ifPresent(registeredServer -> {

            player.getCurrentServer().ifPresent(serverConnection -> {

                if (name != null) {
                    chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                            .append(ChatFormat.t("Téléportation vers "))
                            .append(ChatFormat.h(name))
                            .append(ChatFormat.t(" ..."))
                            .build());
                }

                if (serverConnection.getServer().getServerInfo().equals(registeredServer.getServerInfo())) {
                    plugin.getLogger().info("local teleport on same server location=" + location + " name=" + name);
                    ServerTeleportEvent teleportEvent = new ServerTeleportEvent();
                    teleportEvent.setPlayer(player);
                    teleportEvent.setFullLocation(location);
                    plugin.getChannel().sendChannelEvent(teleportEvent);

                } else {
                    plugin.getLogger().info("remote teleport on other server location=" + location);
                    player.createConnectionRequest(registeredServer).connectWithIndication().whenComplete((connect, error) -> {
                        if (connect) {
                            plugin.getLogger().info("remote teleport player connected. send local teleport. location=" + location + " name=" + name);
                            ServerTeleportEvent teleportEvent = new ServerTeleportEvent();
                            teleportEvent.setPlayer(player);
                            teleportEvent.setFullLocation(location);
                            plugin.getChannel().sendChannelEvent(teleportEvent);
                        } else {

                            if (name != null) {
                                chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                                        .append(ChatFormat.e("La téléportation vers "))
                                        .append(ChatFormat.h(name))
                                        .append(ChatFormat.e(" a échouée!"))
                                        .build());
                            } else {
                                chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                                        .append(ChatFormat.e("La téléportation a échouée!"))
                                        .build());
                            }
                        }
                    });
                }
            });
        });
    }
}
