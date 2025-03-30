package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InventoryHelper {

    // the key for the item the user wants to buy
    public static String itemToBuyKey;
    static HashMap<UUID, String> playerKey = new HashMap<>();

    // set up the buy inventory
    public static Inventory setupBuyInventory(Player player, String key) {

        // create the inventory
        Inventory inventory = createInventory("buyMenu");

        // add the clicked item to the inventory
        int clickItemSlot = CustomConfigs.get("buyMenu").getInt("clickedItemSlot");

        // get the item stack and add it
        ItemStack itemStackToBuy = Main.itemsShopMenu.get(key);
        inventory.setItem(clickItemSlot, Main.itemsShopMenu.get(key));

        // create the item that the player wants to buy
        for (String secondKey : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

            ItemStack itemStack = Main.itemsShopMenu.get(key);

            if (itemStackToBuy.isSimilar(itemStack)) {
                playerKey.put(player.getUniqueId(), secondKey);
            }
        }

        // loop through all items in the config and add them to the inventory
        for (String secondKey : CustomConfigs.get("buyMenu").getConfigurationSection("items").getKeys(false)) {

            ItemStack itemStack = Main.itemsBuyMenu.get(secondKey);

            int slot = CustomConfigs.get("buyMenu").getInt("items." + secondKey + ".slot");
            if (slot != 0) {
                inventory.setItem(slot - 1, itemStack);
            } else {
                inventory.addItem(itemStack);
            }
        }

        return inventory;
    }

    // create an inventory
    public static Inventory createInventory(String fileName) {

        String title = CustomConfigs.get(fileName).getString("title") != null ? CustomConfigs.get(fileName).getString("title") : Main.missingString;
        Integer rows = CustomConfigs.get(fileName).get("rows") != null ? CustomConfigs.get(fileName).getInt("rows") * 9 : 9;

        Inventory inventory = Bukkit.createInventory(null, rows, title);

        return inventory;
    }

    // check if an item can be added to the player's inventory
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
}