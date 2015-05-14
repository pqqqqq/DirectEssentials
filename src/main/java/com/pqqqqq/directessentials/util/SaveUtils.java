package com.pqqqqq.directessentials.util;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by Kevin on 2015-05-13.
 */
public class SaveUtils {

    // Location
    public static Location loadLocation(ConfigurationNode node) {
        String world = node.getNode("world").getString();
        double x = node.getNode("x").getDouble();
        double y = node.getNode("y").getDouble();
        double z = node.getNode("z").getDouble();

        Optional<World> worldObj = DirectEssentials.plugin.getGame().getServer().getWorld(world);
        if (worldObj.isPresent()) {
            return new Location(worldObj.get(), new Vector3d(x, y, z));
        }

        return null;
    }

    public static void saveLocation(Location location, ConfigurationNode node) {
        if (location != null) {
            node.getNode("world").setValue(((World) location.getExtent()).getName());
            node.getNode("x").setValue(location.getX());
            node.getNode("y").setValue(location.getY());
            node.getNode("z").setValue(location.getZ());
        }
    }
}
