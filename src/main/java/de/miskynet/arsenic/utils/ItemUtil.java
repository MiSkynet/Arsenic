package de.miskynet.arsenic.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    static FileConfiguration pagesConfig = CustomConfig.get("pages");

    // Modify the item stack (adding displayname, lore, etc.)
    public static ItemStack modifyItemStack(String key) {

        ItemStack itemStack = new ItemStack(Material.getMaterial(pagesConfig.getString("items." + key + ".material").toUpperCase()));

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (pagesConfig.getString("items." + key + ".display_name") != null) {
            itemMeta.setDisplayName("§r" + pagesConfig.getString("items." + key + ".display_name"));
        }

        List<String> loreList = new ArrayList<>();

        if (CustomConfig.get("pages").getStringList("items." + key + ".lore") != null) {
            try {
                for (String lore : CustomConfig.get("pages").getStringList("items." + key + ".lore")) {
                    loreList.add("§r" + lore);
                }
                itemMeta.setLore(loreList);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (CustomConfig.get("pages").get("items." + key + ".amount") != null) {
            try {
                itemStack.setAmount(CustomConfig.get("pages").getInt("items." + key + ".amount"));
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (CustomConfig.get("pages").get("items." + key + ".custom_model_data") != null) {
            try {
                itemMeta.setCustomModelData(CustomConfig.get("pages").getInt("items." + key + ".custom_model_data"));
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
