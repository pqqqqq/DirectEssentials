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
 * Created by Kevin on 2015-05-18.
 */
public class CommandEssentials implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandEssentials(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandEssentials(plugin)).description(Texts.of(TextColors.AQUA, "Essentials main plugin command"))
                .child(CommandReload.build(plugin), "reload").child(CommandSave.build(plugin), "save").child(CommandHelp.build(plugin), "help").build();
    }

    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        commandSource.sendMessage(Texts.of(TextColors.RED, "/ess <reload|save|help>"));
        return CommandResult.success();
    }
}
