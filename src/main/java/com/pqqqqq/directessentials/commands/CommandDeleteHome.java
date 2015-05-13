package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

/**
 * Created by Kevin on 2015-05-12.
 */
public class CommandDeleteHome extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text>of(Texts.of(TextColors.AQUA, "Deletes a home to a place."));
    public static final Optional<Text> help = Optional.<Text>of(Texts.of(TextColors.AQUA, "Deletes a home to a place."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/deletehome <name>");

    public CommandDeleteHome(DirectEssentials plugin) {
        super(plugin);
    }

    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return Optional.of(CommandResult.success());
        }

        if (!(source instanceof Player)) {
            source.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return Optional.of(CommandResult.success());
        }

        Player player = (Player) source;
        EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());
        String[] args = arguments.trim().split(" ");

        if (arguments.trim().isEmpty()) {
            source.sendMessage(getUsage(source));
            return Optional.of(CommandResult.success());
        }

        if (user.getHomes().remove(args[0]) == null) {
            source.sendMessage(Texts.of(TextColors.RED, "There is no home with this name."));
            return Optional.of(CommandResult.success());
        }

        source.sendMessage(Texts.of(TextColors.GREEN, "Home deleted successfully."));
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.home") || source.hasPermission("directessentials.*");
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
