package me.uniodex.unioevents.listeners;

import me.uniodex.unioevents.UnioEvents;
import me.uniodex.unioevents.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ParkurListeners implements Listener {

    private UnioEvents plugin;

    public ParkurListeners(UnioEvents plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getEventManager().getParkurOyuncular().contains(player)) {
            return;
        }
        if (!Utils.isLocationInArea(event.getTo(), "parkur")) {
            player.sendMessage(plugin.getMessage("parkur.noTeleport"));
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().hasPermission("parkur.cantwin")) {
            return;
        }
        if (!plugin.getEventManager().getParkurStatus()) {
            return;
        }
        if (!plugin.getEventManager().getParkurOyuncular().contains(event.getPlayer())) {
            return;
        }
        if (plugin.getEventManager().getParkurKazananlar().contains(event.getPlayer().getName())) {
            return;
        }
        if (plugin.getEventManager().getParkurKazananlar().size() < 9) {
            if (Utils.isLocationInArea(event.getTo(), "parkurwin")) {
                plugin.getEventManager().addWinner(event.getPlayer().getName(), "parkur");
                Bukkit.broadcastMessage(plugin.getMessage("parkur.won").replaceAll("%player%", event.getPlayer().getName()).replaceAll("%order%", String.valueOf(plugin.getEventManager().getParkurKazananlar().size())));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (plugin.getEventManager().getParkurOyuncular().contains(event.getPlayer())) {
            plugin.getEventManager().leaveParkur(event.getPlayer());
        }
    }
}
