package fr.republicraft.papermc.world.api.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookBuilder {

    public static ItemStack GiveBook(Player pl, String Auteur, String Title, String[] pages) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta) book.getItemMeta();
        bm.setAuthor(Auteur);
        bm.setTitle(Title);
        bm.setPages(pages);
        book.setItemMeta(bm);
        return book;
    }

    public static ItemStack GiveCommandsBook(Player player) {
        //TODO améliorer la page de presentation (presentation/lobby/construction/resources)
        //TODO informer des regles/servers
        //TODO informer restart server tous les jours 05h00 (progessif total ~10min)
        String[] pages = new String[]{
                "\n" +
                        "§2Guide commandes de base§0\n" +
                        "\n" +
                        "§9PAGE: 2\n" +
                        "  - §0Commandes omniprésentes\n" +
                        "§9PAGE: 3\n" +
                        "  - §0Commandes Constructions\n" +
                        "§9PAGE: 4\n" +
                        "  - §0Commandes Ressources\n" +
                        "§9PAGE: 5\n" +
                        "  - §0Commandes Hôtel des ventes\n" +
                        "\n",
                "\n" +
                        "§2Commandes omniprésentes§0\n" +
                        "\n" +
                        "- §1/book \n" +
                        "  §0obtenir le livre d'aide\n" +
                        "- §1/server <server> \n" +
                        "  §0changer de serveur\n" +
                        "- §1/home \n" +
                        "  §0ajouter/supprimer des homes\n" +
                        "- §1/spawn \n" +
                        "  §0Retourner au Spawn du monde\n" +
                        "",
                "\n" +
                        "§2Commandes Constructions§0\n" +
                        "\n" +
                        "- §1/claimlist \n" +
                        "  §0liste tes claims\n" +
                        "- §1/buyclaimblocks \n" +
                        "  §0acheter des blocs de claims\n" +
                        "- §1/sellclaim \n" +
                        "  §0supprimer le claim\n" +
                        "",
                "\n" +
                        "§2Commandes Ressources§0\n" +
                        "\n" +
                        "- §0 Aucune commande spécifique\n" +
                        "",
                "\n" +
                        "§2Commandes Hôtel des ventes§0\n" +
                        "\n" +
                        "- §1/hdv sell §6<item> <qté> <prix total>§0\n" +
                        "  §0vends des items en ta possession\n" +
                        "\n" +
                        "§1L'achat d'items §0 se fait au §2lobby§0 avec le villageois §1Maître des ventes.\n" +
                        "",
        };
        return GiveBook(player, "Républicraft", "Commandes de bases", pages);
    }
}
