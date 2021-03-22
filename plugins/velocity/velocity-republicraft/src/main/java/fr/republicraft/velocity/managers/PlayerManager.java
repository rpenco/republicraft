package fr.republicraft.velocity.managers;

import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.dao.player.PlayerDao;
import fr.republicraft.common.api.dao.player.Profile;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.listeners.PlayerListener;
import lombok.Getter;
import net.kyori.text.TextComponent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.e;

public class PlayerManager extends Manager {

    @Getter
    final RepublicraftPlugin plugin;
    private final ChatManager chat;
    private final DiscordManager discord;
    private PlayerDao playerDao;
    private PlayerListener listener;

    public PlayerManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
        discord = ((DiscordManager) plugin.getManagers().get(DiscordManager.class));
    }

    public Optional<Profile> getProfile(UUID uuid) {
        return playerDao.get(uuid);
    }

    @Override
    public void start() {
        playerDao = new PlayerDao(plugin.getClient());
        playerDao.migrate();
        listener = new PlayerListener(plugin);
        plugin.getProxy().getEventManager().register(plugin, listener);
    }

    @Override
    public void stop() {
        plugin.getProxy().getCommandManager().unregister("money");
        plugin.getProxy().getEventManager().unregisterListener(plugin, listener);
        playerDao.close();
    }

    public void playerJoin(Player player) {
        Optional<Profile> profile = playerDao.get(player.getUniqueId());
        if (!profile.isPresent()) {
            Profile p = new Profile();
            getPlugin().getLogger().info("create new player player=" + player.getUsername());


            p.setUuid(player.getUniqueId());
            p.setUsername(player.getUsername());
            p.setBalance(100.0F);
            playerDao.insert(p);

            chat.sendPublicServerToPlayersMessage(TextComponent.builder()
                    .append(ChatFormat.g("Bienvenue sur "))
                    .append(e("Républicraft "))
                    .append(ChatFormat.h(player.getUsername()))
                    .append(ChatFormat.g("!"))
                    .build());

            chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                    .append("Le serveur est en cours de construction, n'hésite pas à nous contacter au moindre problème.")
                    .build());

            discord.sendPlayerWelcomeMessage(player);
            plugin.getChannel().sendTitles(player, "§9§lRépu§f§lblic§4§lraft", "§6Bienvenue, §9" + player.getUsername() + " §6!§r");

        } else {
            getPlugin().getLogger().info("player profile=" + profile.get().getUsername());
            if (profile.get().getBanned()) {
                plugin.getReporter().create()
                        .setEventName("BannedPlayerJoinEvent")
                        .setPlayer(player)
                        .send();
                player.disconnect(e("Tu as été banni(e) du serveur! Reviens plus tard!"));
            }
        }
    }

    public List<Profile> getAllPlayers() {
        return playerDao.getPlayers();
    }
}
