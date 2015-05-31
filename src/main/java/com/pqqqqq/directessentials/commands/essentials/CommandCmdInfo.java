package com.pqqqqq.directessentials.commands.essentials;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.*;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.lang.reflect.Field;

/**
 * Created by Kevin on 2015-05-31.
 */
public class CommandCmdInfo implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandCmdInfo(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandCmdInfo(plugin)).description(Texts.of(TextColors.AQUA, "Gets information about a command.")).permission("directessentials.cmdinfo")
                .arguments(GenericArguments.string(Texts.of("Command"))).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String command = args.<String>getOne("Command").get();
        Optional<? extends CommandMapping> commandMappingOptional = plugin.getGame().getCommandDispatcher().get(command);

        if (!commandMappingOptional.isPresent()) {
            src.sendMessage(Texts.of(TextColors.RED, "Unknown command: ", TextColors.WHITE, command));
        } else {
            CommandMapping commandMapping = commandMappingOptional.get();
            CommandCallable commandCallable = commandMapping.getCallable();

            if (!(commandCallable instanceof CommandSpec)) {
                src.sendMessage(Texts.of(TextColors.RED, "This command does not have the CommandSpec inheritance."));
            } else {
                CommandSpec commandSpec = (CommandSpec) commandCallable;

                // Use reflection for extended description and permission since there is no getter method.
                try {
                    Field extendedDescriptionField = CommandSpec.class.getDeclaredField("extendedDescription");
                    extendedDescriptionField.setAccessible(true);
                    Object extendedDescription = extendedDescriptionField.get(commandSpec);

                    Field permissionField = CommandSpec.class.getDeclaredField("permission");
                    permissionField.setAccessible(true);
                    Object permission = permissionField.get(commandSpec);

                    Optional<Text> shortDescription = commandSpec.getShortDescription(src);

                    TextBuilder builder = Texts.builder();
                    builder.append(Texts.of(TextColors.AQUA, "Command: ", TextColors.WHITE, "/", command))
                            .append(CommandMessageFormatting.NEWLINE_TEXT)
                            .append(Texts.of(TextColors.AQUA, "Alias(es): ", TextColors.WHITE, commandMapping.getAllAliases().toString()))
                            .append(CommandMessageFormatting.NEWLINE_TEXT)
                            .append(Texts.of(TextColors.AQUA, "Executor class: ", TextColors.WHITE, commandSpec.getExecutor().getClass().toString()))
                            .append(CommandMessageFormatting.NEWLINE_TEXT)
                            .append(Texts.of(TextColors.AQUA, "S description: "))
                            .append(!shortDescription.isPresent() ? Texts.of(TextColors.GRAY, "None") : shortDescription.get())
                            .append(CommandMessageFormatting.NEWLINE_TEXT)
                            .append(Texts.of(TextColors.AQUA, "L description: "))
                            .append((extendedDescription == null ? Texts.of(TextColors.GRAY, "None") : (Text) extendedDescription))
                            .append(CommandMessageFormatting.NEWLINE_TEXT)
                            .append(Texts.of(TextColors.AQUA, "Permission: ", TextColors.WHITE, (permission == null ? "None" : permission)))
                            .append(CommandMessageFormatting.NEWLINE_TEXT)
                            .append(Texts.of(TextColors.AQUA, "Permissible: ", TextColors.WHITE, commandCallable.testPermission(src)));

                    src.sendMessage(builder.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return CommandResult.success();
    }
}
