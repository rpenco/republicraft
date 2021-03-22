package fr.republicraft.velocity.commands;


import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.dao.mail.MailDao;
import fr.republicraft.common.api.dao.player.PlayerDao;
import fr.republicraft.velocity.RepublicraftPlugin;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.e;

/**
 * Envoi d'un mail
 *
 * @author romain
 */
public class MailCommand implements Command {

    @Getter
    final MailDao mailDao;

    @Getter
    final PlayerDao playerDao;

    public MailCommand(RepublicraftPlugin plugin) {
        playerDao = new PlayerDao(plugin.getClient());
        mailDao = new MailDao(plugin.getClient());
        mailDao.migrate();
        plugin.getProxy().getCommandManager().register(this, "mail");
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        System.out.println("/home suggest=" + String.join(",", currentArgs));
        if (currentArgs.length == 0) {
            return new ArrayList<String>() {{
                add("read");
                add("remove");
                add("send");
                add("help");
            }}.stream().filter(s -> s.contains(currentArgs[0])).collect(Collectors.toList());
        }
        return ImmutableList.of();
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;

            if (args.length != 2) {
                player.sendMessage(e("/mail [read|send|remove] <username> \"[message]\""));
                return;
            }

            switch (args[0]) {
                case "send":
//                    playerDao.getProfile(args[1], profile -> {
//                        if (profile != null) {
//                            mailDao.send(player.getUniqueId(), profile.getUuid(), args[2], (mail) -> {
//                                player.sendMessage(TextComponent.builder()
//                                        .append(g("Message remis à"))
//                                        .append(h(profile.getUsername())).build());
//                                // TODO send channel message player receive mail
//                            });
//                        }
//                        player.sendMessage(TextComponent.builder()
//                                .append(e("Le joueur "))
//                                .append(h(args[1]))
//                                .append(e(" n'existe pas.")).build());
//                    });
                    break;
                case "read":
//                    mailDao.list(player.getUniqueId(), mails -> {
//                        TextComponent mainComponent = TextComponent.of("Boîte aux lettres (" + mails.size() + "):");
//
//                        for (Mail mail : mails) {
//                            TextComponent.Builder mailBuilder = TextComponent.builder()
//                                    .append(t(mail.getCreated().toString()))
//                                    .append(h(mail.getFromUsername()))
//                                    .append(mail.getMessage())
//                                    .append(TextComponent.builder()
//                                            .append(e("supprimer"))
//                                            .clickEvent(ClickEvent.runCommand("mail delete " + mail.getId()))
//                                    );
//                            mainComponent.append(mailBuilder);
//                        }
//                        player.sendMessage(mainComponent);
//                    });
                case "remove":
//                    mailDao.delete(player.getUniqueId(), args[1], () -> {
//                        player.sendMessage(TextComponent.builder()
//                                .append(g("Message supprimé!")).build());
//                    });
            }
        }
    }


}
