package com.pqqqqq.directessentials.commands.essentials;

import com.pqqqqq.directessentials.DirectEssentials;
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
public class CommandReload implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandReload(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandReload(plugin)).description(Texts.of(TextColors.AQUA, "Reloads the main config.")).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        plugin.getConfig().load();
        source.sendMessage(Texts.of(TextColors.GREEN, "Config reloaded."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.reload") || source.hasPermission("directessentials.*");
    }
}
