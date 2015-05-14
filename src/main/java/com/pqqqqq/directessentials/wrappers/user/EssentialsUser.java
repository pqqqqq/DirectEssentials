package com.pqqqqq.directessentials.wrappers.user;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.config.EventCommand;
import com.pqqqqq.directessentials.data.Home;
import com.pqqqqq.directessentials.wrappers.WeakEssentialsMap;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Kevin on 2015-05-11.
 */
public class EssentialsUser implements IWeakValue, ISaveable {
    private String uuid;

    private Optional<Player> cachedPlayer = Optional.<Player> absent();
    private String lastCachedUsername = null;

    private final WeakEssentialsMap<String, Home> homes = new WeakEssentialsMap<String, Home>();

    private final Map<EventCommand, Boolean> commandDelay = new HashMap<EventCommand, Boolean>();

    public static Map<String, EssentialsUser> loadUsers(ConfigurationNode node) {
        Map<String, EssentialsUser> users = new HashMap<String, EssentialsUser>();

        ConfigurationNode usersNode = node.getNode("users");
        for (ConfigurationNode user : usersNode.getChildrenMap().values()) {
            EssentialsUser userObj = new EssentialsUser(user.getKey().toString());

            userObj.setLastCachedUsername(user.getNode("username").getString(null)); // Load username
            userObj.getHomes().putAll(Home.loadHomes(user, userObj)); // Load homes

            users.put(user.getKey().toString(), userObj);
        }

        return users;
    }

    public EssentialsUser() {
    }

    public EssentialsUser(String uuid) {
        this.init(uuid);
    }

    public String getUuid() {
        return this.uuid;
    }

    /**
     * Returns an {@link Optional} {@link Player} instance corresponding to the UUID. Returns a cached value if possible.
     * @return the player
     */
    public Optional<Player> getPlayer() {
        if (this.cachedPlayer.isPresent() && this.cachedPlayer.get().isLoaded() && !this.cachedPlayer.get().isRemoved()) {
            return cachedPlayer;
        }

        Optional<Player> player = DirectEssentials.plugin.getGame().getServer().getPlayer(UUID.fromString(this.uuid));
        if (player.isPresent()) {
            this.lastCachedUsername = player.get().getName();
        }

        return (this.cachedPlayer = player);
    }

    /**
     * Gets the last cached username of this wrappers.
     * @return the last cached username
     */
    public String getLastCachedUsername() {
        return lastCachedUsername;
    }

    /**
     * Sets the last cached username of this wrappers.
     * @param lastCachedUsername the new last cached username
     */
    public void setLastCachedUsername(String lastCachedUsername) {
        this.lastCachedUsername = lastCachedUsername;
    }

    public WeakEssentialsMap<String, Home> getHomes() {
        return homes;
    }

    public Home getOrCreateHome(String name) {
        return this.homes.getOrCreate(name, new Home(), name, this.uuid);
    }

    public Map<EventCommand, Boolean> getCommandDelay() {
        return commandDelay;
    }

    public void init(Object... args) {
        this.uuid = (String) args[0];
    }

    public void save(ConfigurationNode node) {
        ConfigurationNode users = node.getNode("users");
        ConfigurationNode user = users.getNode(this.uuid);

        // Save username
        getPlayer(); // For retrieving the cached username
        if (lastCachedUsername != null) {
            user.getNode("username").setValue(lastCachedUsername);
        }

        // Save homes
        for (Home home : homes) {
            home.save(user);
        }
    }
}
