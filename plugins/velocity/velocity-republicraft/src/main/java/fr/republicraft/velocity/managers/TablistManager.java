package fr.republicraft.velocity.managers;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.listeners.TabListListener;
import lombok.Getter;
import net.kyori.text.TextComponent;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.*;

public class TablistManager extends Manager {

    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Getter
    private final RepublicraftPlugin plugin;
    int MAX_BY_ROW = 27;
    private TabListListener listener;

    /**
     * Constructeur privé
     */
    public TablistManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    public static void insertIntoTabListCleanly(TabList list, TabListEntry entry, List<UUID> toKeep) {
        UUID inUUID = entry.getProfile().getId();
        List<UUID> containedUUIDs = new ArrayList<>();
        Map<UUID, TabListEntry> cache = new HashMap<>();

        for (TabListEntry current : list.getEntries()) {
            containedUUIDs.add(current.getProfile().getId());
            cache.put(current.getProfile().getId(), current);
        }

        if (!containedUUIDs.contains(inUUID)) {
            list.addEntry(entry);
        } else {
            TabListEntry currentEntry = cache.get(inUUID);
            if (!currentEntry.getDisplayName().equals(entry.getDisplayName())) {
                list.removeEntry(inUUID);
                list.addEntry(entry);
            }
        }

        toKeep.add(inUUID);
    }

    @Override
    public void start() {
        listener = new TabListListener(plugin);
        plugin.getProxy().getEventManager().register(plugin, listener);
        scheduler.scheduleAtFixedRate(this::update, 0, 10, TimeUnit.SECONDS);
    }

    public void playerJoin(Player player) {
        update();
    }

    public void playerQuit(Player player) {
        update();
    }

    @Override
    public void stop() {
        plugin.getProxy().getEventManager().unregisterListener(plugin, listener);
    }

    private void update() {
        TextComponent header = TextComponent.builder()
                .append(h("========================="))
                .append(g(" Républicraft "))
                .append(h("=========================\n"))
                .append(s(getPlugin().getProxy().getPlayerCount() + " joueurs en ligne\n"))
                .build();

        if (getPlugin().getProxy().getPlayerCount() > 0) {
            for (Player currentPlayer : getPlugin().getProxy().getAllPlayers()) {
                TabList tabList = currentPlayer.getTabList();

//                if (!ConfigManager.isServerAllowed(currentPlayer.getCurrentServer())) {
//                    continue;
//                }

                List<UUID> toKeep = new ArrayList<>();

                // Real players
                for (Player tabPlayer : getPlugin().getProxy().getAllPlayers()) {

                    TextComponent.Builder dn = TextComponent.builder()
                            .append(" ")
                            .append(formatDonator(tabPlayer))
                            .append(formatStaff(tabPlayer))
                            .append(tabPlayer.getUsername());

                    if(tabPlayer.getCurrentServer().isPresent()){
                        dn.append(t(" ("+tabPlayer.getCurrentServer().get().getServerInfo().getName()+")"));
                    }

                    TabListEntry currentEntry = TabListEntry.builder()
                            .profile(tabPlayer.getGameProfile())
                            .displayName(dn.build())
                            .tabList(tabList).build();

                    insertIntoTabListCleanly(tabList, currentEntry, toKeep);
                }

                // Fake players
//                if (ConfigManager.customTabsEnabled()) {

//                for (int i3 = 0; i3 < MAX_BY_ROW; i3++) {
//                    GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + i3);
//                    TabListEntry currentEntry = TabListEntry.builder()
//                            .profile(tabProfile)
//                            .displayName(TextComponent.of(" " + i3))
//                            .tabList(tabList).build();
//
//                    insertIntoTabListCleanly(tabList, currentEntry, toKeep);
//                }


                // Update entries in tab list
                for (TabListEntry current : tabList.getEntries()) {
                    if (!toKeep.contains(current.getProfile().getId()))
                        tabList.removeEntry(current.getProfile().getId());
                }

                TextComponent footer;
                if (currentPlayer.getCurrentServer().isPresent()) {
                    footer = TextComponent.builder()
                            .append(h("\n========================= "))
                            .append(p(currentPlayer.getCurrentServer().get().getServer().getServerInfo().getName()))
                            .append(h(" ========================="))
                            .build();
                } else {
                    footer = TextComponent.builder()
                            .append(h("\n========================= "))
                            .append(p("-------"))
                            .append(h(" ========================="))
                            .build();
                }

                // Set tab list headers and footers
                tabList.setHeaderAndFooter(header, footer);
            }
        }
    }
}
