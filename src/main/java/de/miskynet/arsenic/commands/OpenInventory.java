package de.miskynet.arsenic.commands;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.PaginatedMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OpenInventory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!commandSender.hasPermission("arsenic.shop")) {
            commandSender.sendMessage(Main.getInstance().getConfig().getString("messages.no-permission-command"));
            return true;
        }

        // Check if the command sender is a player
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Main.getInstance().getConfig().getString("messages.no-player"));
            return true;
        }

        Player player = (Player) commandSender;

        // set the player page to one
        PaginatedMenu.playerPage.put(player.getUniqueId(), 1);

        // Get the title and rows from the config
        Inventory inventory = PaginatedMenu.setupInventory(player);

        player.openInventory(inventory);

        return true;
    }
}
