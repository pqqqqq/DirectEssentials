package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

/**
 * Created by Kevin on 2015-05-12.
 */
public class CommandSpawn implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSpawn(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().setExecutor(new CommandSpawn(plugin)).setDescription(Texts.of(TextColors.AQUA, "Teleports to server spawn.")).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        if (!(source instanceof Player)) {
            source.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) source;

        if (plugin.getEssentialsGame().getSpawn() == null) {
            source.sendMessage(Texts.of(TextColors.RED, "No spawn has been set yet."));
            return CommandResult.success();
        }

        player.setLocationSafely(plugin.getEssentialsGame().getSpawn());
        source.sendMessage(Texts.of(TextColors.GREEN, "Teleported to spawn."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.spawn") || source.hasPermission("directessentials.*");
    }
}
