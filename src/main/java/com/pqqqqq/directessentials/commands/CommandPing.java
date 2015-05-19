package com.pqqqqq.directessentials.commands;

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
 * Created by Kevin on 2015-05-19.
 */
public class CommandPing implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandPing(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandPing(plugin)).description(Texts.of(TextColors.AQUA, "Tests ping on server with a message")).permission("directessentials.ping").build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Texts.of(TextColors.GREEN, "Pong!"));
        return CommandResult.success();
    }
}
