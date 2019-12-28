package me.uniodex.unioevents.commands;

import me.uniodex.unioevents.UnioEvents;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandEsyaBulma implements CommandExecutor {

    private UnioEvents plugin;

    public CommandEsyaBulma(UnioEvents plugin) {
        this.plugin = plugin;
        plugin.getCommand("esyabulma").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (!plugin.getEventManager().getEsyaBulmaStatus()) {
                    player.sendMessage(plugin.getMessage("esyabulma.notActive"));
                    return true;
                }
                if (plugin.getEventManager().getEsyaBulmaOyuncular().contains(player)) {
                    player.sendMessage(plugin.getMessage("esyabulma.alreadyJoined"));
                } else {
                    plugin.getEventManager().joinEsyaBulma(player);
                    player.sendMessage(plugin.getMessage("esyabulma.joined"));
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("ayrıl")) {
                    if (!plugin.getEventManager().getEsyaBulmaOyuncular().contains(player)) {
                        player.sendMessage(plugin.getMessage("esyabulma.notJoined"));
                        return true;
                    }
                    plugin.getEventManager().leaveEsyaBulma(player);
                    sender.sendMessage(plugin.getMessage("esyabulma.left"));
                }
            }
        }

        if (sender.hasPermission("unioevents.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("kazananlar")) {
                    sender.sendMessage(plugin.getEventManager().getEsyaBulmaKazananlar().toString());
                }
                if (args[0].equalsIgnoreCase("toggle")) {
                    for (Player otherPlayer : new ArrayList<>(plugin.getEventManager().getEsyaBulmaOyuncular())) {
                        plugin.getEventManager().leaveEsyaBulma(otherPlayer);
                    }
                    plugin.getEventManager().setEsyaBulmaStatus(!plugin.getEventManager().getEsyaBulmaStatus());
                    sender.sendMessage(plugin.getMessage("esyabulma.toggle").replaceAll("%status%", String.valueOf(plugin.getEventManager().getEsyaBulmaStatus())));
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    plugin.getEventManager().addWinner(args[1], "esyabulma");
                    sender.sendMessage(plugin.getMessage("esyabulma.winnerAdded"));
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    plugin.getEventManager().removeWinner(args[1], "esyabulma");
                    sender.sendMessage(plugin.getMessage("esyabulma.winnerRemoved"));
                }
            }
        }
        return true;
    }
}
