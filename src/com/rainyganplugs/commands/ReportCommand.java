package com.rainyganplugs.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.rainyganplugs.Main.*;

public class ReportCommand implements CommandExecutor {

    /*
    * STRINGS FROM CONFIG
    * */
    String consoleWarn = plugin.getConfig().getString("Messages.ConsoleNotAllowed");
    String playerWarn = plugin.getConfig().getString("Messages.PlayerIsOfflineOrDoesNotExist");


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        /*
        * REPORT COOLDOWN
        * */
        int reportCooldownTime = plugin.getConfig().getInt("Cooldowns.Report");

        /*
        * IF SENDER IS NOT A PLAYER
        * */
        if (!(sender instanceof Player)) {
            sender.sendMessage(colorCode(consoleWarn));
            return true;
        }
        Player player = (Player) sender;
        if (plugin.getConfig().getBoolean("ReportCooldown")) {
            if (!(player.hasPermission("re.report.cooldownbypass"))) {
                if (reportCooldown.containsKey(player.getName())) {
                    long secondsLeft = ((reportCooldown.get(player.getName()) / 1000) + reportCooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0) {
                        player.sendMessage(colorCode(plugin.getConfig().getString("Messages.Cooldown").replace("%cooldown_timeleft%", Long.toString(secondsLeft))));
                        return true;
                    }
                }
            }
        }
        if (player.hasPermission("re.report")) {
            if (args.length <= 1) {
                player.sendMessage(colorCode("&cCorrect usage > /report <player> <reason>"));
                return true;
            }
            if (args.length >= 2) {
                Player reportedPlayer = Bukkit.getPlayer(args[0]);
                if (reportedPlayer.getName().equals(player.getName())) {
                    player.sendMessage(colorCode(plugin.getConfig().getString("Messages.CantReportYourSelf")));
                    return true;
                }
                if (!Bukkit.getOnlinePlayers().contains(reportedPlayer)) {
                    if (!(player.hasPermission("re.report.cooldownbypass"))) {
                        if (reportCooldown.containsKey(player.getName())) {
                            long secondsLeft = ((reportCooldown.get(player.getName()) / 1000) + reportCooldownTime) - (System.currentTimeMillis() / 1000);
                            if (secondsLeft > 0) {
                                player.sendMessage(colorCode(plugin.getConfig().getString("Messages.Cooldown").replace("%cooldown_timeleft%", Long.toString(secondsLeft))));
                                return true;
                            }
                        }
                    }
                    player.sendMessage(colorCode(playerWarn));
                    return true;
                }

                reportCooldown.put(player.getName(), System.currentTimeMillis());

                StringBuilder reason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reason.append(args[i]);
                    if (i < args.length) {
                        reason.append(" ");
                    }
                }

                plugin.getPlayerReportsConfig().createSection("Reports." + reportedPlayer.getName() + ".Reports." + player.getName());
                plugin.getPlayerReportsConfig().set("Reports." + reportedPlayer.getName() + ".Reports." + player.getName(), reason.toString());
                plugin.saveReportsConfig();

                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage(colorCode(plugin.getConfig().getString("Messages.ReportMessageForAll").replace("%reporter%", player.getName()).replace("%reported%", reportedPlayer.getName()).replace("%reason%", reason)));
                    return true;
                }
            }
        }
        player.sendMessage(colorCode(plugin.getConfig().getString("Messages.NotEnoughPermissions")));
        return true;
    }

    /*
    * CHAT COLOR
    * */
    private String colorCode(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
