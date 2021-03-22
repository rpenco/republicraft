package fr.republicraft.papermc.shops.commands;

import com.google.common.collect.ImmutableList;
import fr.republicraft.papermc.shops.ShopsPlugin;
import fr.republicraft.papermc.shops.managers.HdvManager;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

import static fr.republicraft.papermc.world.api.chat.ChatHelper.*;

public class HdvCommand extends Command {
    @Getter
    private final HdvManager manager;

    @Getter
    private final ShopsPlugin plugin;

    public HdvCommand(ShopsPlugin plugin) {
        super("hdv");
        this.plugin = plugin;
        manager = (HdvManager) plugin.getRepublicraftPlugin().getManagers().get(HdvManager.class);
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            return ImmutableList.of();
        } else if (args.length == 1) {
            return ImmutableList.of("sell");
        } else if (args.length == 2) {
            return Arrays.stream(((Player) sender).getInventory().getContents()).filter(Objects::nonNull)
                    .map(itemStack -> itemStack.getType().getKey().getKey()).collect(Collectors.toList());
        } else if (args.length == 3) {
            Optional<ItemStack> first = Arrays.stream(((Player) sender).getInventory().getContents()).filter(Objects::nonNull)
                    .filter(itemStack -> {
                        System.out.println("item key=" + itemStack.getType().getKey().getKey() + " eql=" + args[1]);
                        return itemStack.getType().getKey().getKey().equals(args[1]);
                    }).findFirst();
            if (first.isPresent()) {
                String amount = String.valueOf(first.get().getAmount());
                return ImmutableList.of("1", String.valueOf(Math.round(Float.parseFloat(amount) / 2)), amount);
            } else {
                return ImmutableList.of("0");
            }
        } else if (args.length == 4) {
            return ImmutableList.of("0.5", "1.0", "2.0", "3.0");
        }
        return ImmutableList.of();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1 && "help".equals(args[0])) {
                sender.sendMessage(usage());
                return true;
            } else if (args.length == 4) {
                if (args[0].equals("sell")) {
                    Player player = (Player) sender;
                    List<ItemStack> selled = new ArrayList<>();
                    ItemStack material = null;
                    int sellSize = Integer.parseInt(args[2]);
                    for (ItemStack content : player.getInventory().getContents()) {
                        if (content == null)
                            continue;
                        material = content;
                        if (material.getType().getKey().getKey().equals(args[1])) {
                            System.out.println("material=" + material.getType().getKey().getKey() + " find=" + args[1] + " amount=" + content.getAmount());
                            if (content.getAmount() >= sellSize) {
                                for (int i = 0; i < sellSize; i++) {
                                    selled.add(content.clone());
                                    content.subtract(1);
                                }
                            }
                            break;
                        }
                    }
                    if (selled.size() == Integer.parseInt(args[2])) {
                        double amount = Double.parseDouble(args[3]);
                        String name = selled.get(0).getType().getKey().toString();
                        getPlugin().getLogger().info("message=\"player sales items\" player=\"" + player.getUniqueId() + "\" amount=\"" + amount + "\" quantity=\"" + selled.size() + "\" material=\"" + name + "\"");

                        manager.sellItem(player, name, selled.size(), amount, args[1]);
                        chatServerToPlayer(player,
                                s("Tu as mis en vente " + h(selled.size() + " " + args[1]) + " pour " + m(amount)));

                        return true;
                    } else {
                        selled.forEach(itemStack -> {
                            player.getInventory().addItem(itemStack);
                        });
                        chatServerToPlayer((Player) sender, e("Tu n'as pas la quantité de " + h(args[1]) + " nécessaire."));
                        return false;
                    }
                }
                chatServerToPlayer((Player) sender, e("Usage: /hdv <sell> <item> <quantité> <prix>"));
            }
        }
        return false;
    }

    String usage() {
        return "§e-------------------------- §fHôtel des Ventes §e---------------------------\n" +
                "§7Vendres des items à l'§3Hôtel des Ventes§7 du serveur\n" +
                "§7Tout le monde peut vendre et acheter. L'HdV se situe au lobby.\n" +
                "§6/hdv sell <§eitem§6> <§equantité§6> <§eprix§6>: §fMettre en vente des items de ton inventaire\n" +
                "§e--------------------------------       --------------------------------";
    }
}
