package de.miskynet.arsenic.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ArsenicTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!commandSender.hasPermission("arsenic.shop")) {
            commandSender.sendMessage("§cSorry, but you are not allowed to use this command!");
            return null;
        }

        ArrayList<String> completer = new ArrayList<>();

        // tabcompleter for the /arsenic command
        if (strings.length == 1) {
            if ("reload".startsWith(strings[0])) {
                completer.add("reload");
            }

            if ("credits".startsWith(strings[0])) {
                completer.add("credits");
            }

        }

        return completer;
    }
}
