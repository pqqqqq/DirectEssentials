package com.pqqqqq.directessentials.wrappers.world;

import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Created by Kevin on 2015-05-12.
 */
public class EssentialsWorld implements IWeakValue, ISaveable {
    private String world;

    public EssentialsWorld() {
    }

    public EssentialsWorld(String world) {
        this.init(world);
    }

    public String getWorld() {
        return this.world;
    }

    public void init(Object... args) {
        this.world = (String) args[0];
    }

    public void save(ConfigurationNode node) {

    }
}
