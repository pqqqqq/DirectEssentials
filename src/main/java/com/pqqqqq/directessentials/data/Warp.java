package com.pqqqqq.directessentials.data;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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

            String world = warp.getNode("world").getString();
            double x = warp.getNode("x").getDouble();
            double y = warp.getNode("y").getDouble();
            double z = warp.getNode("z").getDouble();

            Optional<World> worldObj = DirectEssentials.plugin.getGame().getServer().getWorld(world);
            if (worldObj.isPresent()) {
                warpObj.setLocation(new Location(worldObj.get(), new Vector3d(x, y, z)));
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

    public boolean isPermissible(Subject subject) {
        return subject.hasPermission("directessentials.warp." + name) || subject.hasPermission("directessentials.warp.*") || subject.hasPermission("directessentials.*");
    }

    public void init(Object... args) {
        this.name = (String) args[0];
    }

    public void save(ConfigurationNode node) {
        // TODO: Commenting??
        if (location != null) {
            ConfigurationNode warps = node.getNode("warps");
            ConfigurationNode thisWarp = warps.getNode(name);

            thisWarp.getNode("world").setValue(((World) location.getExtent()).getName());
            thisWarp.getNode("x").setValue(location.getX());
            thisWarp.getNode("y").setValue(location.getY());
            thisWarp.getNode("z").setValue(location.getZ());
        }
    }
}
