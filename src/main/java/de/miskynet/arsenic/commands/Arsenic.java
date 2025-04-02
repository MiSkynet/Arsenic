package de.miskynet.arsenic.commands;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfigs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Arsenic implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!commandSender.hasPermission("arsenic.arsenic")) {
            commandSender.sendMessage(Main.getInstance().getConfig().getString("messages.no-permission-command"));
            return true;
        }

        // code is kinda obvious
        if (strings.length > 0) {

            switch (strings[0]){
                case "reload":
                    CustomConfigs.reload("shop");
                    CustomConfigs.reload("buyMenu");
                    commandSender.sendMessage(Main.getInstance().getConfig().getString("messages.plugin-reloaded"));
                    break;

                case "credits":
                    commandSender.sendMessage("§8+--------------------------------------+");
                    commandSender.sendMessage("");
                    commandSender.sendMessage("§7ᴀʀѕᴇɴɪᴄ ᴡᴀѕ ᴅᴇᴠᴇʟᴏᴘᴇᴅ ʙʏ §x§F§F§1§C§5§4ᴍɪѕᴋʏɴᴇᴛ§7.");
                    commandSender.sendMessage("§7ʏᴏᴜ ᴄᴀɴ ꜰɪɴᴅ ᴛʜᴇ ᴘʀᴏᴊᴇᴄᴛ ᴏɴ ᴍʏ §x§1§9§1§9§1§9ɢɪᴛʜᴜʙ§7: ");
                    commandSender.sendMessage("§7• §x§F§F§1§C§5§4https://github.com/MiSkynet");
                    commandSender.sendMessage("");
                    commandSender.sendMessage("§8+--------------------------------------+");
                    break;
            }
        }

        return true;
    }
}
