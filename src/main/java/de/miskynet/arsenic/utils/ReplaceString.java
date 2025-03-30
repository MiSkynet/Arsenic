package de.miskynet.arsenic.utils;

import org.bukkit.ChatColor;

public class ReplaceString {

    // replace the placeholders in the strings
    public static String replaceString(String fileName, String string, String key) {

        if (string == null) {
            return "";
        }

        if (fileName.equals("shop")) {
            Integer priceSell = CustomConfigs.get(fileName).get("items." + key + ".price.sell") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".price.sell") : null;
            Integer priceBuy = CustomConfigs.get(fileName).get("items." + key + ".price.buy") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".price.buy") : null;

            if (priceSell != null) {
                string = string.replace("%priceSell%", "" + priceSell);
            }

            if (priceBuy != null) {
                string = string.replace("%priceBuy%", "" + priceBuy);
            }
        } else if (fileName.equals("buyMenu")) {

            Integer itemToBuyPriceBuy = CustomConfigs.get("shop").getInt("items." + InventoryHelper.itemToBuyKey + ".price.buy");
            Integer itemToBuyPriceSell = CustomConfigs.get("shop").getInt("items." + InventoryHelper.itemToBuyKey + ".price.sell");

            Integer itemDataAmount = CustomConfigs.get(fileName).get("items." + key + ".itemData.amount") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".itemData.amount") : null;
            String itemDataType = CustomConfigs.get(fileName).getString("items." + key + ".itemData.type") != null ? CustomConfigs.get(fileName).getString("items." + key + ".itemData.type") : null;


            // Replace the amount
            if (itemDataAmount != null) {
                string = string.replace("%itemDataAmount%", itemDataAmount.toString());
            }

            // Replace the price
            string = string.replace("%itemPriceBuy%", "" + itemToBuyPriceBuy * itemDataAmount);
            string = string.replace("%itemPriceSell%", "" + itemToBuyPriceSell * itemDataAmount);

            if (itemDataType != null) {
                string = string.replace("%itemDataType%", itemDataType);
            }

        }

        String material = CustomConfigs.get(fileName).getString("items." + key + ".material") != null ? CustomConfigs.get(fileName).getString("items." + key + ".material") : null;
        String displayName = CustomConfigs.get(fileName).getString("items." + key + ".displayName") != null ? CustomConfigs.get(fileName).getString("items." + key + ".displayName") : null;
        Integer customModelData = CustomConfigs.get(fileName).get("items." + key + ".customModelData") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".customModelData") : null;
        Integer slot = CustomConfigs.get(fileName).get("items." + key + ".amount") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".amount") : null;
        Integer amount = CustomConfigs.get(fileName).get("items." + key + ".slot") != null ? CustomConfigs.get(fileName).getInt("items." + key + ".slot") : null;

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
