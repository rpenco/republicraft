//package fr.republicraft.velocity.world.commands;
//
//import fr.republicraft.papermc.world.WorldPlugin;
//import fr.republicraft.papermc.world.managers.EconomyManager;
//import lombok.Getter;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.jetbrains.annotations.NotNull;
//
//import static fr.republicraft.papermc.world.api.chat.ChatHelper.chatServerToPlayer;
//import static fr.republicraft.papermc.world.api.chat.ChatHelper.e;
//
//public class PayCommand extends Command {
//    @Getter
//    WorldPlugin plugin;
//    private final EconomyManager manager;
//
//    public PayCommand(WorldPlugin plugin) {
//        super("pay");
//        manager = (EconomyManager) plugin.getManagers().get(EconomyManager.class);
//    }
//
//    @Override
//    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
//        if (sender instanceof Player) {
//            Player playerFrom = (Player) sender;
//
//            if (args.length != 2) {
//                ChatHelper.chatServerToPlayer(playerFrom, ChatHelper.e("Usage: /pay <player> <montant>"));
//                return false;
//            }
//
//            manager.pay(args[0], Double.parseDouble(args[1]));
//
//            Player playerTo = Bukkit.getPlayer(args[0]);
//
//            if (playerTo == null) {
//                playerFrom.sendMessage(ChatColor.RED + "Joueur " + args[0] + " introuvable ou non connecté.");
//                ChatHelper.chatServerToPlayer(playerFrom, ChatHelper.e("Usage: /pay <player> <montant>"));
//                return false;
//            }
//
//
//            double amount = 0;
//            try {
//                amount = Double.parseDouble(args[1]);
//            } catch (NumberFormatException e) {
//                playerFrom.sendMessage(ChatColor.RED + "Montant invalide, doit être supérieur à 0.");
//                return false;
//            }
//
//            if (amount <= 0) {
//                playerFrom.sendMessage(ChatColor.RED + "Montant invalide, doit être supérieur à 0.");
//                return false;
//            }
//
////            manager.pay(playerFrom, playerFrom, amount);
//            playerFrom.sendMessage(ChatColor.GREEN + "Vous venez de payer " + amount + " pièces à " + playerTo.getDisplayName() + ".");
//            playerTo.sendMessage(ChatColor.GREEN + "Vous venez de recevoir " + amount + " pièces de " + playerFrom.getDisplayName() + ".");
//            return true;
//        }
//        return false;
//    }
//}
