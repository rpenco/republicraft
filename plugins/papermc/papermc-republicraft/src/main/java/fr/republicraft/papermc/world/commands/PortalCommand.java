package fr.republicraft.papermc.world.commands;

import fr.republicraft.common.api.RepublicraftPermission;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.api.utils.NextDirection;
import fr.republicraft.papermc.world.api.utils.OppositeDirection;
import fr.republicraft.papermc.world.api.portal.PortalHelper;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PortalCommand implements CommandExecutor {


    @Getter
    RepublicraftPlugin plugin;

    public PortalCommand(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(RepublicraftPermission.PORTAL)) {
                getPlugin().getLogger().info("create portals");
                PortalHelper portalHelper = new PortalHelper();


                BlockFace eyesDirection = portalHelper.getYawDirection(player.getLocation().getYaw());
                BlockFace portalDirection = NextDirection.get(eyesDirection);
                player.sendMessage("eye direction: " + eyesDirection.name());

                Block targetBlock = player.getTargetBlock(5);
                Material material = targetBlock.getBlockData().getMaterial();

                if (!material.isBlock()) {
                    player.sendMessage("le materiaux n'est pas de type bloc.");
                    return false;
                }
                if (args.length == 1) {
                    targetBlock.setType(Material.AIR);
                    if ("end".equals(args[0])) {
                        try {
                            portalHelper.fillEnderPortal(player.getName(), targetBlock);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } else if ("nether".equals(args[0])) {
                        getPlugin().getLogger().info("Block face = " + targetBlock.getFace(player.getLocation().getBlock()));
                        try {
                            portalHelper.fillNetherPortal(player.getName(), targetBlock, portalDirection, OppositeDirection.get(portalDirection));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } else if ("custom".equals(args[0])) {
                        try {
                            portalHelper.fillCustomPortal(player.getName(), targetBlock, Material.AIR, portalDirection, OppositeDirection.get(portalDirection));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }
}
