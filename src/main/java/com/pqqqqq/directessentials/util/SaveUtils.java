package com.pqqqqq.directessentials.util;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Kevin on 2015-05-13.
 */
public class SaveUtils {

    // Location
    public static Location loadLocation(ConfigurationNode node) {
        Vector3d vec = loadVector(node);

        Optional<World> worldObj = loadWorld(node);
        if (worldObj.isPresent()) {
            return new Location(worldObj.get(), vec);
        }
        return null;
    }

    public static void saveLocation(Location location, ConfigurationNode node) {
        if (location != null) {
            saveWorld((World) location.getExtent(), node);
            saveVector(location.getPosition(), node);
        }
    }

    public static Vector3d loadVector(ConfigurationNode node) {
        double x = node.getNode("x").getDouble();
        double y = node.getNode("y").getDouble();
        double z = node.getNode("z").getDouble();
        return new Vector3d(x, y, z);
    }

    public static void saveVector(Vector3d vec, ConfigurationNode node) {
        if (vec != null) {
            node.getNode("x").setValue(vec.getX());
            node.getNode("y").setValue(vec.getY());
            node.getNode("z").setValue(vec.getZ());
        }
    }

    public static Optional<World> loadWorld(ConfigurationNode node) {
        return DirectEssentials.plugin.getGame().getServer().getWorld(node.getNode("world").getString());
    }

    public static void saveWorld(World world, ConfigurationNode node) {
        if (world != null) {
            node.getNode("world").setValue(world.getName());
        }
    }

    public static List<String> loadStringList(ConfigurationNode node) {
        return node.getList(new Function<Object, String>() {
            @Nullable
            public String apply(@Nullable Object input) {
                return input == null ? null : input.toString();
            }
        });
    }

    public static void saveStringList(List<String> set, ConfigurationNode node) {
        if (set != null) {
            node.setValue(set);
        }
    }
}
