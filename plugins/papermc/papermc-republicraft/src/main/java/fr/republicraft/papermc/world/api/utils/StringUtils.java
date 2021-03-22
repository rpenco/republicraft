package fr.republicraft.papermc.world.api.utils;

import fr.republicraft.common.api.helper.SimpleLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class StringUtils {


    /**
     * Create a formatted and colorized help string for a command
     *
     * @param usage   The usage string with command field and params
     * @param desc    a brief, one line description of the command
     * @param command the string to use for as the command
     * @return The fully formatted command help string
     */
    public static String helpString(String usage, String desc, String command) {
        usage = usage.replace("#{cmd}", command); // replace cmd with the first (main) alias

        // return the constructed and colorized help string
        return ChatColor.DARK_AQUA + usage + ChatColor.RESET + " - " + desc;
    }

    /**
     * Parse location "x,y,z" to location with world
     *
     * @param world
     * @param location
     * @return
     */
    public static Location parseLocation(String world, String location) {
        return parseLocation(Bukkit.getWorld(world), location);
    }

    /**
     * Parse location "x,y,z" to location with world
     *
     * @param world
     * @param location
     * @return
     */
    public static Location parseLocation(World world, String location) {
        SimpleLocation sl = SimpleLocation.fromString(location);
        return new Location(world, sl.getX(), sl.getY(), sl.getZ());
    }


    /**
     * Get location from Location object
     *
     * @param loc
     * @return "x,y,z"
     */
    public static String unparseLocation(Location loc) {
        return loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }


}
