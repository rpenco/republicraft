package fr.republicraft.velocity.commands;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.velocity.RepublicraftPlugin;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

public class PortalCommand implements Command {

    @Getter
    final RepublicraftPlugin plugin;

    public PortalCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> suggest(@NonNull CommandSource source, String[] currentArgs) {
        if (source instanceof Player) {
            if (currentArgs.length == 0) {
                return ImmutableList.of();
            } else if (currentArgs.length == 1) {
                return ImmutableList.of("spawn", "lobby", "build");
            }
        }
        return ImmutableList.of();
    }

    @Override
    public void execute(CommandSource source, String[] args) {

    }

}
