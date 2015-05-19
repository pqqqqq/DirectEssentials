package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

/**
 * Created by Kevin on 2015-05-18.
 */
public class CommandSudo implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSudo(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandSudo(plugin)).description(Texts.of(TextColors.AQUA, "Makes a player perform a command."))
                .permission("directessentials.sudo").arguments(GenericArguments.player(Texts.of("Player"), plugin.getGame()), GenericArguments.remainingJoinedStrings(Texts.of("Command"))).build();
    }

    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        Player player = commandContext.<Player>getOne("Player").get();
        String fullCommand = commandContext.<String>getOne("Command").get();

        // Truncate to only base command
        String baseCommand = fullCommand;
        String arguments = "";

        if (fullCommand.contains(" ")) {
            baseCommand = fullCommand.substring(0, fullCommand.indexOf(' '));
            arguments = fullCommand.substring(fullCommand.indexOf(' ') + 1);
        }

        // Retrieve the command mapping
        Optional<? extends CommandMapping> commandMapping = plugin.getGame().getCommandDispatcher().get(baseCommand);
        if (!commandMapping.isPresent()) {
            commandSource.sendMessage(Texts.of(TextColors.RED, "The mapping for this command could not be found."));
        } else {
            commandMapping.get().getCallable().process(player, arguments);
            commandSource.sendMessage(Texts.of(TextColors.GREEN, "Command executed."));

            if (player.hasPermission("directessentials.sudo.see")) {
                // Let them know who did this to them
                player.sendMessage(Texts.of(TextColors.WHITE, commandSource.getName(), TextColors.AQUA, " made you run the command: ", TextColors.WHITE, "/" + fullCommand));
            }
        }
        return CommandResult.success();
    }
}
