package de.miskynet.arsenic.commands;

import de.miskynet.arsenic.utils.CustomConfigs;
import de.miskynet.arsenic.utils.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class openInventory implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        Inventory inventory = Bukkit.createInventory(null, 27, CustomConfigs.get("shop").getString("title"));

        CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false).forEach(key -> {

            inventory.addItem(InventoryHelper.createItemStack(key));
        });

        player.openInventory(inventory);

        return true;
    }
}
