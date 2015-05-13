package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.config.Config;
import com.pqqqqq.directessentials.data.Home;
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
public class CommandSetHome extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text>of(Texts.of(TextColors.AQUA, "Sets a home to a place."));
    public static final Optional<Text> help = Optional.<Text>of(Texts.of(TextColors.AQUA, "Sets a home to a place."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/sethome <name>");

    public CommandSetHome(DirectEssentials plugin) {
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
            source.sendMessage(getUsage(player));
            return Optional.of(CommandResult.success());
        }

        if (user.getHomes().size() >= Config.homesLimit) {
            source.sendMessage(Texts.of(TextColors.RED, "You cannot have more than ", TextColors.WHITE, Config.homesLimit, TextColors.RED, " home(s)."));
            return Optional.of(CommandResult.success());
        }

        if (user.getHomes().contains(args[0])) {
            source.sendMessage(Texts.of(TextColors.RED, "A home already exists with this name."));
            return Optional.of(CommandResult.success());
        }

        Home home = new Home(args[0], player.getUniqueId().toString());
        home.setLocation(player.getLocation());
        user.getHomes().put(args[0], home);

        source.sendMessage(Texts.of(TextColors.GREEN, "Home created successfully."));
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
