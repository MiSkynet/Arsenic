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

public class openInventory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        String title = CustomConfigs.get("shop").getString("title") != null ? CustomConfigs.get("shop").getString("title") : Main.missingString;
        Integer rows = (CustomConfigs.get("shop").getInt("rows") != 0) && (CustomConfigs.get("shop").getInt("rows") >= 1) && (CustomConfigs.get("shop").getInt("rows") <= 6) ? CustomConfigs.get("shop").getInt("rows") * 9 : 9;

        Inventory inventory = Bukkit.createInventory(null, rows, title);

        Integer addedItems = 0;

        for (String key : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

            if (addedItems < inventory.getSize()) {
                ItemStack itemStack = InventoryHelper.createItemStack(key);

                if (CustomConfigs.get("shop").getInt("items." + key + ".slot") != 0) {
                    inventory.setItem(CustomConfigs.get("shop").getInt("items." + key + ".slot") - 1, itemStack);
                } else {
                    inventory.addItem(itemStack);
                }
                addedItems++;
            }else {
                break;
            }
        }

        player.openInventory(inventory);

        return true;
    }
}
