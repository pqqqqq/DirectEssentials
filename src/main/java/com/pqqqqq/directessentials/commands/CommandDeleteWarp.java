package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.elements.EssentialsArguments;
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
public class CommandDeleteWarp implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandDeleteWarp(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().setExecutor(new CommandDeleteWarp(plugin)).setDescription(Texts.of(TextColors.AQUA, "Deletes a player warp."))
                .setArguments(EssentialsArguments.warp(Texts.of("WarpName"), plugin)).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        if (plugin.getEssentialsGame().getWarps().remove(arguments.<String>getOne("WarpName").get()) == null) {
            source.sendMessage(Texts.of(TextColors.RED, "There is no warp with this name."));
            return CommandResult.success();
        }

        source.sendMessage(Texts.of(TextColors.GREEN, "Warp deleted successfully."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.deletewarp") || source.hasPermission("directessentials.*");
    }
}
