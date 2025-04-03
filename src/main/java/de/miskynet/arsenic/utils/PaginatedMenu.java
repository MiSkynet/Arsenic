package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PaginatedMenu {

    public static HashMap<UUID, Integer> playerPage = new HashMap<>();

    // create an inventory
    public static Inventory setupInventory(Player player) {

        if (!(playerPage.containsKey(player.getUniqueId()))) {
            playerPage.put(player.getUniqueId(), 1);
        }

        Inventory inventory = InventoryHelper.createInventory("shop");

        addItems(inventory, player);

        return inventory;
    }

    // add the items to the page
    private static Inventory addItems(Inventory inventory, Player player) {

        Integer maxIndex = Main.itemsShopMenu.size();
        Integer shopSize = CustomConfigs.get("shop").getInt("rows") * 9;

        // CPP = current player page
        Integer cpp = playerPage.get(player.getUniqueId());

        // add the next/previous page button
        for (String nextPreviousPage: Main.getInstance().getConfig().getConfigurationSection("menu").getKeys(false)) {

            // check if the item should be available or unavailable
            if (nextPreviousPage.equals("next-page")) {

                if (maxIndex >= (cpp * shopSize)) {

                    ItemStack itemStack = CreateItems.createItemStackfromDefaultConfig(nextPreviousPage, ".available");
                    int slot = Main.getInstance().getConfig().getInt("menu." + nextPreviousPage + ".available.slot");

                    inventory.setItem(slot - 1, itemStack);
                }else {

                    ItemStack itemStack = CreateItems.createItemStackfromDefaultConfig(nextPreviousPage, ".unavailable");
                    int slot = Main.getInstance().getConfig().getInt("menu." + nextPreviousPage + ".unavailable.slot");

                    inventory.setItem(slot - 1, itemStack);
                }
            }

            if (nextPreviousPage.equals("previous-page")) {

                if (cpp != 1) {

                    ItemStack itemStack = CreateItems.createItemStackfromDefaultConfig(nextPreviousPage, ".available");
                    int slot = Main.getInstance().getConfig().getInt("menu." + nextPreviousPage + ".available.slot");

                    inventory.setItem(slot - 1, itemStack);
                }else {

                    ItemStack itemStack = CreateItems.createItemStackfromDefaultConfig(nextPreviousPage, ".unavailable");
                    int slot = Main.getInstance().getConfig().getInt("menu." + nextPreviousPage + ".unavailable.slot");

                    inventory.setItem(slot - 1, itemStack);
                }
            }
        }

        Integer index = 0;

        // add the items, that you can buy, to the inventory
        for (String buyMenuKey : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

            if (!(index < (cpp * shopSize - shopSize) - 2) && !(index - 2 > cpp * shopSize)) {

                Integer slot = CustomConfigs.get("shop").getInt("items." + buyMenuKey + ".slot");

                ItemStack itemStack = Main.itemsShopMenu.get(buyMenuKey);

                if (slot - 1 <= shopSize) {
                    if (slot != 0) {
                        inventory.setItem(slot - 1, itemStack);
                    } else {
                        inventory.addItem(itemStack);
                    }
                }
            }

            index++;
        }
        return inventory;
    }
}
