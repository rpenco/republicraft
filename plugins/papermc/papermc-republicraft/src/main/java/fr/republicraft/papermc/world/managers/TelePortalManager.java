package fr.republicraft.papermc.world.managers;

import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.config.EntityConfig;
import fr.republicraft.common.api.helper.FullLocation;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import fr.republicraft.papermc.world.api.channels.PaperChannelListener;
import fr.republicraft.papermc.world.api.channels.events.RemoteTeleportEvent;
import fr.republicraft.papermc.world.commands.PortalCommand;
import fr.republicraft.papermc.world.commands.SpawnCommand;
import fr.republicraft.papermc.world.config.portals.PortalConfig;
import fr.republicraft.papermc.world.config.spawn.SpawnConfig;
import fr.republicraft.papermc.world.listeners.PortalListener;
import fr.republicraft.papermc.world.listeners.WordListener;
import fr.republicraft.papermc.world.managers.portal.Portal;
import fr.republicraft.papermc.world.managers.portal.PortalParticle;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static fr.republicraft.papermc.world.api.utils.StringUtils.parseLocation;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getWorld;

/**
 * Gestion des portails de téléportation
 */
public class TelePortalManager extends Manager implements Listener, PaperChannelListener {

    private final RepublicraftPlugin plugin;
    private final List<PortalConfig> portalConfigs;
    private final Map<UUID, Portal> portals;
    private final Map<UUID, PortalParticle> portalParticles;
    private final int MAX_HEALTH = 40;
    private int bukkitTask;

    public TelePortalManager(RepublicraftPlugin plugin, List<PortalConfig> portalConfigs) {
        this.plugin = plugin;
        this.portalConfigs = portalConfigs;
        this.portals = new HashMap<>();
        this.portalParticles = new HashMap<>();
    }

    /**
     * Creation de toutes les entitiés servant à interagir pour se téléporter
     */
    @Override
    public void start() {
        plugin.getCommand("spawn").setExecutor(new SpawnCommand(plugin));
        plugin.getCommand("portal").setExecutor(new PortalCommand(plugin));
        plugin.getServer().getPluginManager().registerEvents(new PortalListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new WordListener(plugin), plugin);
        plugin.getChannel().register(this, ChannelEventName.SERVER_TELEPORT);

//        PortalParticle portalParticle = new PortalParticle();
//        portalParticle.addLocation(new Location(plugin.getWorld(), 1404, 64, 922));
//        portalParticle.addLocation(new Location(plugin.getWorld(), 1405, 64, 922));
//        portalParticle.addLocation(new Location(plugin.getWorld(), 1404, 65, 922));
//        portalParticle.addLocation(new Location(plugin.getWorld(), 1405, 65, 922));
//        portalParticle.addLocation(new Location(plugin.getWorld(), 1404, 66, 922));
//        portalParticle.addLocation(new Location(plugin.getWorld(), 1405, 66, 922));
//        portalParticles.put(UUID.randomUUID(), portalParticle);

//        bukkitTask = schedule();

        if (portalConfigs != null) {
            for (PortalConfig portalConfig : portalConfigs) {
                Entity entity = spawn(plugin.getWorld(), portalConfig);
                if (entity != null) {
                    portals.put(entity.getUniqueId(), new Portal(entity, portalConfig));
                }
            }
        }
    }


    /**
     * Suppression de toutes les entités créé par le manager.
     * Important! A appeller à chaque arrête/désactivation du plugin.
     */
    @Override
    public void stop() {
        for (Map.Entry<UUID, Portal> entry : portals.entrySet()) {
            Entity entity = entry.getValue().getEntity();
            plugin.getLogger().info("remove portal name=" + entity.getCustomName());
            entity.remove();
        }

//        portalParticles.clear();
//        if (bukkitTask > 0) {
//            plugin.getServer().getScheduler().cancelTask(bukkitTask);
//        }
    }

    private void cleanupEntityLocation(World world, Location location, Class type) {
        if (world != null) {
            Collection<LivingEntity> entities = location.getNearbyEntitiesByType(type, 3);
            for (LivingEntity entity : entities) {
                plugin.getSLF4JLogger().info("remove previous entity={}", entity.getName());
                entity.remove();
            }
        }
    }

