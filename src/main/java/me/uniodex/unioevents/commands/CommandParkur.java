package me.uniodex.unioevents.commands;

import me.uniodex.unioevents.UnioEvents;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandParkur implements CommandExecutor {

    private UnioEvents plugin;

    public CommandParkur(UnioEvents plugin) {
        this.plugin = plugin;
        plugin.getCommand("parkur").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (!plugin.getEventManager().getParkurStatus()) {
                    player.sendMessage(plugin.getMessage("parkur.notActive"));
                    return true;
                }
                if (plugin.getEventManager().getParkurOyuncular().contains(player)) {
                    player.sendMessage(plugin.getMessage("parkur.alreadyJoined"));
                } else {
                    plugin.getEventManager().joinParkur(player);
                    player.sendMessage(plugin.getMessage("parkur.joined"));
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("ayrıl")) {
                    if (!plugin.getEventManager().getParkurOyuncular().contains(player)) {
                        player.sendMessage(plugin.getMessage("parkur.notJoined"));
                        return true;
                    }
                    plugin.getEventManager().leaveParkur(player);
                    sender.sendMessage(plugin.getMessage("parkur.left"));
                }
                if (args[0].equalsIgnoreCase("başadön") || args[0].equalsIgnoreCase("basadon")) {
                    if (plugin.getEventManager().getParkurOyuncular().contains(player)) {
                        player.teleport(plugin.getEventManager().getParkurLocation());
                    }else {
                        player.sendMessage(plugin.getMessage("parkur.haveToJoin"));
                        return true;
                    }

                }
            }
        }

        if (sender.hasPermission("unioevents.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("kazananlar")) {
                    sender.sendMessage(plugin.getEventManager().getParkurKazananlar().toString());
                }
                if (args[0].equalsIgnoreCase("toggle")) {
                    for (Player otherPlayer : new ArrayList<>(plugin.getEventManager().getParkurOyuncular())) {
                        plugin.getEventManager().leaveParkur(otherPlayer);
                    }
                    plugin.getEventManager().setParkurStatus(!plugin.getEventManager().getParkurStatus());
                    sender.sendMessage(plugin.getMessage("parkur.toggle").replaceAll("%status%", String.valueOf(plugin.getEventManager().getParkurStatus())));
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    plugin.getEventManager().addWinner(args[1], "parkur");
                    sender.sendMessage(plugin.getMessage("parkur.winnerAdded"));
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    plugin.getEventManager().removeWinner(args[1], "parkur");
                    sender.sendMessage(plugin.getMessage("parkur.winnerRemoved"));
                }
            }
        }
        return true;
    }
}
