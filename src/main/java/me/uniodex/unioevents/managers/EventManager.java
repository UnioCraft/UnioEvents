package me.uniodex.unioevents.managers;

import lombok.Getter;
import lombok.Setter;
import me.uniodex.unioevents.UnioEvents;
import me.uniodex.unioevents.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private UnioEvents plugin;

    @Getter
    @Setter
    private Location esyaBulmaLocation;
    @Getter
    @Setter
    private Location parkurLocation;
    @Getter
    private List<String> esyaBulmaKazananlar = new ArrayList<>();
    @Getter
    private List<String> parkurKazananlar = new ArrayList<>();
    @Getter
    private List<Player> esyaBulmaOyuncular = new ArrayList<>();
    @Getter
    private List<Player> parkurOyuncular = new ArrayList<>();
    @Getter
    @Setter
    private Boolean esyaBulmaStatus = false;
    @Getter
    @Setter
    private Boolean parkurStatus = false;
    @Getter
    @Setter
    private List<String> winnerChests = new ArrayList<>();

    public EventManager(UnioEvents plugin) {
        this.plugin = plugin;

        esyaBulmaLocation = Utils.getLocationString(plugin.getConfig().getString("esyabulma.location"));
        parkurLocation = Utils.getLocationString(plugin.getConfig().getString("parkur.location"));
    }

    public void addWinner(String player, String type) {
        if (type.equalsIgnoreCase("esyabulma")) {
            esyaBulmaKazananlar.add(player);
        } else if (type.equalsIgnoreCase("parkur")) {
            for (Player otherPlayer : new ArrayList<>(plugin.getEventManager().getParkurOyuncular())) {
                Bukkit.getPlayer(player).showPlayer(otherPlayer);
            }
            parkurKazananlar.add(player);
        }
    }

    public void removeWinner(String player, String type) {
        if (type.equalsIgnoreCase("esyabulma")) {
            esyaBulmaKazananlar.remove(player);
        } else if (type.equalsIgnoreCase("parkur")) {
            parkurKazananlar.remove(player);
        }
    }

    public void joinParkur(Player player) {
        for (Player otherPlayer : new ArrayList<>(plugin.getEventManager().getParkurOyuncular())) {
            player.hidePlayer(otherPlayer);
            otherPlayer.hidePlayer(player);
        }
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "efly " + player.getName() + " off");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tpauto " + player.getName() + " off");
        player.teleport(plugin.getEventManager().getParkurLocation());
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 86400, 1));
        parkurOyuncular.add(player);
    }

    public void joinEsyaBulma(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "efly " + player.getName() + " off");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tpauto " + player.getName() + " off");
        player.teleport(esyaBulmaLocation);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 86400, 1));
        esyaBulmaOyuncular.add(player);
    }

    public void leaveParkur(Player player) {
        for (Player otherPlayer : parkurOyuncular) {
            player.showPlayer(otherPlayer);
            otherPlayer.showPlayer(player);
        }
        parkurOyuncular.remove(player);
        player.performCommand("spawn");
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public void leaveEsyaBulma(Player player) {
        esyaBulmaOyuncular.remove(player);
        player.performCommand("spawn");
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public void destroyChest(Location location) {
        if (location.getBlock().getType().equals(Material.CHEST)) {
            for (ItemStack item : ((Chest) location.getBlock().getState()).getBlockInventory().getContents()) {
                ((Chest) location.getBlock().getState()).getBlockInventory().remove(item);
            }
            location.getBlock().setType(Material.AIR);
        }
    }
}
