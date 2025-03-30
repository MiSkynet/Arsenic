package de.miskynet.arsenic.listeners;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        // check if the player clicked an item
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == null) {
            event.setCancelled(true);
            return;
        }

        String mainShop = CustomConfigs.get("shop").getString("title");
        String buyMenu = CustomConfigs.get("buyMenu").getString("title");

        if (event.getView().getTitle().equals(mainShop)) {

            // loop through all items in the config
            for (String key : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

                // check if the player clicked the left mouse button
                if (event.isLeftClick()) {

                    ItemStack itemStack = Main.itemsShopMenu.get(key);

                    Integer maxIndex = 0;
                    Integer shopSize = CustomConfigs.get("shop").getInt("rows") * 9;

                    // CPP = current player page
                    Integer cpp = PaginatedMenu.playerPage.get(player.getUniqueId());

                    // get the max index
                    for (String temp : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {
                        maxIndex++;
                    }

                    // search for the clicked item in the config
                    if (event.getCurrentItem().isSimilar(itemStack)) {

                        // check if the clicked item is in the shop
                        if (event.getRawSlot() <= (event.getInventory().getSize() - 1)) {
                            Main.currentPlayerKey.put(player.getUniqueId(), key);
                            player.openInventory(InventoryHelper.setupBuyInventory(player, key));
                            break;
                        }
                    }

                    // create the next page of the shop
                    for (String clickedButton: Main.getInstance().getConfig().getConfigurationSection("menu").getKeys(false)) {

                        if (clickedButton.equals("next-page")) {

                            if (maxIndex >= (cpp * shopSize)) {
                                ItemStack clickedItem = CreateItems.createItemStackfromDefaultConfig(clickedButton, ".available");

                                if (event.getCurrentItem().isSimilar(clickedItem)) {
                                    PaginatedMenu.playerPage.put(player.getUniqueId(), PaginatedMenu.playerPage.get(player.getUniqueId()) + 1);
                                    player.openInventory(PaginatedMenu.setupInventory(player));
                                    break;
                                }
                            }
                        }

                        // create the previous page of the shop
                        if (clickedButton.equals("previous-page")) {

                            if (cpp != 1) {
                                ItemStack clickedItem = CreateItems.createItemStackfromDefaultConfig(clickedButton, ".available");

                                if (event.getCurrentItem().isSimilar(clickedItem)) {
                                    PaginatedMenu.playerPage.put(player.getUniqueId(), PaginatedMenu.playerPage.get(player.getUniqueId()) - 1);
                                    player.openInventory(PaginatedMenu.setupInventory(player));
                                    break;
                                }
                            }
                        }

                        // close the shop
                        if (clickedButton.equals("close")) {

                            ItemStack clickedItem = CreateItems.createItemStackfromDefaultConfig(clickedButton, "");

                            if (event.getCurrentItem().isSimilar(clickedItem)) {
                                player.closeInventory();
                            }
                            break;
                        }
                    }
                }
            }
            event.setCancelled(true);
        } else if (event.getView().getTitle().equals(buyMenu)) {

            // check if the player clicked the left mouse button
            if (!event.isLeftClick()) {
                event.setCancelled(true);
            }

            // check if the clicked item is in the shop
            if (!(event.getRawSlot() <= (event.getInventory().getSize() - 1))) {
                event.setCancelled(true);
            }

            // loop through all items in the config
            for (String key : CustomConfigs.get("buyMenu").getConfigurationSection("items").getKeys(false)) {

                int clickedSlot = event.getSlot();
                int configSlot = CustomConfigs.get("buyMenu").getInt("items." + key + ".slot") - 1;

                // check if the slot a player clicked on is the slot of an item in the config
                if (clickedSlot == configSlot) {

                    Integer calculateAmount = CustomConfigs.get("buyMenu").getInt("items." + key + ".itemData.amount");

                    double itemPrice = 0.0;

                    ItemStack itemToBuy = event.getInventory().getItem(CustomConfigs.get("buyMenu").getInt("clickedItemSlot"));
                    ItemStack purchasedItem = itemToBuy.clone();
                    ItemMeta purchasedItemMeta = purchasedItem.getItemMeta();
                    purchasedItem.setAmount(calculateAmount);

                    // the current player key
                    String purchasedItemKey = Main.currentPlayerKey.get(player.getUniqueId());

                    // modify the lore of the item, set the price
                    // check if the item Displayname is the same as the item in the config
                    if (itemToBuy.getItemMeta().getDisplayName().equals(ReplaceString.replaceString("shop", CustomConfigs.get("shop").getString("items." + purchasedItemKey + ".displayName"), purchasedItemKey))) {

                        itemPrice = CustomConfigs.get("shop").getDouble("items." + purchasedItemKey + ".price.buy");

                        purchasedItemMeta.setLore(null);

                        List<String> lore = CustomConfigs.get("shop").getStringList("items." + purchasedItemKey + ".lore");

                        for (int i = 0; i < lore.size(); i++) {
                            lore.set(i, ReplaceString.replaceString("shop", lore.get(i), purchasedItemKey));
                        }
                        purchasedItemMeta.setLore(lore);
                        purchasedItem.setItemMeta(purchasedItemMeta);
                    }

                    // check if...
                    // ...the player wants to buy an item
                    if (CustomConfigs.get("buyMenu").getString("items." + key + ".itemData.type").equals("BUY")) {
                        checkForBuy(player, purchasedItem, itemPrice, calculateAmount);
                    }
                    // ...the player wants to sell an item
                    if (CustomConfigs.get("buyMenu").getString("items." + key + ".itemData.type").equals("SELL")) {
                        checkForSell(player, key, purchasedItem, itemPrice, calculateAmount);
                    }
                    // ...the player wants to go back the inventory
                    if (CustomConfigs.get("buyMenu").getString("items." + key  + ".itemData.type").equals("CLOSE")) {
                        checkForClose(player);
                    }
                    // ...the player wants to exit the item
                    if (CustomConfigs.get("buyMenu").getString("items." + key  + ".itemData.type").equals("EXIT")) {
                        exitInventory(player);
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    private static void checkForSell(Player player, String key, ItemStack purchasedItem, Double itemPrice, Integer calculateAmount) {

        // check if the player has enough of the item to sell
        int buyAmount = CustomConfigs.get("buyMenu").getInt("items." + key + ".itemData.amount");

        if (Main.getItemAmount(player, purchasedItem) >= buyAmount) {

            player.getInventory().removeItem(purchasedItem);

            Main.econ.depositPlayer(player, (itemPrice * calculateAmount));

            player.sendMessage("§aYour new balance is: " + Main.econ.getBalance(player));

        }else {
            player.sendMessage("§cYou don't have enough of this item to sell.");
        }
    }

    private static void checkForBuy(Player player, ItemStack purchasedItem, Double itemPrice, Integer calculateAmount) {

        // check if the player has enough money
        if (Main.econ.getBalance(player) >= (itemPrice * calculateAmount)) {

            // check if the player has enough space in their inventory
            if (!InventoryHelper.canAddItem(player, purchasedItem)) {
                player.sendMessage("§cYou don't have enough space in your inventory to buy this item.");
            }

            player.getInventory().addItem(purchasedItem);

            Main.econ.withdrawPlayer(player, (itemPrice * calculateAmount));
        } else {
            player.sendMessage("§cYou don't have enough money to buy this item. (You need " + (itemPrice * calculateAmount) + ")");
        }

    }

    private static void checkForClose(Player player) {
        player.openInventory(PaginatedMenu.setupInventory(player));
    }

    private static void exitInventory(Player player) {
        player.closeInventory();
    }

}
