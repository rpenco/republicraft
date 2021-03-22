package fr.republicraft.velocity.managers;


import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.commands.MpCommand;
import fr.republicraft.velocity.listeners.ChatListener;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

public class ChatManager extends Manager {


    private final RepublicraftPlugin plugin;
    private ChatListener listener;

    public ChatManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        listener = new ChatListener(plugin);
        plugin.getProxy().getEventManager().register(plugin, listener);
        plugin.getProxy().getCommandManager().register(new MpCommand(plugin), "mp");
    }

    @Override
    public void stop() {
        plugin.getProxy().getEventManager().unregisterListener(plugin, listener);
    }

    /**
     * Send a private (not public, and not discord) message from SERVER to a PLAYER
     * (internal player message )
     *
     * @param player
     * @param message
     */
    public void sendPrivateServerToPlayerMessage(Player player, TextComponent message) {
        player.sendMessage(message);
    }


    /**
     * Send a public (not private, and WITH discord) message from SERVER to all PLAYERS
     * (internal player message )
     *
     * @param message
     */
    public void sendPublicServerToPlayersMessage(TextComponent message) {
        plugin.getProxy().getAllPlayers().forEach(player -> {
            sendPrivateServerToPlayerMessage(player, message);
        });
    }


    /**
     * Send a public (not private, and WITH discord) message from SERVER to all PLAYERS
     *
     * @param sender
     * @param message
     */
    public void chatPlayerToAll(Player sender, TextComponent message) {
        //TODO support link http://

        sendPublicServerToPlayersMessage(TextComponent.builder()
                .append(ChatFormat.formatDonator(sender))
                .append(ChatFormat.formatJob(sender))
                .append("§l" + sender.getUsername() + "§r ")
                .append(ChatFormat.formatStaff(sender))
                .append(TextComponent.of("• ").color(TextColor.GRAY))
                .append(message)
                .build());
    }


    public void chatFakePlayerToAll(String username, TextComponent message) {
        //TODO support link http://
        //TODO get luckperms for player
        sendPublicServerToPlayersMessage(TextComponent.builder()
//                .append(ChatFormat.formatDonator(sender))
//                .append(ChatFormat.formatJob(sender))
                .append("§l" + username + "§r ")
//                .append(ChatFormat.formatStaff(sender))
                .append(TextComponent.of("• ").color(TextColor.GRAY))
                .append(message)
                .build());
    }


    public void sendUnauthorizedMessage(Player player) {
        sendPrivateServerToPlayerMessage(player, ChatFormat.e("Tu n'a pas la permission d'effectuer cette action."));
    }


    public void chatExternalToAll(String name, String message) {
        if (plugin.getProxy().getPlayer(name).isPresent()) {
            plugin.getProxy().getPlayer(name).ifPresent(player -> chatPlayerToAll(player, TextComponent.of(message)));
        } else {
            chatFakePlayerToAll(name, TextComponent.of(message));
        }
    }

    public void chatPlayerToPlayer(Player player, String name, String[] messages) {
        plugin.getProxy().getPlayer(name).ifPresent(toPlayer -> {
            //TODO support link http://
            TextComponent message = TextComponent.builder()
                    .append(ChatFormat.formatDonator(player))
                    .append(ChatFormat.formatJob(player))
                    .append("§l" + player.getUsername() + "§r ")
                    .append(ChatFormat.formatStaff(player))
                    .append(TextComponent.of("-> ").color(TextColor.RED))
                    .append("§l" + toPlayer.getUsername() + "§r ")
                    .append(String.join(" ", messages))
                    .build();
            player.sendMessage(message);
            toPlayer.sendMessage(message);
        });
    }
}