    /**
     * Creation d'une entité
     *
     * @param world
     * @param portalConfig
     * @return
     */
    Entity spawn(World world, PortalConfig portalConfig) {
        switch (portalConfig.getEntity().getType()) {
            case "villager":
                return spawnVillager(world, portalConfig.getEntity());
            case "particles":
                return spawnParticles(world, portalConfig.getEntity());
            default:
                plugin.getLogger().severe("Failed to spawn portal for entity type=" + portalConfig.getEntity().getType());
                return null;
        }
    }


    /**
     * Creation d'une entité de type villageois
     *
     * @param world
     * @param entity
     * @return
     */
    Entity spawnVillager(World world, EntityConfig entity) {
        Location location = parseLocation(world, entity.getLocation());
        cleanupEntityLocation(world, location, Villager.class);

        location.setYaw(entity.getYaw());
        Villager v = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
        v.setAI(false);
        v.setCustomName(entity.getName());
        v.setProfession(Villager.Profession.FLETCHER);
        v.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(MAX_HEALTH);
        v.setHealth(MAX_HEALTH);
        v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3560000, 356000));
        plugin.getLogger().info("create portal name=" + v.getCustomName() + " at location=" + location);
        return v;
    }

    private Entity spawnParticles(World world, EntityConfig entity) {
        return null;
    }

    public void remoteTeleport(Player player, FullLocation location) {
        RemoteTeleportEvent event = new RemoteTeleportEvent();
        event.setPlayer(player);
        event.setFullLocation(location);
        plugin.getChannel().sendChannelEvent(event);
    }

    public void localTeleport(Player player, FullLocation location) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            Location loc = new Location(getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
            loc.setYaw(location.getYaw());

            if(loc.getX() == 0 && loc.getY() == 0 && loc.getZ() == 0){
                loc.setX(loc.getWorld().getSpawnLocation().getX());
                loc.setY(loc.getWorld().getSpawnLocation().getY());
                loc.setZ(loc.getWorld().getSpawnLocation().getZ());
            }

            getLogger().info("local teleport player=" + player.getDisplayName() + " location=" + location);
            player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
        });
    }

    public void spawn(Player player, SpawnConfig spawnConfig) {
        if (spawnConfig != null) {
            Location location = parseLocation(player.getServer().getWorld(spawnConfig.getWorld()), spawnConfig.getLocation());
            location.setYaw(spawnConfig.getYaw());
            player.teleport(location);
        }
    }

    public void spawn(Player player) {
        spawn(player, plugin.getWorldConfig().getSpawn());
    }


    @Override
    public void onChannelMessage(Player player, String channel, String event, List<String> input) {
        FullLocation location = new FullLocation();
        String type = input.get(0);
        switch (type){
            case "location":
                location.setServer(input.get(1));
                location.setWorld(input.get(2));
                location.setX(Float.parseFloat(input.get(3)));
                location.setY(Float.parseFloat(input.get(4)));
                location.setZ(Float.parseFloat(input.get(5)));
                location.setYaw(Float.parseFloat(input.get(6)));
                localTeleport(player, location);
                break;
            case "player":
                String username = input.get(1);
                Player remotePlayer = plugin.getServer().getPlayer(username);
                if(remotePlayer != null ) {
                    location.setServer(remotePlayer.getServer().getName());
                    location.setWorld(remotePlayer.getWorld().getName());
                    location.setX((float) remotePlayer.getLocation().getX());
                    location.setY((float) remotePlayer.getLocation().getY());
                    location.setZ((float) remotePlayer.getLocation().getZ());
                    location.setYaw(remotePlayer.getLocation().getYaw());
                    localTeleport(player, location);
                }
                break;
            default:
                plugin.getSLF4JLogger().warn("no teleport event type={} matching options", type);
        }
    }

    public Portal getPortal(UUID uniqueId) {
        return portals.get(uniqueId);
    }


    protected int schedule() {
        bukkitTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            portalParticles.entrySet().parallelStream().forEach(uuidPortalParticleEntry -> {
                PortalParticle portal = uuidPortalParticleEntry.getValue();
                portal.update();
            });
        }, 0, 100L);
        return bukkitTask;
    }
}
