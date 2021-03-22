package fr.republicraft.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.*;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.event.query.ProxyQueryEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import com.velocitypowered.api.util.ModInfo;
import fr.republicraft.velocity.api.metrics.VelocityMetricReport;
import fr.republicraft.velocity.RepublicraftPlugin;
import lombok.Getter;
import net.kyori.text.TextComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ProxyListener {
    @Getter
    final RepublicraftPlugin plugin;

    @Getter
    final Favicon favicon;

    public ProxyListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        this.favicon = getFavicon();
    }

    private Favicon getFavicon() {
        InputStream is = ProxyListener.class.getResourceAsStream("/logo-64x64.jpg");
        try {
            BufferedImage imBuff = ImageIO.read(is);
            return Favicon.create(imBuff);
        } catch (IOException e) {
            getPlugin().getLogger().warn("failed to read favicon ");
            e.printStackTrace();
        }
        return null;
    }


    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        getPlugin().getReporter().create()
                .setEventName("ProxyPingEvent")
                .setProperty("client.host.name", event.getConnection().getRemoteAddress().getHostName())
                .setProperty("client.host.port", event.getConnection().getRemoteAddress().getPort())
                .setProperty("client.protocol.name", event.getConnection().getProtocolVersion().getName())
                .setProperty("client.protocol.protocol", event.getConnection().getProtocolVersion().getProtocol())
                .setProperty("client.protocol.legacy", event.getConnection().getProtocolVersion().isLegacy())
                .setProperty("client.protocol.unknown", event.getConnection().getProtocolVersion().isUnknown())
                .send();
        /**
         * https://minecraft.tools/fr/color-code.php
         */
        ServerPing e = event
                .getPing()
                .asBuilder()
                .onlinePlayers(getPlugin().getProxy().getPlayerCount())
                .maximumPlayers(event.getPing().getPlayers().get().getMax())
                .favicon(favicon)
                .description(TextComponent.of(
                        "§9§lRépu§f§lblic§4§lraft §r- §dNouveau serveur 2020 ♥\n" +
                                "§4Survie §r- §bRejoins-nous! §r| §a1.8 §rà §a1.15.2"))
                .build();
        event.setPing(e);

    }

    @Subscribe
    public void onPlayerModInfo(PlayerModInfoEvent event) {
        VelocityMetricReport ev = getPlugin().getReporter().create()
                .setEventName("PlayerModInfoEvent")
                .setPlayer(event.getPlayer())
                .setProperty("player.mod.type", event.getModInfo().getType());
        for (ModInfo.Mod mod : event.getModInfo().getMods()) {
            ev.setProperty("player.mod." + mod.getId(), mod.getVersion());
        }

        ev.send();
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent event) {
        getPlugin().getReporter().create()
                .setEventName(event.toString())
                .send();
    }

    @Subscribe()
    public void onProxyShutdown(ProxyShutdownEvent event) {
        getPlugin().getReporter().create()
                .setEventName(event.toString())
                .send();
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        getPlugin().getReporter().create()
                .setEventName(event.toString())
                .send();
    }

    @Subscribe
    public void onPostLoginEvent(PostLoginEvent event) {
        getPlugin().getReporter().create()
                .setEventName("PostLoginEvent")
                .setPlayer(event.getPlayer())
                .send();
    }

    @Subscribe
    public void onProxyQuery(ProxyQueryEvent event) {
        getPlugin().getLogger().info("[Velocity Event] event=ProxyQueryEvent queryType=" + event.getQueryType() + " e=" + event.toString());
        getPlugin().getReporter().create()
                .setEventName("ProxyQueryEvent")
                .setProperty("query.host.name", event.getQuerierAddress().getHostName())
                .setProperty("query.local.address.any", event.getQuerierAddress().isAnyLocalAddress())
                .setProperty("query.local.address.link", event.getQuerierAddress().isLinkLocalAddress())
                .setProperty("query.loopback.address", event.getQuerierAddress().isLoopbackAddress())
                .setProperty("query.global.mc", event.getQuerierAddress().isMCGlobal())
                .setProperty("query.local.mc.link", event.getQuerierAddress().isMCLinkLocal())
                .setProperty("query.local.mc.node", event.getQuerierAddress().isMCNodeLocal())
                .setProperty("query.local.mc.org", event.getQuerierAddress().isMCOrgLocal())
                .setProperty("query.local.mc.site", event.getQuerierAddress().isMCSiteLocal())
                .setProperty("query.multicast.address.", event.getQuerierAddress().isMulticastAddress())
                .setProperty("query.local.address.site", event.getQuerierAddress().isSiteLocalAddress())
                .send();
    }

    @Subscribe
    public void onClientConnectEvent(ServerConnectedEvent event) {
        getPlugin().getReporter().create()
                .setEventName("ServerConnectedEvent")
                .setPlayer(event.getPlayer())
                .setServer(event.getServer())
                .send();
    }

    @Subscribe
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {
        getPlugin().getReporter().create()
                .setEventName("PlayerResourcePackStatusEvent")
                .setPlayer(event.getPlayer())
                .setProperty("player.resource_pack.status", event.getStatus().name())
                .send();
    }

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        getPlugin().getReporter().create()
                .setEventName("ServerPreConnectEvent")
                .setPlayer(event.getPlayer())
                .setServer(event.getOriginalServer())
                .setProperty("result.allowed", event.getResult().isAllowed())
                .send();
    }

    @Subscribe
    public void onKickedFromServer(KickedFromServerEvent event) {
        VelocityMetricReport report = getPlugin().getReporter().create()
                .setEventName("KickedFromServerEvent")
                .setProperty("client.host.name", event.kickedDuringServerConnect())
                .setPlayer(event.getPlayer());
        if (event.getOriginalReason().isPresent()) {
            report.setProperty("kicked.original.reason", event.getOriginalReason().get().toString());
        }
        report.send();
    }
}
