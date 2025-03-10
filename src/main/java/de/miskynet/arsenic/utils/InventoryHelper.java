package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryHelper {

    // Set the Material, Name, Lore and CustomModelData
    public static ItemStack createItemStackFromConfig(String key) {

        // Get the material, name and lore
        String material = null;
        String displayName = null;
        List<String> lore = null;
        Integer customModelData = null;

        try {
            material = CustomConfigs.get("shop").getString("items." + key + ".material");
            displayName = CustomConfigs.get("shop").getString("items." + key + ".displayName");
            lore = CustomConfigs.get("shop").getStringList("items." + key + ".lore");
            customModelData = CustomConfigs.get("shop").getInt("items." + key + ".customModelData");
        }catch (NullPointerException ignored) {}

        ItemStack itemStack = new ItemStack(Material.getMaterial(material));

        // Get the item meta
        ItemMeta itemMeta = itemStack.getItemMeta();

        // Set the display name
        if (displayName != null) {
            itemMeta.setDisplayName(Main.replaceString(displayName, key));
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

    public static ItemStack createItemStackFromItem(Material material, String displayName, List<String> lore, Integer customModelData) {

        // Get the item meta
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        // Set the display name
        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }

        // Set the lore
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i));
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

    public static Inventory setupBuyInventory(String key) {

        Inventory inventory = Bukkit.createInventory(null, 27, "Buy " + key);

        List<String> lore = new ArrayList<>();
        lore.add("§7Left click to buy");
        inventory.setItem(9, createItemStackFromItem(Material.LIME_STAINED_GLASS_PANE, "§a§lBuy 1", lore, null));
        inventory.setItem(10, createItemStackFromItem(Material.LIME_STAINED_GLASS_PANE, "§a§lBuy 2", lore, null));
        inventory.setItem(11, createItemStackFromItem(Material.LIME_STAINED_GLASS_PANE, "§a§lBuy 8", lore, null));
        inventory.setItem(12, createItemStackFromItem(Material.LIME_STAINED_GLASS_PANE, "§a§lBuy 32", lore, null));

        inventory.setItem(13, createItemStackFromConfig(key));

        inventory.setItem(14, createItemStackFromItem(Material.RED_STAINED_GLASS_PANE, "§c§lSell 32", lore, null));
        inventory.setItem(15, createItemStackFromItem(Material.RED_STAINED_GLASS_PANE, "§c§lSell 8", lore, null));
        inventory.setItem(16, createItemStackFromItem(Material.RED_STAINED_GLASS_PANE, "§c§lSell 2", lore, null));
        inventory.setItem(17, createItemStackFromItem(Material.RED_STAINED_GLASS_PANE, "§c§lSell 1", lore, null));

        return inventory;
    }

}