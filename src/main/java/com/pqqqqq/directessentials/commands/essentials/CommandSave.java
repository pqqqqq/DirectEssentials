package com.pqqqqq.directessentials.commands.essentials;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.CommandBase;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

/**
 * Created by Kevin on 2015-05-12.
 */
public class CommandSave extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text> of(Texts.of(TextColors.AQUA, "Saves the current data."));
    public static final Optional<Text> help = Optional.<Text> of(Texts.of(TextColors.AQUA, "Saves the current data."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/essentials save");

    public CommandSave(DirectEssentials plugin) {
        super(plugin);
    }

    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return Optional.of(CommandResult.success());
        }

        plugin.getDataConfig().save();
        source.sendMessage(Texts.of(TextColors.GREEN, "Data saved."));
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.save") || source.hasPermission("directessentials.*");
    }

    public Optional<Text> getShortDescription(CommandSource source) {
        return desc;
    }

    public Optional<Text> getHelp(CommandSource source) {
        return help;
    }

    public Text getUsage(CommandSource source) {
        return usage;
    }
}
