package com.pqqqqq.directessentials.commands.region;

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
public class CommandRegion implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandRegion(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandRegion(plugin)).description(Texts.of(TextColors.AQUA, "Region utility commands"))
                .child(CommandCreate.build(plugin), "create", "c").child(CommandAdd.build(plugin), "add", "addmember", "am").child(CommandRemove.build(plugin), "remove", "removemember", "rm")
                .child(CommandSetOwner.build(plugin), "setowner", "so").build();
    }

    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        commandSource.sendMessage(Texts.of(TextColors.RED, "/region <create|add|remove|setowner>"));
        return CommandResult.success();
    }
}
