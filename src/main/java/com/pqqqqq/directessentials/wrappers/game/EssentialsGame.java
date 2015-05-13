package com.pqqqqq.directessentials.wrappers.game;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.Warp;
import com.pqqqqq.directessentials.wrappers.WeakEssentialsMap;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import com.pqqqqq.directessentials.wrappers.world.EssentialsWorld;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Game;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by Kevin on 2015-05-12.
 */
public class EssentialsGame implements ISaveable {
    private final Game game;

    // Wrapper data
    private final WeakEssentialsMap<String, EssentialsWorld> worlds = new WeakEssentialsMap<String, EssentialsWorld>();
    private final WeakEssentialsMap<String, EssentialsUser> users = new WeakEssentialsMap<String, EssentialsUser>();

    // Other data
    private final WeakEssentialsMap<String, Warp> warps = new WeakEssentialsMap<String, Warp>();
    private Location spawn = null;

    public EssentialsGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public WeakEssentialsMap<String, EssentialsWorld> getWorlds() {
        return worlds;
    }

    public EssentialsWorld getOrCreateWorld(String world) {
        return worlds.getOrCreate(world, new EssentialsWorld(), world);
    }

    public WeakEssentialsMap<String, EssentialsUser> getUsers() {
        return users;
    }

    public EssentialsUser getOrCreateUser(String uuid) {
        return users.getOrCreate(uuid, new EssentialsUser(), uuid);
    }

    public WeakEssentialsMap<String, Warp> getWarps() {
        return warps;
    }

    public Warp getOrCreateWarp(String name) {
        return warps.getOrCreate(name, new Warp(), name);
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public void load(ConfigurationNode node) {
        // World loading
        worlds.clear();

        // User loading
        users.clear();
        users.putAll(EssentialsUser.loadUsers(node));

        // Warp loading
        warps.clear();
        warps.putAll(Warp.loadWarps(node));

        // Spawn loading
        ConfigurationNode spawn = node.getNode("spawn");
        String world = spawn.getNode("world").getString();
        double x = spawn.getNode("x").getDouble();
        double y = spawn.getNode("y").getDouble();
        double z = spawn.getNode("z").getDouble();

        Optional<World> worldObj = DirectEssentials.plugin.getGame().getServer().getWorld(world);
        if (worldObj.isPresent()) {
            this.spawn = new Location(worldObj.get(), new Vector3d(x, y, z));
        }
    }

    public void save(ConfigurationNode node) {
        // World saving
        for (EssentialsWorld world : worlds) {
            world.save(node);
        }

        // User saving
        for (EssentialsUser user : users) {
            user.save(node);
        }

        // Warps saving
        for (Warp warp : warps) {
            warp.save(node);
        }

        // Spawn saving
        if (this.spawn != null) {
            ConfigurationNode spawn = node.getNode("spawn");
            spawn.getNode("world").setValue(((World) this.spawn.getExtent()).getName());
            spawn.getNode("x").setValue(this.spawn.getX());
            spawn.getNode("y").setValue(this.spawn.getY());
            spawn.getNode("z").setValue(this.spawn.getZ());
        }
    }
}
