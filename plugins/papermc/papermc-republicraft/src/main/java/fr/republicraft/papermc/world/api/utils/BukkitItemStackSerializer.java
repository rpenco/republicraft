package fr.republicraft.papermc.world.api.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

class BukkitItemStackSerializer {

    /**
     * Deserialize a serialized byte array to an ItemStack array.
     *
     * @param data The data that should get deserialized.
     * @return The deserialized ItemStack array. null if deserialization failed.
     */
    public ItemStack[] deserialize(byte[] data)
    {
        if(data != null)
        {
            try(BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(new ByteArrayInputStream(data)))
            {
                return (ItemStack[]) bukkitObjectInputStream.readObject();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Serializes a ItemStack array to a byte array.
     *
     * @param itemStacks The ItemStacks that should be serialized.
     * @return Serialized ItemsStacks as byte array. null if serialization failed.
     */
    public byte[] serialize(ItemStack[] itemStacks)
    {
        byte[] ba = null;
        if(itemStacks != null)
        {
            try(ByteArrayOutputStream b = new ByteArrayOutputStream(); BukkitObjectOutputStream output = new BukkitObjectOutputStream(b))
            {
                output.writeObject(itemStacks);
                output.flush();
                ba = b.toByteArray();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return ba;
    }

}
