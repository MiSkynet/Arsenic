package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryHelper {

    // Create an inventory
    public static Inventory createInventory(String fileName) {

        String title = Main.missingString;
        Integer rows = 9;

        try {
            title = CustomConfigs.get(fileName).getString("title");
            rows = CustomConfigs.get(fileName).getInt("rows") * 9;
        }catch (NullPointerException ignored) {}

        Inventory inventory = Bukkit.createInventory(null, rows, title);
        return inventory;
    }

    // Set the material, name, lore, amount and custommodeldata
    public static ItemStack createItemStackFromConfig(String fileName, String key, Boolean allowGeneralLore) {

        // Get the material, name and lore
        String material = "STONE";
        String displayName = null;
        List<String> loreFromItem = null;
        List<String> loreGeneral = null;
        Integer customModelData = null;
        Integer amount = null;
        Boolean showGeneralLore = true;

        try {
            material = CustomConfigs.get(fileName).getString("items." + key + ".material");
            displayName = CustomConfigs.get(fileName).getString("items." + key + ".displayName");
            loreFromItem = CustomConfigs.get(fileName).getStringList("items." + key + ".lore");
            loreGeneral = Main.getInstance().getConfig().getStringList("generalItemLore.lore");
            customModelData = CustomConfigs.get(fileName).getInt("items." + key + ".customModelData");
            amount = CustomConfigs.get(fileName).getInt("items." + key + ".amount");
            showGeneralLore = CustomConfigs.get(fileName).get("items." + key + ".showGeneralLore") != null ? CustomConfigs.get(fileName).getBoolean("items." + key + ".showGeneralLore") : true;
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
            itemMeta.setDisplayName(InventoryHelper.replaceString(fileName, displayName, key));
        }

        // Set the lore
        if (loreFromItem != null || loreGeneral != null) {

            // Lore is from the config
            if (loreFromItem == null && loreGeneral != null && allowGeneralLore && showGeneralLore) {
                for (int i = 0; i < loreGeneral.size(); i++) {
                    loreGeneral.set(i, InventoryHelper.replaceString(fileName, loreGeneral.get(i), key));
                }
                if (!(fileName.equals("buyMenu"))) {
                    itemMeta.setLore(loreGeneral);
                }

            // Lore is from the item
            }else if (loreFromItem != null && loreGeneral == null) {
                for (int i = 0; i < loreFromItem.size(); i++) {
                    loreFromItem.set(i, InventoryHelper.replaceString(fileName, loreFromItem.get(i), key));
                }
                itemMeta.setLore(loreFromItem);

            // Lore is from both
            }else {
                List<String> combinedLore = new ArrayList<>();

                // Check if the general lore should be below the item lore
                if (Main.getInstance().getConfig().getString("generalItemLore.behavior").equals("below")) {
                    combinedLore.addAll(loreFromItem);
                    if (!(fileName.equals("buyMenu"))) {
                        if (allowGeneralLore && showGeneralLore) {
                            combinedLore.addAll(loreGeneral);
                        }
                    }
                // Set the general lore above the item lore
                }else {
                    if (!(fileName.equals("buyMenu"))) {
                        if (allowGeneralLore && showGeneralLore) {
                            combinedLore.addAll(loreGeneral);
                        }
                    }
                    combinedLore.addAll(loreFromItem);
                }
                for (int i = 0; i < combinedLore.size(); i++) {
                    combinedLore.set(i, InventoryHelper.replaceString(fileName, combinedLore.get(i), key));
                }
                itemMeta.setLore(combinedLore);
            }
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

            ItemStack itemStack = createItemStackFromConfig("buyMenu", buyMenuKey, true);

            int slot = CustomConfigs.get("buyMenu").getInt("items." + buyMenuKey + ".slot");
            if (slot != 0) {
                inventory.setItem(slot - 1, itemStack);
            } else {
                inventory.addItem(itemStack);
            }
        }

        // Add the clicked item to the inventory
        int clickItemSlot = CustomConfigs.get("buyMenu").getInt("clickedItemSlot");
        inventory.setItem(clickItemSlot, createItemStackFromConfig("shop", key, true));

        return inventory;
    }

    // Check if an item can be added to the player's inventory
    public static boolean canAddItem(Player player, ItemStack item) {
        // Überprüfen, ob das Item null ist
        if (item == null || item.getType() == null || !(item.getAmount() >= 1)) {
            return false;
        }

        Integer itemToFillAmount = item.getAmount();

        // Check if there's an empty slot in the player's inventory
        if (player.getInventory().firstEmpty() == -1) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack currentItem = player.getInventory().getItem(i);

                // Check if the item is not null before accessing its properties
                if (currentItem != null && currentItem.getType().equals(item.getType())) {
                    if (currentItem.getAmount() + itemToFillAmount <= item.getMaxStackSize()) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }

        return false;
    }

    // Replace the placeholders in the strings
    public static String replaceString(String fileName, String string, String key) {

        if (fileName.equals("shop")) {
            String priceSell = null;
            String priceBuy = null;

            try {
                priceSell = CustomConfigs.get(fileName).getString("items." + key + ".price.sell");
                priceBuy = CustomConfigs.get(fileName).getString("items." + key + ".price.buy");
            }catch (NullPointerException ignored) {}

            if (priceSell != null) {
                string = string.replace("%priceSell%", priceSell);
            }

            if (priceBuy != null) {
                string = string.replace("%priceBuy%", priceBuy);
            }
        } else if (fileName.equals("buyMenu")) {
            Integer itemDataAmount = null;
            Integer itemDataPrice = null;
            String itemDataType = null;

            try {
                itemDataAmount = CustomConfigs.get(fileName).getInt("items." + key + ".itemData.amount");
                itemDataPrice = CustomConfigs.get(fileName).getInt("items." + key + ".itemData.price") * itemDataAmount;
                itemDataType = CustomConfigs.get(fileName).getString("items." + key + ".itemData.type");
            }catch (NullPointerException ignored) {}

            if (itemDataAmount != null) {
                string = string.replace("%itemDataAmount%", itemDataAmount.toString());
            }

            if (itemDataPrice != null) {
                string = string.replace("%itemDataPrice%", itemDataPrice.toString());
            }

            if (itemDataType != null) {
                string = string.replace("%itemDataType%", itemDataType);
            }
        }

        String material = null;
        String displayName = null;
        Integer customModelData = null;
        Integer slot = null;
        Integer amount = null;

        try {
            material = CustomConfigs.get(fileName).getString("items." + key + ".material");
            displayName = CustomConfigs.get(fileName).getString("items." + key + ".displayName");
            customModelData = CustomConfigs.get(fileName).getInt("items." + key + ".customModelData");
            amount = CustomConfigs.get(fileName).getInt("items." + key + ".amount");
            slot = CustomConfigs.get(fileName).getInt("items." + key + ".slot");
        }catch (NullPointerException ignored) {}

        if (material != null) {
            string = string.replace("%material%", material);
        }

        if (displayName != null) {
            string = string.replace("%displayName%", displayName);
        }

        if (customModelData != null) {
            string = string.replace("%customModelData%", customModelData.toString());
        }

        if (slot != null) {
            string = string.replace("%slot%", slot.toString());
        }

        if (amount != null) {
            string = string.replace("%amount%", amount.toString());
        }

        if (amount != null) {
            string = string.replace("%amount%", amount.toString());
        }

        if (slot != null) {
            string = string.replace("%slot%", slot.toString());
        }

        string = ChatColor.translateAlternateColorCodes('&', string);

        return string;
    }

}