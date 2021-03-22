package fr.republicraft.papermc.world.commands;


import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.managers.TelePortalManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.chatPlayerUnauthorized;

public class SpawnCommand implements CommandExecutor {

    private final TelePortalManager portalManager;
    private RepublicraftPlugin plugin;

    public SpawnCommand(RepublicraftPlugin plugin) {
        this.portalManager = ((TelePortalManager) plugin.getManagers().get(TelePortalManager.class));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission(RepublicraftPermission.SPAWN)) {
                if (args.length == 1 && "help".equals(args[0])) {
                    player.sendMessage(usage());
                } else {
                    portalManager.spawn(player);
                    plugin.getReporter().create()
                            .setEventName("CommandEvent")
                            .setCommand("spawn")
                            .setPlayer(player)
                            .send();
                }
                return true;
            } else {
                chatPlayerUnauthorized(player, RepublicraftPermission.SPAWN);
            }
        }
        return false;
    }

    String usage() {
        return "§e-------------------------------- §fspawn §e--------------------------------\n" +
                "§7Se téléporter au point de §3spawn§7 du monde actuel\n" +
                "§6/spawn : §fSe téléporter au point de spawn du monde actuel\n" +
                "§e--------------------------------       --------------------------------";
    }
}
