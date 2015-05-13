package com.pqqqqq.directessentials.wrappers.game;

import com.pqqqqq.directessentials.data.Warp;
import com.pqqqqq.directessentials.wrappers.WeakEssentialsMap;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import com.pqqqqq.directessentials.wrappers.world.EssentialsWorld;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Game;

/**
 * Created by Kevin on 2015-05-12.
 */
public class EssentialsGame implements ISaveable {
    private final Game game;
    private final WeakEssentialsMap<String, EssentialsWorld> worlds = new WeakEssentialsMap<String, EssentialsWorld>();
    private final WeakEssentialsMap<String, EssentialsUser> users = new WeakEssentialsMap<String, EssentialsUser>();

    private final WeakEssentialsMap<String, Warp> warps = new WeakEssentialsMap<String, Warp>();

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

    public void load(ConfigurationNode node) {
        // World loading
        worlds.clear();

        // User loading
        users.clear();
        users.putAll(EssentialsUser.loadUsers(node));

        // Warp loading
        warps.clear();
        warps.putAll(Warp.loadWarps(node));
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
    }
}
