package com.rainyganplugs.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.rainyganplugs.Main.plugin;

public class RECommands implements CommandExecutor {

    String noPerms = plugin.getConfig().getString("Messages.NotEnoughPermissions");
    String reloadConfig = plugin.getConfig().getString("Messages.ReloadConfig");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("re")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reloadconfig")) {
                    if (sender.hasPermission("re.reloadconfig")) {
                        plugin.saveDefaultConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(colorCode(PlaceholderAPI.setPlaceholders((Player) sender, reloadConfig)));
                        return true;
                    }
                }
                sender.sendMessage(colorCode(noPerms));
                return true;
            }
            sender.sendMessage(colorCode("&7[&c-----------[RE1.8.8]-----------&7]"));
            sender.sendMessage(colorCode("&c/re reloadconfig"));
            sender.sendMessage(colorCode("&7[&c-----------------------------&7]"));
            return true;
        }
        sender.sendMessage(colorCode(noPerms));
        return true;
    }


    private String colorCode(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
