package de.miskynet.arsenic.commands;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfigs;
import de.miskynet.arsenic.utils.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OpenInventory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Check if the command sender is a player
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        // Get the title and rows from the config
        Inventory inventory = InventoryHelper.createInventory("shop");

        Integer addedItems = 0;

        // Loop through all items in the config
        for (String key : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

            // Check if there is enough space in the inventory
            if (addedItems >= inventory.getSize()) {
                break;
            }

            ItemStack itemStack = InventoryHelper.createItemStackFromConfig("shop", key, true);

            // Check if the slot is set in the config
            int slot = CustomConfigs.get("shop").getInt("items." + key + ".slot");

            if (slot != 0) {
                inventory.setItem(slot - 1, itemStack);
            } else {
                inventory.addItem(itemStack);
            }
            addedItems++;
        }

        player.openInventory(inventory);

        return true;
    }
}
