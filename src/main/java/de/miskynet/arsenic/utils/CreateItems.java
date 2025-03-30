package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CreateItems {

    // create an item stack from the config.yml
    public static ItemStack createItemStackfromDefaultConfig(String key, String type) {

        String material = Main.getInstance().getConfig().getString("menu." + key + type + ".material") != null ? Main.getInstance().getConfig().getString("menu." + key + type + ".material") : "STONE";
        String displayName = Main.getInstance().getConfig().getString("menu." + key + type + ".displayName") != null ? Main.getInstance().getConfig().getString("menu." + key + type + ".displayName") : null;
        List<String> lore = Main.getInstance().getConfig().getStringList("menu." + key + type + ".lore") != null ? Main.getInstance().getConfig().getStringList("menu." + key + type + ".lore") : null;
        Integer customModelData = Main.getInstance().getConfig().get("menu." + key + type + ".customModelData") != null ? Main.getInstance().getConfig().getInt("menu." + key + type + ".customModelData") : null;
        Integer amount = Main.getInstance().getConfig().get("menu." + key + type + ".amount") != null ? Main.getInstance().getConfig().getInt("menu." + key + type + ".amount") : 1;

        ItemStack itemStack = new ItemStack(Material.getMaterial(material));

        // Get the item meta
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (material == null) {
            Bukkit.getLogger().severe("§cInvalid material for key: " + key);
            return new ItemStack(Material.AIR);
        }

        // Set the display name
        if (displayName != null) {
            itemMeta.setDisplayName("§r" + displayName);
        }

        // Set the lore
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, "§r" + lore.get(i));
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

    // set the material, name, lore, amount and custom model data
    public static ItemStack createItemStackFromConfig(String fileName, String key, Boolean allowGeneralLore) {

        // get the material, name and lore
        String material = CustomConfigs.get(fileName).getString("items." + key + ".material") != null ? CustomConfigs.get(fileName).getString("items." + key + ".material") : "STONE";
        String displayName = CustomConfigs.get(fileName).getString("items." + key + ".displayName") != null ? CustomConfigs.get(fileName).getString("items." + key + ".displayName"): null;
        List<String> loreFromItem = CustomConfigs.get(fileName).getStringList("items." + key + ".lore") != null ? CustomConfigs.get(fileName).getStringList("items." + key + ".lore") : null;
        List<String> loreGeneral = Main.getInstance().getConfig().getStringList("generalItemLore.lore") != null ? Main.getInstance().getConfig().getStringList("generalItemLore.lore") : null;
        Integer customModelData = CustomConfigs.get(fileName).get("items." + key + ".customModelData") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".customModelData") : null;
        Integer amount = CustomConfigs.get(fileName).get("items." + key + ".amount") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".amount") : 1;
        Boolean showGeneralLore = CustomConfigs.get(fileName).get("items." + key + ".showGeneralLore") != null ? CustomConfigs.get(fileName).getBoolean("items." + key + ".showGeneralLore") : true;;

        ItemStack itemStack = new ItemStack(Material.getMaterial(material));

        // get the item meta
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (material == null) {
            Bukkit.getLogger().severe("§cInvalid material for key: " + key);
            return new ItemStack(Material.AIR);
        }

        // set the display name
        if (displayName != null) {
            itemMeta.setDisplayName(ReplaceString.replaceString(fileName, displayName, key));
        }

        // set the lore
        if (loreFromItem != null || loreGeneral != null) {

            // lore is from the config
            if (loreFromItem == null && loreGeneral != null && allowGeneralLore && showGeneralLore) {
                for (int i = 0; i < loreGeneral.size(); i++) {
                    loreGeneral.set(i,"§r" + ReplaceString.replaceString(fileName, loreGeneral.get(i), key));
                }
                if (!(fileName.equals("buyMenu"))) {
                    itemMeta.setLore(loreGeneral);
                }

                // lore is from the item
            }else if (loreFromItem != null && loreGeneral == null) {
                for (int i = 0; i < loreFromItem.size(); i++) {
                    loreFromItem.set(i,"§r" + ReplaceString.replaceString(fileName, loreFromItem.get(i), key));
                }
                itemMeta.setLore(loreFromItem);

                // lore is from both
            }else {
                List<String> combinedLore = new ArrayList<>();

                // check if the general lore should be below the item lore
                if (Main.getInstance().getConfig().getString("generalItemLore.behavior").equals("below")) {
                    combinedLore.addAll(loreFromItem);
                    if (!(fileName.equals("buyMenu"))) {
                        if (allowGeneralLore && showGeneralLore) {
                            combinedLore.addAll(loreGeneral);
                        }
                    }
                    // set the general lore above the item lore
                }else {
                    if (!(fileName.equals("buyMenu"))) {
                        if (allowGeneralLore && showGeneralLore) {
                            combinedLore.addAll(loreGeneral);
                        }
                    }
                    combinedLore.addAll(loreFromItem);
                }
                for (int i = 0; i < combinedLore.size(); i++) {
                    combinedLore.set(i,"§r" +  ReplaceString.replaceString(fileName, combinedLore.get(i), key));
                }
                itemMeta.setLore(combinedLore);
            }
        }

        // set the custom model data
        if (customModelData != null) {
            itemMeta.setCustomModelData(customModelData);
        }

        // set the amount
        if (amount != null && amount > 1) {
            itemStack.setAmount(amount);
        }

        // set the item meta
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
