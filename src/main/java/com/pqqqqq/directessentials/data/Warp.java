package com.pqqqqq.directessentials.data;

import com.pqqqqq.directessentials.util.SaveUtils;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2015-05-12.
 */
public class Warp implements IWeakValue, ISaveable {
    private String name;
    private Location location = null;

    public static Map<String, Warp> loadWarps(ConfigurationNode node) {
        Map<String, Warp> warps = new HashMap<String, Warp>();

        ConfigurationNode warpsNode = node.getNode("warps");
        for (ConfigurationNode warp : warpsNode.getChildrenMap().values()) {
            Warp warpObj = new Warp(warp.getKey().toString());
            Location location = SaveUtils.loadLocation(warp);

            if (location != null) {
                warpObj.setLocation(location);
                warps.put(warp.getKey().toString(), warpObj);
            }
        }

        return warps;
    }

    public Warp() {
    }

    public Warp(String name) {
        this.init(name);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean apply(Player player) {
        if (location == null) {
            return false;
        }

        player.setLocation(location);
        return true;
    }

    public void init(Object... args) {
        this.name = (String) args[0];
    }

    public void save(ConfigurationNode node) {
        // TODO: Commenting??
        if (location != null) {
            ConfigurationNode warps = node.getNode("warps");
            ConfigurationNode thisWarp = warps.getNode(name);

            SaveUtils.saveLocation(this.location, thisWarp);
        }
    }
}
