package com.pqqqqq.directessentials.data;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2015-05-12.
 */
public class Home implements IWeakValue, ISaveable {
    private String name;
    private String owner;
    private Location location = null;

    public static Map<String, Home> loadHomes(ConfigurationNode node, EssentialsUser user) {
        Map<String, Home> homes = new HashMap<String, Home>();

        ConfigurationNode homesNode = node.getNode("homes");
        for (ConfigurationNode home : homesNode.getChildrenMap().values()) {
            Home homeObj = new Home(home.getKey().toString(), user.getUuid());

            String world = home.getNode("world").getString();
            double x = home.getNode("x").getDouble();
            double y = home.getNode("y").getDouble();
            double z = home.getNode("z").getDouble();

            Optional<World> worldObj = DirectEssentials.plugin.getGame().getServer().getWorld(world);
            if (worldObj.isPresent()) {
                homeObj.setLocation(new Location(worldObj.get(), new Vector3d(x, y, z)));
                homes.put(home.getKey().toString(), homeObj);
            }
        }

        return homes;
    }

    public Home() {
    }

    public Home(String name, String owner) {
        this.init(name, owner);
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
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
        this.owner = (String) args[1];
    }

    public void save(ConfigurationNode node) {
        // TODO: Commenting??
        if (location != null) {
            ConfigurationNode warps = node.getNode("homes");
            ConfigurationNode thisWarp = warps.getNode(name);

            thisWarp.getNode("world").setValue(((World) location.getExtent()).getName());
            thisWarp.getNode("x").setValue(location.getX());
            thisWarp.getNode("y").setValue(location.getY());
            thisWarp.getNode("z").setValue(location.getZ());
        }
    }
}
