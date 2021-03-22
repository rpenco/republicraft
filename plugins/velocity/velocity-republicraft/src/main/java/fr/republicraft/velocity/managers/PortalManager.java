package fr.republicraft.velocity.managers;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.helper.FullLocation;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.api.channels.VelocityChannelListener;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.commands.TpCommand;
import lombok.Getter;
import net.kyori.text.TextComponent;
import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.*;
import static fr.republicraft.velocity.api.helpers.tp.TeleportHelper.tp;

public class PortalManager extends Manager implements VelocityChannelListener {
    @Getter
    final RepublicraftPlugin plugin;
    private ChatManager chat;
    private PassiveExpiringMap<UUID, UUID> tpaRequests = new PassiveExpiringMap<>(30, TimeUnit.SECONDS);
    public PortalManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        getPlugin().getChannel().register(this, ChannelEventName.PROXY_TELEPORT);
        chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
        plugin.getProxy().getCommandManager().register(new TpCommand(plugin), "tpa");
    }

    @Override
    public void stop() {
        getPlugin().getChannel().unregister(this, ChannelEventName.PROXY_TELEPORT);
    }


    public void requestTp(Player requester, Player remotePlayer){
        tpaRequests.put(remotePlayer.getUniqueId(), requester.getUniqueId());
        chat.sendPrivateServerToPlayerMessage(remotePlayer, TextComponent.builder()
                .append("§l" +requester.getUsername()+"§r ")
                .append("souhaite se téléporter à toi. ")
                .append(s("/tpa accept "))
                .append(" ou ")
                .append(e("/tpa reject"))
                .build());
        chat.sendPrivateServerToPlayerMessage(requester, TextComponent.builder()
                .append(h("une demande de téléportation à été envoyé à "))
                .append("§l" +remotePlayer.getUsername()+"§r." )
                .build());
    }

    public void tpAccept(Player remotePlayer){
        UUID requesterUUID = tpaRequests.get(remotePlayer.getUniqueId());
        if(requesterUUID != null && plugin.getProxy().getPlayer(requesterUUID).isPresent()){
            Player requester  = plugin.getProxy().getPlayer(requesterUUID).get();
            chat.sendPrivateServerToPlayerMessage(remotePlayer, TextComponent.builder()
                    .append("§l" +requester.getUsername()+"§r " )
                    .append(g("se téléporte à toi."))
                    .build());
            chat.sendPrivateServerToPlayerMessage(requester, TextComponent.builder()
                    .append("§l" +remotePlayer.getUsername()+"§r " )
                    .append(g("a accepté. Tu vas être téléporté. "))
                    .build());
            tpaRequests.remove(remotePlayer.getUniqueId());


            tp(plugin, requester, remotePlayer);

        }else{
            chat.sendPrivateServerToPlayerMessage(remotePlayer, e("La demande a expiré."));
        }

    }


    public void tpDeny(Player remotePlayer){
        UUID requesterUUID = tpaRequests.get(remotePlayer.getUniqueId());
        if(requesterUUID != null && plugin.getProxy().getPlayer(requesterUUID).isPresent()){
            plugin.getProxy().getPlayer(requesterUUID).ifPresent(requester -> {
                chat.sendPrivateServerToPlayerMessage(remotePlayer, TextComponent.builder()
                        .append(h("tu as refusé la téléportation."))
                        .build());
                chat.sendPrivateServerToPlayerMessage(requester, TextComponent.builder()
                        .append("§l" +remotePlayer.getUsername()+"§r " )
                        .append(e("a refusé la téléportation. "))
                        .build());
            });
            tpaRequests.remove(remotePlayer.getUniqueId());
        }else{
            chat.sendPrivateServerToPlayerMessage(remotePlayer, e("La demande a expiré."));
        }
    }


    @Override
    public void onChannelMessage(ChannelMessageSource source, PluginMessageEvent event, String eventName, List<String> messages) {
        plugin.getLogger().info("receive channel message teleport");
        FullLocation location = new FullLocation();
        // TODO replace by a Gson event then Java Pojo Event
        location.setServer(messages.get(0));
        location.setWorld(messages.get(1));
        location.setX(Float.parseFloat(messages.get(2)));
        location.setY(Float.parseFloat(messages.get(3)));
        location.setZ(Float.parseFloat(messages.get(4)));
        location.setYaw(Float.parseFloat(messages.get(5)));
        tp(plugin, ((ServerConnection) event.getSource()).getPlayer(), location);
    }
}
