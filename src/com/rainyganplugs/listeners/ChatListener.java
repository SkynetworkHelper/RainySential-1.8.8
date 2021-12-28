package com.rainyganplugs.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static com.rainyganplugs.Main.chatCooldown;
import static com.rainyganplugs.Main.plugin;

public class ChatListener implements Listener {
    /*
    * COOLDOWN FOR CHAT
    * */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        int cooldownTime = plugin.getConfig().getInt("Cooldowns.Chat");
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("ChatCooldown")) {
            if (!player.hasPermission("re.chatcooldown.bypass")) {
                if (chatCooldown.containsKey(player.getName())) {
                    long secondsLeft = ((chatCooldown.get(player.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0) {
                        player.sendMessage(chatColor(plugin.getConfig().getString("Messages.Cooldown").replace("%cooldown_timeleft%", Long.toString(secondsLeft))));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            chatCooldown.put(player.getName(), System.currentTimeMillis());
        }
    }





    /*
    * CHAT COLOR
    * */
    private String chatColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
