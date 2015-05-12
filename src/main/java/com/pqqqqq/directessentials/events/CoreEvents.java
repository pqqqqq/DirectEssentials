package com.pqqqqq.directessentials.events;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.user.EssentialsUser;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;

/**
 * Created by Kevin on 2015-05-11.
 */
public class CoreEvents {
    private DirectEssentials plugin;

    public CoreEvents(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if (!plugin.getUserManager().contains(uuid)) {
            plugin.getUserManager().add(uuid, new EssentialsUser(uuid));
        }
    }
}
