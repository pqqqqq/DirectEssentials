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
public class CommandSetSpawn implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSetSpawn(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandSetSpawn(plugin)).description(Texts.of(TextColors.AQUA, "Set the server spawn.")).build();
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
        plugin.getEssentialsGame().setSpawn(player.getLocation());

        source.sendMessage(Texts.of(TextColors.GREEN, "Spawn set successfully."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.setspawn") || source.hasPermission("directessentials.*");
    }
}
