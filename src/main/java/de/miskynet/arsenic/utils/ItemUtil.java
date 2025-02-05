package de.miskynet.arsenic.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    static FileConfiguration pagesConfig = CustomConfig.get("pages");

    // Modify the item stack (adding displayname, lore, etc.)
    public static ItemStack modifyItemStack(String key, Boolean displayNameBoolean, Boolean loreBoolean, Boolean amountBoolean, Boolean customModelDataBoolean) {

        // Create the item stack
        ItemStack itemStack = new ItemStack(Material.getMaterial(pagesConfig.getString("items." + key + ".material").toUpperCase()));

        ItemMeta itemMeta = itemStack.getItemMeta();

        // Set the display name
        if (displayNameBoolean) {
            if (pagesConfig.getString("items." + key + ".display_name") != null) {
                String displayName = pagesConfig.getString("items." + key + ".display_name");

                // Replace placeholders
                displayName = pagesConfig.get("items." + key + ".price.buy") != null ? displayName.replace("%buy_price%", String.valueOf(pagesConfig.getInt("items." + key + ".price.buy"))) : displayName;
                displayName = pagesConfig.get("items." + key + ".price.sell") != null ? displayName.replace("%sell_price%", String.valueOf(pagesConfig.getInt("items." + key + ".price.sell"))) : displayName;
                displayName = setColor(displayName);
                itemMeta.setDisplayName("§r" + displayName);
            }
        }

        // Set the lore
        if (loreBoolean) {
            List<String> loreList = new ArrayList<>();
            if (CustomConfig.get("pages").getStringList("items." + key + ".lore") != null) {
                try {
                    for (String lore : CustomConfig.get("pages").getStringList("items." + key + ".lore")) {

                        // Replace placeholders
                        lore = pagesConfig.get("items." + key + ".price.buy") != null ? lore.replace("%buy_price%", String.valueOf(pagesConfig.getInt("items." + key + ".price.buy"))) : lore;
                        lore = pagesConfig.get("items." + key + ".price.sell") != null ? lore.replace("%sell_price%", String.valueOf(pagesConfig.getInt("items." + key + ".price.sell"))) : lore;
                        loreList.add("§r" + setColor(lore));
                    }
                    itemMeta.setLore(loreList);
                } catch (NullPointerException e) {
                    Bukkit.getLogger().config("Couldn't set lore for item: " + key);
                    e.printStackTrace();
                }
            }
        }

        // Set the amount
        if (amountBoolean) {
            if (CustomConfig.get("pages").get("items." + key + ".amount") != null) {
                try {
                    itemStack.setAmount(CustomConfig.get("pages").getInt("items." + key + ".amount"));
                }catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        // Set the custom model data
        if (customModelDataBoolean) {
            if (CustomConfig.get("pages").get("items." + key + ".custom_model_data") != null) {
                try {
                    itemMeta.setCustomModelData(CustomConfig.get("pages").getInt("items." + key + ".custom_model_data"));
                }catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static String setColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
