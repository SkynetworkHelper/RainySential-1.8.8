package com.rainyganplugs.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class RETabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabComplete = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("re")) {
            if (args.length == 1) {
                if (sender.hasPermission("re.reloadconfig")) {
                    tabComplete.add("reloadconfig");
                }
                return tabComplete;
            }
        }
        return tabComplete;
    }

}
