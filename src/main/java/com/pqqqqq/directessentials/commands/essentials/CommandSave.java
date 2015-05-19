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
public class CommandSave implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSave(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandSave(plugin)).description(Texts.of(TextColors.AQUA, "Saves the current data.")).permission("directessentials.save").build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        plugin.getDataConfig().save();
        source.sendMessage(Texts.of(TextColors.GREEN, "Data saved."));
        return CommandResult.success();
    }
}
