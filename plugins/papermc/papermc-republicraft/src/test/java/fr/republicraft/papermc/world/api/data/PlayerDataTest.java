package fr.republicraft.papermc.world.api.data;

import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerDataTest {


    @Test
    public void serializeDeserializePlayerData(){
//        Player p = new PlayerMock();
//        PlayerData dataSerialize = new PlayerData();
//        dataSerialize.setPlayer(p);
//        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
//        itemStack.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
//        itemStack.addEnchantment(Enchantment.PROTECTION_FIRE, 2);
//        Gson gson = new Gson();

//        String message = gson.toJson(p.getItemOnCursor());
//        System.out.println(message);
//        System.out.println(JsonItemStack.encodeItem(p.getItemOnCursor()));;

//        System.out.println(dataSerialize.toJSON());

//        PlayerData data = PlayerData.fromJSON(dataSerialize.toJSON());
//        assertEquals(10, data.getExp());
//        assertEquals(100, data.getLevel());
//        assertEquals(1234, data.getTotalExperience());
//        assertEquals(3, data.getAbsorptionAmount());
//        assertEquals(20, data.getMaximumAir());
//        assertEquals(11, data.getRemainingAir());
//        assertEquals(13, data.getHealth());
//        assertEquals(1, data.getFireTicks());
//        assertEquals(111, data.getMaxFireTicks());
//        assertEquals(123, data.getFoodLevel());
//        assertEquals(133, data.getFlySpeed());
//        assertEquals(144, data.getWalkSpeed());
//        assertEquals(11293, data.getTicksLived());
//        assertEquals(9, data.getSaturation());
//        assertEquals(p.getItemOnCursor().getAmount(), data.getItemOnCursor().getAmount());
    }
}
