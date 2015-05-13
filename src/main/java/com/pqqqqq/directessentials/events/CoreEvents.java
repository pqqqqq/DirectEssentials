package com.pqqqqq.directessentials.events;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.config.EventCommand;
import com.pqqqqq.directessentials.config.Config;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 2015-05-11.
 */
public class CoreEvents {
    private DirectEssentials plugin;

    public CoreEvents(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    // Set to spawn, create EssentialsUser instance
    @Subscribe
    public void join(PlayerJoinEvent event) {
        Player player = event.getEntity();
        plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());

        // Teleport to spawn if it exists
        if (plugin.getEssentialsGame().getSpawn() != null) {
            player.setLocation(plugin.getEssentialsGame().getSpawn());
        }
    }

    // Shortcuts and timers, EventCommands
    @Subscribe(order = Order.FIRST)
    public void command(CommandEvent event) {
        CommandSource source = event.getSource();

        if (!(source instanceof Player)) {
            return;
        }

        final Player player = (Player) source;
        final EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());
        String command = (event.getCommand() + " " + event.getArguments()).trim(); // Command is the base command plus the arguments separated by a space

        for (final EventCommand eventCommand : Config.commands) { // Iterate through all the config specified event commands
            boolean didRun = false; // Boolean to determine if the command was ever run.
            String run = eventCommand.getCommand();

            if (command.toLowerCase().startsWith(run.toLowerCase())) { // Basic run of the command
                // Check if timer still in event
                if (user.getCommandDelay().containsKey(eventCommand)) {
                    // Too bad, can't run right now.
                    player.sendMessage(Texts.of(TextColors.AQUA, "Please wait ", TextColors.WHITE, eventCommand.getTimerSeconds(), TextColors.AQUA, " second(s) before running this command again."));
                    event.setCancelled(true);
                    event.setResult(CommandResult.success());
                    return;
                }

                didRun = true;
            } else {
                // Shortcuts
                for (String alias : eventCommand.getShortcuts()) {
                    if (command.toLowerCase().startsWith(alias.toLowerCase())) {
                        if (user.getCommandDelay().containsKey(eventCommand)) {
                            // Too bad, can't run right now.
                            player.sendMessage(Texts.of(TextColors.AQUA, "Please wait ", TextColors.WHITE, eventCommand.getTimerSeconds(), TextColors.AQUA, " second(s) before running this command again."));
                            event.setCancelled(true);
                            event.setResult(CommandResult.success());
                            return;
                        }

                        Optional<? extends CommandMapping> mapping = plugin.getGame().getCommandDispatcher().get(run); // Get the mapping

                        if (mapping.isPresent()) {
                            try {
                                Optional<CommandResult> result = mapping.get().getCallable().process(player, command.substring(command.indexOf(alias) + alias.length()).trim()); // Execute, trim the base command off of the command specified.
                                event.setResult((result.isPresent() ? result.get() : CommandResult.empty()));
                                event.setCancelled(true);
                                didRun = true;
                            } catch (CommandException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                }
            }

            // Timer
            if (didRun && eventCommand.getTimerSeconds() > 0) {
                user.getCommandDelay().put(eventCommand, true); // Add delay
                plugin.getGame().getAsyncScheduler().runTaskAfter(plugin, new Runnable() { // Start async task to unset delay

                    public void run() {
                        user.getCommandDelay().remove(eventCommand);
                    }
                }, TimeUnit.SECONDS, eventCommand.getTimerSeconds());
                return; // Return because we found the command we were looking for
            }
        }
    }

    // Teleport back to spawn on death
    /* TODO: Readd this when implemented by Sponge

    @Subscribe
    public void respawn(PlayerRespawnEvent event) {
        final Player player = event.getEntity();

        if (plugin.getEssentialsGame().getSpawn() != null) {
            event.setSpawnLocation(plugin.getEssentialsGame().getSpawn());
        }
    }
    */
}
