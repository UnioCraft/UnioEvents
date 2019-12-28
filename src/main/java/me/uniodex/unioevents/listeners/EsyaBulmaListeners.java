package me.uniodex.unioevents.listeners;

import me.uniodex.unioevents.UnioEvents;
import me.uniodex.unioevents.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EsyaBulmaListeners implements Listener {

    private UnioEvents plugin;

    public EsyaBulmaListeners(UnioEvents plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getEventManager().getEsyaBulmaOyuncular().contains(player)) {
            return;
        }
        if (!Utils.isLocationInArea(event.getTo(), "esyabulma")) {
            player.sendMessage(plugin.getMessage("esyabulma.noTeleport"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!plugin.getEventManager().getEsyaBulmaStatus()) {
            return;
        }
        if (!Utils.isLocationInArea(event.getClickedBlock().getLocation(), "esyabulma")) {
            return;
        }
        if (!event.getClickedBlock().getType().equals(Material.CHEST)) {
            return;
        }
        if (!plugin.getEventManager().getEsyaBulmaOyuncular().contains(player)) {
            return;
        }

        plugin.getEventManager().destroyChest(event.getClickedBlock().getLocation());
        plugin.getEventManager().getWinnerChests().remove(Utils.getStringLocation(event.getClickedBlock().getLocation()));
        event.getClickedBlock().getLocation().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.EXPLOSION_LARGE, 0, 3);

        if (plugin.getConfig().getStringList("esyabulma.chests").contains(Utils.getStringLocation(event.getClickedBlock().getLocation()))) {
            plugin.getEventManager().addWinner(player.getName(), "esyabulma");
            player.sendMessage(plugin.getMessage("esyabulma.wonMsg"));
            Bukkit.broadcastMessage(plugin.getMessage("esyabulma.won").replaceAll("%player%", player.getName()).replaceAll("%remaining%", String.valueOf((15 - plugin.getEventManager().getEsyaBulmaKazananlar().size()))));
        } else {
            player.sendMessage(plugin.getMessage("esyabulma.noAward"));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (plugin.getEventManager().getEsyaBulmaOyuncular().contains(event.getPlayer())) {
            plugin.getEventManager().leaveEsyaBulma(event.getPlayer());
        }
    }


}
