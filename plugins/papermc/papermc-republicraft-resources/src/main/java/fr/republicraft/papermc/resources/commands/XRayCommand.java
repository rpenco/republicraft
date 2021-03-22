package fr.republicraft.papermc.resources.commands;

import com.google.common.collect.ImmutableList;
import fr.republicraft.papermc.resources.ResourcesPlugin;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class XRayCommand extends Command {
    @Getter
    private final ResourcesPlugin plugin;

    public XRayCommand(ResourcesPlugin plugin) {
        super("axr");
        this.plugin = plugin;
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            return ImmutableList.of();
        } else if (args.length == 1) {
            return ImmutableList.of("on", "off");
        }
        return ImmutableList.of();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }

    String usage() {
        return "";
    }
}
