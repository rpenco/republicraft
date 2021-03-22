package fr.republicraft.papermc.world.api.portal;

import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.util.Stack;

public class PortalHelper {
    public int maxPortalBlocs = 12;
    public Material netherFrameBlock = Material.NETHER_WART_BLOCK;
    public Material enderFrameBlock = Material.OAK_LOG;

    /**
     * http://en.wikipedia.org/wiki/Flood_fill#Alternative_implementations
     */
    private void floodFill(final String player, final Block node, final Material portalMaterial, final Material targetMaterial,
                           final PortalType portalType, final BlockFace dir1, final BlockFace dir2, final BlockFace dir3, final BlockFace dir4, final boolean isCleaning)
            throws Throwable {

        System.out.println("block face dir1=" + dir1 + " dir2=" + dir2 + " dir3=" + dir3 + " dir4=" + dir4);
        //final String worldName = node.getWorld().getName();
//		if (!lazyWorldServerMap.containsKey(worldName))
//		{
//			lazyWorldServerMap.put(worldName, ((CraftWorld) node.getWorld()).getHandle());
//		}

        //final WorldServer worldServer = lazyWorldServerMap.get(worldName);
        //worldServer.suppressPhysics = true;

        final Stack<Block> queue = new Stack<Block>();
        final Stack<Block> treatedBlocks = new Stack<Block>();

        queue.push(node);
        Block n = null;
        while (!queue.empty() && treatedBlocks.size() < maxPortalBlocs) {
            n = queue.pop();
            final Material type = n.getType();

            if (type == targetMaterial) {

                treatedBlocks.push(n);

                final BlockState state = n.getState();
                state.setType(portalMaterial);
                state.update(true, false); // force = true, applyPhysics = false
                //worldServer.setTypeId(n.getX(), n.getY(), n.getZ(), portalMaterial.getId());

//                displayRedstoneParticles(n.getWorld(), n.getLocation().getX(),
//                        n.getLocation().getY(), n.getLocation().getZ());
//                n.getWorld().spawnParticle(Particle.SPELL_MOB, , 4, 0.001, 1, 0, 1, new Particle.DustOptions(Color.ORANGE, 1));


                queue.push(n.getRelative(dir1));
                queue.push(n.getRelative(dir2));
                queue.push(n.getRelative(dir3));
                queue.push(n.getRelative(dir4));
            } else if (!isCleaning) {
                switch (portalType) {
                    case NETHER:
                        if (netherFrameBlock != null && type != portalMaterial && type != netherFrameBlock) {
                            System.err.println("Unauthorized block " + type + " used to build " + portalType + " portal. Expected: " + netherFrameBlock);
                        }
                        break;

                    case ENDER:
                        if (enderFrameBlock != null && type != portalMaterial && type != enderFrameBlock) {
                            System.err.println("Unauthorized block " + type + " used to build " + portalType + " portal. Expexted: " + enderFrameBlock);
                        }
                        break;

                    default:
                        System.out.println("[CustomPortals] Unhandled portal type on FILLING: " + portalType);
                }
            }
        }
        if (!isCleaning) {
            System.out.println("[CustomPortals] " + player + " has made the " + portalType + " portal of " + treatedBlocks.size()
                    + " blocks from " + node.getWorld().getName() + " at " + node.getX() + "," + node.getY() + "," + node.getZ());
        } else {
            System.out.println("[CustomPortals] " + player + " has cleaned the " + portalType + " portal of " + treatedBlocks.size()
                    + " blocks from " + node.getWorld().getName() + " at " + node.getX() + "," + node.getY() + "," + node.getZ());
        }

        treatedBlocks.clear();
        queue.clear();
        //worldServer.suppressPhysics = false;
    }

    public void fillNetherPortal(final String player, final Block node, final BlockFace right, final BlockFace left) throws Throwable {
        floodFill(player, node, Material.NETHER_PORTAL, Material.AIR, PortalType.NETHER, BlockFace.DOWN, left, BlockFace.UP, right, false);
    }

    public void fillEnderPortal(final String player, final Block node) throws Throwable {
        floodFill(player, node, Material.END_PORTAL, Material.AIR, PortalType.ENDER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
                BlockFace.WEST, false);
    }

    public void cleanEnderPortal(final String player, final Block node) throws Throwable {
        floodFill(player, node, Material.AIR, Material.END_PORTAL, PortalType.ENDER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
                BlockFace.WEST, true);
    }

    public void cleanNetherPortal(final String player, final Block node, final BlockFace right, final BlockFace left) throws Throwable {
        floodFill(player, node, Material.AIR, Material.NETHER_PORTAL, PortalType.NETHER, BlockFace.DOWN, left, BlockFace.UP, right, true);
    }

    public void cleanNetherPortal(final String player, final Block node) throws Throwable {
        floodFill(player, node, Material.AIR, Material.NETHER_PORTAL, PortalType.NETHER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
                BlockFace.WEST, true);
    }

    public void fillCustomPortal(String player, Block node, Material material, BlockFace left, BlockFace right) throws Throwable {
        floodFill(player, node, material, Material.AIR, PortalType.CUSTOM, BlockFace.DOWN, left, BlockFace.UP, right, false);
    }

    public BlockFace getYawDirection(final float inputYaw) {
        final float yaw = (inputYaw < 0) ? inputYaw + 360 : inputYaw;

        if (yaw >= 0f && yaw < 45f || yaw >= 315f) {
            return BlockFace.SOUTH;
        } else if (yaw >= 45f && yaw < 135f) {
            return BlockFace.WEST;
        } else if (yaw >= 135f && yaw < 225f) {
            return BlockFace.NORTH;
        } else if (yaw >= 225f && yaw < 315f) {
            return BlockFace.EAST;
        } else {
            throw new IllegalArgumentException("Bad parameter for getYawDirection: " + inputYaw);
        }
    }
}
