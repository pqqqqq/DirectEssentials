package com.pqqqqq.directessentials.events;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.config.Config;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.Map;

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
        Player player = event.getEntity();
        plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());
    }

    @Subscribe(order = Order.FIRST)
    public void command(CommandEvent event) {
        CommandSource source = event.getSource();

        if (!(source instanceof Player)) {
            return;
        }

        Player player = (Player) source;
        String command = (event.getCommand() + " " + event.getArguments()).trim();

        for (Map.Entry<String, String> entry : Config.shortcuts.entrySet()) {
            String check = entry.getKey();
            String run = entry.getValue();

            if (command.toLowerCase().startsWith(check.toLowerCase())) {
                Optional<? extends CommandMapping> mapping = plugin.getGame().getCommandDispatcher().get(run);

                if (mapping.isPresent()) {
                    try {
                        Optional<CommandResult> result = mapping.get().getCallable().process(player, command.substring(command.indexOf(check) + check.length()).trim());
                        event.setResult((result.isPresent() ? result.get() : CommandResult.empty()));
                        event.setCancelled(true);
                    } catch (CommandException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }
}
