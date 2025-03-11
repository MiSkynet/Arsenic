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

    // Create an inventory
    public static Inventory createInventory(String fileName) {
        String title = CustomConfigs.get(fileName).getString("title") != null ? CustomConfigs.get("shop").getString("title") : Main.missingString;
        Integer rows = (CustomConfigs.get(fileName).getInt("rows") != 0) && (CustomConfigs.get("shop").getInt("rows") >= 1) && (CustomConfigs.get("shop").getInt("rows") <= 6) ? CustomConfigs.get("shop").getInt("rows") * 9 : 9;

        Inventory inventory = Bukkit.createInventory(null, rows, title);
        return inventory;
    }

    // Set the Material, Name, Lore and CustomModelData
    public static ItemStack createItemStackFromConfig(String key) {

        // Get the material, name and lore
        String material = "STONE";
        String displayName = null;
        List<String> lore = null;
        Integer customModelData = null;
        Integer amount = null;

        try {
            material = CustomConfigs.get("shop").getString("items." + key + ".material");
            displayName = CustomConfigs.get("shop").getString("items." + key + ".displayName");
            lore = CustomConfigs.get("shop").getStringList("items." + key + ".lore");
            customModelData = CustomConfigs.get("shop").getInt("items." + key + ".customModelData");
            amount = CustomConfigs.get("shop").getInt("items." + key + ".amount");
        }catch (NullPointerException ignored) {}

        ItemStack itemStack = new ItemStack(Material.getMaterial(material));

        // Get the item meta
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (material == null) {
            Bukkit.getLogger().severe("Invalid material for key: " + key);
            return new ItemStack(Material.AIR);
        }

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

        // Set the amount
        if (amount != null && amount > 1) {
            itemStack.setAmount(amount);
        }

        // Set the item meta
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    // Create an item stack from an item
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

    // Set up the buy inventory
    public static Inventory setupBuyInventory(String key) {

        // Create the inventory
        Inventory inventory = createInventory("buyMenu");

        // Loop through all items in the config and add them to the inventory
        for (String buyMenuKey : CustomConfigs.get("buyMenu").getConfigurationSection("items").getKeys(false)) {

            ItemStack itemStack = createItemStackFromConfig(buyMenuKey);

            int slot = CustomConfigs.get("shop").getInt("items." + key + ".slot");
            if (slot != 0) {
                inventory.setItem(slot - 1, itemStack);
            } else {
                inventory.addItem(itemStack);
            }
        }

        // Add the clicked item to the inventory
        int clickItemSlot = CustomConfigs.get("shop").getInt("clickedItemSlot");
        inventory.setItem(clickItemSlot, createItemStackFromConfig(key));

        return inventory;
    }
}