package com.rainyganplugs;

import com.rainyganplugs.commands.RECommands;
import com.rainyganplugs.commands.ReportCommand;
import com.rainyganplugs.listeners.ChatListener;
import com.rainyganplugs.listeners.JoinsListener;
import com.rainyganplugs.tabcompleters.RETabComplete;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {
    //MAIN
    public static Main plugin;

    //HASHMAP
    public static HashMap<String, Long> chatCooldown = new HashMap<>();
    public static HashMap<String, Long> reportCooldown = new HashMap<>();

    //CUSTOM CONFIGURATION
    private File playerReports;
    private FileConfiguration playerReportsConfig;

    //LOGGER
    public static final Logger log = Logger.getLogger("Minecraft");

    //VAULTS IMPLEMENTATIONS
    public static Economy econ = null;
    public static Permission perms = null;

    //EVENTS
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new JoinsListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    //COMMANDS
    public void registerCommands() {
        getCommand("re").setExecutor(new RECommands());
        getCommand("report").setExecutor(new ReportCommand());
    }


    //TAB COMPLETER
    public void registerTabCompleter() {
        getCommand("re").setTabCompleter(new RETabComplete());
    }


    //ON ENABLE
    @Override
    public void onEnable() {
        //MAIN CALLER
        plugin = this;

        createReportsFile();
        playerReportsConfig = YamlConfiguration.loadConfiguration(playerReports);

        //LOGGER
        Bukkit.getConsoleSender().sendMessage(colorCode("&a&l&oOOOO HALLO!"));

        //DEFAULT CONFIG
        saveDefaultConfig();

        //REGISTERS STUFF
        registerEvents();
        registerCommands();
        registerTabCompleter();

        //VAULTS SETUPS
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
            *
            * IF PAPI IS INSTALLED DO THESE THINGS
            *
            * */
            log.info(colorCode("PAPI IS INSTALLED :)"));

        } else {
            /*
            *
            * TO MAKE SURE THAT PLACEHOLDER API IS INSTALLED ON PLUGINS FOLDER
            *
            * */
            log.warning(colorCode("WARNING PLUGIN PLACEHODERAPI DOES NOT EXIST DISABLING PLUGIN"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    /*
    * REPORT FILE CREATION
    * */
    public FileConfiguration getPlayerReportsConfig() {
        return this.playerReportsConfig;
    }

    public void createReportsFile() {
        playerReports = new File(getDataFolder(), "report.yml");
        if (!(playerReports.exists())) {
            playerReports.getParentFile().mkdirs();
            saveResource("report.yml", false);
        }
    }

    public void saveReportsConfig() {
        if (playerReports == null || playerReportsConfig == null) {
            return;
        }
        try {
            getPlayerReportsConfig().save(playerReports);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + playerReports, ex);
        }
    }

    public void reloadReports() {
        if (playerReports == null) {
            playerReports = new File(getDataFolder(), "report.yml");
        }
        playerReportsConfig = YamlConfiguration.loadConfiguration(playerReports);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(this.getResource("report.yml"), StandardCharsets.UTF_8);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            playerReportsConfig.setDefaults(defConfig);
        }
    }

    //ON DISABLE
    @Override
    public void onDisable() {
        //LOGGERS
        Bukkit.getConsoleSender().sendMessage(colorCode("&a&l&o;-; SAYONARAAAAA"));
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }












    //VAULTS ECO AND PERMS
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    //COLOR CODE FOR ALL LOOOOOL
    private String colorCode(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
