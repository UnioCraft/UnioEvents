package me.uniodex.unioevents.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String getStringLocation(final Location l) {
        if (l == null) {
            return "";
        }
        return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
    }

    public static Location getLocationString(final String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }

    public static Location getLocationStringWithoutChunkLoad(final String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            if (w == null) {
                return null;
            }
            if (w.isChunkLoaded(x >> 4, z >> 4)) {
                return new Location(w, x, y, z);
            }
        }
        return null;
    }

    public static boolean isLocationInArea(Location location, String area) {
        List<String> regionIds = new ArrayList<>();
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(location.getWorld());
        ApplicableRegionSet regionsAtLocation = regionManager.getApplicableRegions(location);

        for (ProtectedRegion region : regionsAtLocation) {
            regionIds.add(region.getId());
        }

        return regionIds.contains(area);
    }
}
