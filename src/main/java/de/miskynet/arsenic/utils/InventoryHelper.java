package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static de.miskynet.arsenic.Main.replaceString;

public class InventoryHelper {

//    public static void getEachItem() {
//
//        // Get each item for the inventory
//        try {
//            CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false).forEach(key -> {
//
//                String item = CustomConfigs.get("shop").getString("items." + key + ".material");
//                Bukkit.getLogger().info("Key: " + key);
//                Bukkit.getLogger().info("Item: " + item);
//
//            });
//        }catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }

    // Set the Material, Name, Lore and CustomModelData
    public static ItemStack createItemStack(String key) {

        // Get the material, name and lore
        String material = null;
        String itemName = "";
        List<String> lore = null;
        Integer customModelData = null;

        try {
            material = CustomConfigs.get("shop").getString("items." + key + ".material");
            itemName = CustomConfigs.get("shop").getString("items." + key + ".name");
            lore = CustomConfigs.get("shop").getStringList("items." + key + ".lore");
            customModelData = CustomConfigs.get("shop").getInt("items." + key + ".customModelData");
        }catch (NullPointerException ignored) {}

        ItemStack itemStack = new ItemStack(Material.getMaterial(material));

        // Get the item meta
        ItemMeta itemMeta = itemStack.getItemMeta();

        // Set the display name
        if (itemName != null) {
            itemMeta.setDisplayName(Main.replaceString(itemName, key));
        }

        // Set the lore
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, Main.replaceString(lore.get(i), key));
            }
            itemMeta.setLore(lore);
        }

        // Set the custom model data
        if (customModelData != null) {
            itemMeta.setCustomModelData(customModelData);
        }

        // Set the item meta
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}