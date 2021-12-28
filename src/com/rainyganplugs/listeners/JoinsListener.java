package com.rainyganplugs.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.rainyganplugs.Main.plugin;

public class JoinsListener implements Listener {

    String config = plugin.getConfig().getString("Messages.Welcome");

    @EventHandler
    public void joinListen(PlayerJoinEvent event) {
        /*
        * CUSTOM MESSAGE FOR PLAYERS
        * */
        Player player = event.getPlayer();
        player.sendMessage(colorCode(PlaceholderAPI.setPlaceholders(player, config)));

    }

    private String colorCode(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
