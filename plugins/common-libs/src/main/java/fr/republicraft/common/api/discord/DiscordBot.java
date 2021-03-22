package fr.republicraft.common.api.discord;

import fr.republicraft.common.api.config.DiscordBotConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class DiscordBot extends ListenerAdapter {
    private static JDA jda;
    private DiscordBotConfig config;
    private TextChannel channel;
    private DiscordBotListener listener;

    public DiscordBot(DiscordBotConfig config, DiscordBotListener listener) {
        this.config = config;
        this.listener = listener;
    }

    public void stop() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    public void start() throws InterruptedException, LoginException {
        jda = JDABuilder.createDefault(config.getToken())
                .addEventListeners(this).build();
        jda.awaitReady();
        channel = jda.getTextChannelById(config.getChannelId());
        if (channel == null) {
            System.err.println("channel with id=" + config.getChannelId() + " not found.");
            stop();
        }
    }

    public void debug() {
        for (Guild guild : jda.getGuilds()) {
            System.out.println("guilds: name=\"" + guild.getName() + "\" id=" + guild.getId() + " members=" + guild.getMemberCount());

            for (Member member : guild.getMembers()) {
                System.out.println("  member: nickname=\"" + member.getNickname() + "\" name=\"" + member.getEffectiveName() + "\"");
            }

            for (GuildChannel channel : guild.getChannels()) {
                System.out.println("  channel: name=\"" + channel.getName() + "\" id=" + channel.getId() + " type=" + channel.getType());
                for (PermissionOverride permissionOverride : channel.getPermissionOverrides()) {
                    if (permissionOverride.getMember() != null) {
                        System.out.println("    permission: member=\"" + permissionOverride.getMember().getEffectiveName() + "\"" +
                                " allowed=" + permissionOverride.getAllowedRaw() +
                                " denied=" + permissionOverride.getDeniedRaw()
                        );
                    }
                }
            }
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        // bot id // 690717696840433704
        if (event.getChannel().getIdLong() == config.getChannelId() && !event.getAuthor().isBot()) {
            System.out.println("discord received" +
                    " id=\"" + event.getMessage().getId() + "\"" +
                    " messaged=\"" + event.getMessage().getContentDisplay() + "\"" +
                    " messages=\"" + event.getMessage().getContentStripped() + "\"" +
                    " messager=\"" + event.getMessage().getContentRaw() + "\"" +
                    " message=\"" + event.getMessage().getChannel().getIdLong() + "\"" +
                    " author=\"" + event.getAuthor().getName() + "\"" +
                    " isbot=\"" + event.getAuthor().isBot() + "\"" +
                    " isFake=\"" + event.getAuthor().isFake() + "\"" +
                    " authorId=\"" + event.getAuthor().getIdLong() + "\"" +
                    " channelId=\"" + event.getChannel().getIdLong() + "\"");
            if(listener != null){
                listener.onMessage(event.getAuthor().getName(), event.getMessage().getContentDisplay());
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
//        if (event.getAuthor().getIdLong() == jda.getSelfUser().getIdLong()) {
            System.out.println("discord receive message=" + event.getMessage().getContentRaw() + " author=" + event.getAuthor().getName() + " channelId=" + event.getChannel().getId());
//        }
    }

    public void sendMessage(String message) {
        if(jda.getStatus().equals(JDA.Status.CONNECTED)) {
            channel.sendMessage(message).queue(success -> {
                System.out.printf("discord sent message=\"%s\"\n", success.getContentRaw());
            });
        }
    }

    public void sendPrivateMessage(User user, String content) {
        // openPrivateChannel provides a RestAction<PrivateChannel>
        // which means it supplies you with the resulting channel
        user.openPrivateChannel().queue((channel) ->
        {
            // value is a parameter for the `accept(T channel)` method of our callback.
            // here we implement the body of that method, which will be called later by JDA automatically.
            channel.sendMessage(content).queue();
            // here we access the enclosing scope variable -content-
            // which was provided to sendPrivateMessage(User, String) as a parameter
        });
    }
}
