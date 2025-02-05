package de.miskynet.arsenic.commands;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfig;
import de.miskynet.arsenic.utils.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class openInventory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Check if the command sender is a player
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cYou must be a player to use this command!");
            return false;
        }

        // Cast the command sender to a player
        Player player = (Player) commandSender;

        // Set the title and size of the inventory
        Main.getInstance().setTitle();
        Main.getInstance().setSize();

        // Open the inventory
        player.openInventory(InventoryManager.addItems(Bukkit.createInventory(null, Main.getInstance().getSize(), Main.getInstance().getTitle())));

        return true;
    }

}
