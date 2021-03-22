package fr.republicraft.papermc.world.api.utils;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class EncoderUtils {

    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static String toBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack[] fromBase64ToStack(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] inventory = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < inventory.length; i++) {
                inventory[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
    }

    public static String serializeItemStacks(ItemStack[] stacks) {
        return EncoderUtils.toBase64(stacks);
    }

    public static ItemStack[] deserializeItemStacks(String stacks) {
        return EncoderUtils.fromBase64ToStack(stacks);
    }

    public static String serializeItemStack(ItemStack stack) {
        return EncoderUtils.toBase64(new ItemStack[]{stack});
    }

    public static ItemStack deserializeItemStack(String stack) {
        ItemStack[] stacks = EncoderUtils.fromBase64ToStack(stack);
        return stacks != null ? stacks[0] : null;
    }


    public static Map<String, Object> serializeVehicle(@Nullable Entity vehicle){
        if(vehicle instanceof Horse){
            Horse horse = ((Horse) vehicle);
            return ImmutableMap.<String, Object>builder()
                    .put("type", EntityType.HORSE.name())
                    .put("inventory", serializeItemStacks(horse.getInventory().getContents()))
                    .put("is-tamed", horse.isTamed())
                    .put("color", horse.getColor().name())
                    .put("style", horse.getStyle().name())
                    .put("absorption", horse.getAbsorptionAmount())
                    .put("health", horse.getHealth())
                    .build();
        }else if (vehicle instanceof Pig){
            Pig pig = ((Pig) vehicle);
            return ImmutableMap.<String, Object>builder()
                    .put("absorption", pig.getAbsorptionAmount())
                    .put("health", pig.getHealth())
                    .build();
        }
        // TODO add other vehicles
        return null;
    }

    public static void deSerializeVehicle(Player player, Map<String, Object> vehicle){
        if(vehicle != null) {
            EntityType type = EntityType.valueOf((String) vehicle.get("type"));
            if (EntityType.HORSE.equals(type)) {
                Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE); // Spawns the horse
                horse.getInventory().setContents(deserializeItemStacks((String) vehicle.get("inventory"))); // Gives horse saddle
                horse.setTamed((Boolean) vehicle.get("is-tamed")); // Sets horse to tamed
                horse.setColor(Horse.Color.valueOf((String) vehicle.get("color"))); // Sets horse to tamed
                horse.setStyle(Horse.Style.valueOf((String) vehicle.get("style"))); // Sets horse to tamed
                horse.setAbsorptionAmount((Double) vehicle.get("absorption")); // Sets horse to tamed
                horse.setHealth((Double) vehicle.get("health")); // Sets horse to tamed
                horse.setOwner(player); // Makes the horse the players
            }
        }
        // TODO add other vehicles
    }

    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
