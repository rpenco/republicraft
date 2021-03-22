package fr.republicraft.common.api.channel;

import fr.republicraft.common.api.channel.events.ChannelEventName;
import org.slf4j.Logger;

import java.util.*;

public abstract class PluginChannelDispatcher {
    protected static WeakHashMap<Object, PluginChannelDispatcher> registeredInstances = new WeakHashMap<>();
    protected final Map<String, List<ChannelListener>> forwardListeners = new HashMap<>();
    //    protected final Map<String, Queue<CompletableFuture<?>>> callbackMap;
    protected Object plugin;
    protected Logger logger;


    public PluginChannelDispatcher() {
//        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null");
//        this.logger = logger;
        // Prevent dev's from registering multiple channel listeners,
        // by unregistering the old instance.
//        synchronized (registeredInstances) {
//            registeredInstances.compute(plugin, (k, oldInstance) -> {
//                if (oldInstance != null) oldInstance.unregister();
//                return this;
//            });
//        }
    }

    /**
     * Get or create new VelocityChannelDispatcher instance
     *
     * @param plugin the plugin instance.
     * @return the VelocityChannelDispatcher instance for the {@code plugin}
     * @throws NullPointerException if the {@code plugin} is {@code null}
     */
    public synchronized static <T extends PluginChannelDispatcher> T of(Class<T> clazz, Object plugin, Logger logger) throws IllegalAccessException, InstantiationException {
//        return (T) registeredInstances.compute(plugin, (k, v) -> {
//            if (v == null) {
//                try {
//                    v = clazz.getDeclaredConstructor().newInstance();
//                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
//            }
//            return v;
//        });
        T t = clazz.newInstance();
        t.setPlugin(plugin);
        t.setLogger(logger);
        t.register();
        return t;
    }

    protected void setPlugin(Object plugin) {
        this.plugin = plugin;
    }

    protected void setLogger(Logger logger) {
        this.logger = logger;
    }


    /**
     * Register message channels
     *
     * @exemple bukkit
     * {
     * this.messageListener = this::onPluginMessageReceived;
     * Messenger messenger = Bukkit.getServer().getMessenger();
     * messenger.registerOutgoingPluginChannel(plugin, "BungeeCord");
     * messenger.registerIncomingPluginChannel(plugin, "BungeeCord", messageListener);
     * }
     */
    protected abstract void register();


    /**
     * Unregister message channels.
     *
     * @example bukkit
     * {
     * Messenger messenger = Bukkit.getServer().getMessenger();
     * messenger.unregisterIncomingPluginChannel(plugin, "BungeeCord", messageListener);
     * messenger.unregisterOutgoingPluginChannel(plugin);
     * callbackMap.clear();
     * }
     */
    protected abstract void unregister();


    /**
     * Set a listener for all 'forwarded' messages in a specific subchannel.
     *
     * @param listener   the listener
     * @param eventNames list of registered events (empty == all)
     */
    public void register(ChannelListener listener, ChannelEventName... eventNames) {
        synchronized (forwardListeners) {
            for (ChannelEventName filter : eventNames) {
                forwardListeners.computeIfAbsent(filter.name(), s -> new ArrayList<>());
                logger.info("register listener={} event={}", listener.getClass().getName(), filter.name());
                forwardListeners.get(filter.name()).add(listener);
            }
        }
    }

    public void unregister(ChannelListener listener, ChannelEventName... eventNames) {
        for (ChannelEventName event : eventNames) {
            forwardListeners.getOrDefault(event.name(), new ArrayList<>())
                    .removeIf(channelListener -> {
                        logger.info("unregister listener={} event={}", listener.getClass().getName(), event.name());
                        return channelListener.equals(listener);
                    });
        }
    }
}
