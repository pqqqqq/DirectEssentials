package com.pqqqqq.directessentials.wrappers.user;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.Location;

import java.util.UUID;

/**
 * Created by Kevin on 2015-05-11.
 */
public class EssentialsUser implements IWeakValue, ISaveable {
    private String uuid;

    private Optional<Player> cachedPlayer = Optional.<Player> absent();
    private String lastCachedUsername;

    private Optional<Location> home = Optional.<Location> absent();

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
        if (cachedPlayer.isPresent() && cachedPlayer.get().isLoaded() && !cachedPlayer.get().isRemoved()) {
            return cachedPlayer;
        }

        Optional<Player> player = DirectEssentials.plugin.getGame().getServer().getPlayer(UUID.fromString(uuid));
        if (player.isPresent()) {
            lastCachedUsername = player.get().getName();
        }

        return (cachedPlayer = player);
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

    public Optional<Location> getHome() {
        return home;
    }

    public void setHome(Optional<Location> home) {
        this.home = home;
    }

    public void goHome() {
        Optional<Player> player = getPlayer();
        if (player.isPresent()) {
            player.get().setLocation(home.get());
        }
    }

    public void init(Object... args) {
        this.uuid = (String) args[0];
    }

    public void save(ConfigurationNode node) {

    }
}
