package com.pqqqqq.directessentials.data;

import com.pqqqqq.directessentials.util.SaveUtils;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.Location;

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
            Location location = SaveUtils.loadLocation(home);

            if (location != null) {
                homeObj.setLocation(location);
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

        player.setLocationSafely(location);
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
            ConfigurationNode thisHome = warps.getNode(name);

            SaveUtils.saveLocation(this.location, thisHome);
        }
    }
}
