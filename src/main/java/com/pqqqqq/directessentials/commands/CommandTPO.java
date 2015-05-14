package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

/**
 * Created by Kevin on 2015-05-13.
 */
public class CommandTPO extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text>of(Texts.of(TextColors.AQUA, "Teleport between two players."));
    public static final Optional<Text> help = Optional.<Text>of(Texts.of(TextColors.AQUA, "Teleport between two players."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/tpo [teleporter] <locationplayer>");

    public CommandTPO(DirectEssentials plugin) {
        super(plugin);
    }

    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return Optional.of(CommandResult.success());
        }

        String[] args = arguments.trim().split(" ");
        if (arguments.trim().isEmpty()) {
            source.sendMessage(getUsage(source));
            return Optional.of(CommandResult.success());
        }

        Player teleporter = null;
        Optional<Player> destination = plugin.getGame().getServer().getPlayer(args[(args.length == 1 ? 0 : 1)]);

        if (args.length == 1) {
            if (!(source instanceof Player)) {
                source.sendMessage(Texts.of(TextColors.RED, "Either run as a player or specify one."));
                return Optional.of(CommandResult.success());
            }

            teleporter = (Player) source;
        } else {
            Optional<Player> tele = plugin.getGame().getServer().getPlayer(args[0]);
            if (!tele.isPresent()) {
                source.sendMessage(Texts.of(TextColors.RED, "Invalid player: ", TextColors.WHITE, args[0]));
                return Optional.of(CommandResult.success());
            }

            teleporter = tele.get();
        }

        if (!destination.isPresent()) {
            source.sendMessage(Texts.of(TextColors.RED, "Invalid destination player."));
            return Optional.of(CommandResult.success());
        }

        teleporter.setLocation(destination.get().getLocation());
        teleporter.sendMessage(Texts.of(TextColors.AQUA, "You have been teleported to: ", TextColors.WHITE, destination.get().getName()));
        if (!teleporter.equals(source)) {
            source.sendMessage(Texts.of(TextColors.AQUA, "Teleport successful."));
        }
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.tpo") || source.hasPermission("directessentials.*");
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
