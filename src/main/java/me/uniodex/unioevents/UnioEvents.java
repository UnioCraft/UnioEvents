package me.uniodex.unioevents;

import lombok.Getter;
import me.uniodex.unioevents.commands.CommandEsyaBulma;
import me.uniodex.unioevents.commands.CommandParkur;
import me.uniodex.unioevents.listeners.EsyaBulmaListeners;
import me.uniodex.unioevents.listeners.ParkurListeners;
import me.uniodex.unioevents.managers.ConfigManager;
import me.uniodex.unioevents.managers.ConfigManager.Config;
import me.uniodex.unioevents.managers.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class UnioEvents extends JavaPlugin {

    public static String hataPrefix = ChatColor.AQUA + "" + ChatColor.BOLD + "UNIOCRAFT " + ChatColor.DARK_GREEN + "->" + ChatColor.RED + " ";
    public static String dikkatPrefix = ChatColor.AQUA + "" + ChatColor.BOLD + "UNIOCRAFT " + ChatColor.DARK_GREEN + "->" + ChatColor.GOLD + " ";
    public static String bilgiPrefix = ChatColor.AQUA + "" + ChatColor.BOLD + "UNIOCRAFT " + ChatColor.DARK_GREEN + "->" + ChatColor.GREEN + " ";
    public static String consolePrefix = "[UnioEvents] ";

    @Getter
    private ConfigManager configManager;
    @Getter
    private EventManager eventManager;

    public void onEnable() {
        configManager = new ConfigManager(this);
        eventManager = new EventManager(this);

        new EsyaBulmaListeners(this);
        new ParkurListeners(this);

        new CommandEsyaBulma(this);
        new CommandParkur(this);

        hataPrefix = getMessage("prefix.hataPrefix");
        dikkatPrefix = getMessage("prefix.dikkatPrefix");
        bilgiPrefix = getMessage("prefix.bilgiPrefix");
        consolePrefix = getMessage("prefix.consolePrefix");
    }

    public void onDisable() {
        for (Player player : new ArrayList<>(eventManager.getParkurOyuncular())) {
            eventManager.leaveParkur(player);
        }

        for (Player player : new ArrayList<>(eventManager.getEsyaBulmaOyuncular())) {
            eventManager.leaveEsyaBulma(player);
        }
    }

    public String getMessage(String configSection) {
        FileConfiguration config = configManager.getConfig(Config.LANG);
        if (config.getString(configSection) == null) return null;

        return ChatColor.translateAlternateColorCodes('&', config.getString(configSection).replaceAll("%hataPrefix%", hataPrefix).replaceAll("%bilgiPrefix%", bilgiPrefix).replaceAll("%dikkatPrefix%", dikkatPrefix).replaceAll("%prefix%", bilgiPrefix));
    }

    public List<String> getMessages(String configSection) {
        FileConfiguration config = configManager.getConfig(Config.LANG);
        if (config.getString(configSection) == null) return null;

        List<String> newList = new ArrayList<>();
        for (String msg : config.getStringList(configSection)) {
            newList.add(ChatColor.translateAlternateColorCodes('&', msg.replaceAll("%hataPrefix%", hataPrefix).replaceAll("%bilgiPrefix%", bilgiPrefix).replaceAll("%dikkatPrefix%", dikkatPrefix).replaceAll("%prefix%", bilgiPrefix)));
        }
        return newList;
    }

}
