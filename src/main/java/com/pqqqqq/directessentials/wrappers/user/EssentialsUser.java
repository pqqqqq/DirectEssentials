package com.pqqqqq.directessentials.wrappers.user;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.config.EventCommand;
import com.pqqqqq.directessentials.data.Home;
import com.pqqqqq.directessentials.data.region.Cuboid;
import com.pqqqqq.directessentials.wrappers.WeakEssentialsMap;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by Kevin on 2015-05-11.
 */
public class EssentialsUser implements IWeakValue, ISaveable {
    private String uuid;

    // Stuff to save
    private final WeakEssentialsMap<String, Home> homes = new WeakEssentialsMap<String, Home>();

    // Cache stuff
    private Optional<Player> cachedPlayer = Optional.<Player> absent();
    private String lastCachedUsername = null;
    private InetSocketAddress lastCachedIP = null;

    private boolean requestingTeleport = false;
    private boolean teleportingDisabled = false;
    private final Map<EssentialsUser, Boolean> tpRequests = new HashMap<EssentialsUser, Boolean>(); // True means teleport here

    private final Set<EventCommand> commandDelay = new HashSet<EventCommand>();

    private final Cuboid regionSelection = new Cuboid();
    private boolean regionSelectDelay = false;

    private boolean invisible = false;

    public static Map<String, EssentialsUser> loadUsers(ConfigurationNode node) {
        Map<String, EssentialsUser> users = new HashMap<String, EssentialsUser>();

        ConfigurationNode usersNode = node.getNode("users");
        for (ConfigurationNode user : usersNode.getChildrenMap().values()) {
            EssentialsUser userObj = new EssentialsUser(user.getKey().toString());

            userObj.setLastCachedUsername(user.getNode("username").getString(null)); // Load username

            // Load IP address
            String address = user.getNode("IP").getString(null);
            if (address != null) {
                String[] split = address.split(":");
                userObj.setLastCachedIP(InetSocketAddress.createUnresolved(split[0], Integer.parseInt(split[1])));
            }
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
            this.lastCachedUsername = cachedPlayer.get().getName();
            this.lastCachedIP = cachedPlayer.get().getConnection().getAddress();
            return cachedPlayer;
        }

        Optional<Player> player = DirectEssentials.plugin.getGame().getServer().getPlayer(UUID.fromString(this.uuid));
        if (player.isPresent()) {
            this.lastCachedUsername = player.get().getName();
            this.lastCachedIP = player.get().getConnection().getAddress();
        }

        return (this.cachedPlayer = player);
    }

    public boolean sendMessage(Text... message) {
        Optional<Player> playerOptional = getPlayer();
        if (playerOptional.isPresent()) {
            playerOptional.get().sendMessage(message);
            return true;
        }

        return false;
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

    public InetSocketAddress getLastCachedIP() {
        return lastCachedIP;
    }

    public String getLastCachedIPString() {
        return lastCachedIP == null ? "" : lastCachedIP.toString();
    }

    public void setLastCachedIP(InetSocketAddress lastCachedIP) {
        this.lastCachedIP = lastCachedIP;
    }

    public WeakEssentialsMap<String, Home> getHomes() {
        return homes;
    }

    public Home getOrCreateHome(String name) {
        return this.homes.getOrCreate(name, new Home(), name, this.uuid);
    }

    public Set<EventCommand> getCommandDelay() {
        return commandDelay;
    }

    public boolean isRequestingTeleport() {
        return requestingTeleport;
    }

    public void setRequestingTeleport(boolean requestingTeleport) {
        this.requestingTeleport = requestingTeleport;
    }

    public boolean isTeleportingDisabled() {
        return teleportingDisabled;
    }

    public void setTeleportingDisabled(boolean teleportingDisabled) {
        this.teleportingDisabled = teleportingDisabled;
    }

    public Map<EssentialsUser, Boolean> getTpRequests() {
        return tpRequests;
    }

    public Cuboid getRegionSelection() {
        return regionSelection;
    }

    public boolean isRegionSelectDelay() {
        return regionSelectDelay;
    }

    public void setRegionSelectDelay(boolean regionSelectDelay) {
        this.regionSelectDelay = regionSelectDelay;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public void init(Object... args) {
        this.uuid = (String) args[0];
        getPlayer(); // To get cached username
    }

    public void save(ConfigurationNode node) {
        ConfigurationNode users = node.getNode("users");
        ConfigurationNode user = users.getNode(this.uuid);

        // Save username
        getPlayer(); // For retrieving the cached username
        if (lastCachedUsername != null) {
            user.getNode("username").setValue(lastCachedUsername);
        }
        if (lastCachedIP != null) {
            user.getNode("IP").setValue(lastCachedIP.toString());
        }

        // Save homes
        for (Home home : homes) {
            home.save(user);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EssentialsUser && uuid.equals(((EssentialsUser) obj).getUuid());
    }
}
