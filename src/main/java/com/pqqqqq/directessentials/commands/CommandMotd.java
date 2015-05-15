package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.util.Utilities;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

/**
 * Created by Kevin on 2015-05-12.
 */
public class CommandMotd implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandMotd(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().setExecutor(new CommandMotd(plugin)).setDescription(Texts.of(TextColors.AQUA, "Displays the MOTD."))
                .setArguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Texts.of("MOTD")))).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        if (!arguments.hasAny("MOTD")) {
            String motd = plugin.getEssentialsGame().getMotd();
            if (motd == null) {
                source.sendMessage(Texts.of(TextColors.RED, "No custom MOTD has been configured."));
            } else {
                source.sendMessage(Texts.of(motd));
            }
        } else {
            if (!source.hasPermission("directessentials.motd.set") && !source.hasPermission("directessentials.*")) {
                source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
                return CommandResult.success();
            }

            String newmotd = arguments.<String>getOne("MOTD").get();
            plugin.getEssentialsGame().setMotd(Utilities.formatColour(newmotd));
            source.sendMessage(Texts.of(TextColors.GREEN, "New MOTD has been set."));
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.motd") || source.hasPermission("directessentials.*");
    }
}
