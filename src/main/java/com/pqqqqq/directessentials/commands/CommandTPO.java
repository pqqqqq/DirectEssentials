package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

/**
 * Created by Kevin on 2015-05-13.
 */
public class CommandTPO implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandTPO(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().setExecutor(new CommandTPO(plugin)).setDescription(Texts.of(TextColors.AQUA, "Teleports a player to another."))
                .setArguments(GenericArguments.seq(GenericArguments.player(Texts.of("DestinationPlayer"), plugin.getGame()), GenericArguments.playerOrSource(Texts.of("TeleporterPlayer"), plugin.getGame()))).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        Optional<Player> teleporter = arguments.<Player>getOne("TeleporterPlayer");
        Optional<Player> destination = arguments.<Player>getOne("DestinationPlayer");

        if (!teleporter.isPresent()) {
            source.sendMessage(Texts.of(TextColors.RED, "Specify an online player or run as a player."));
            return CommandResult.success();
        }

        if (!destination.isPresent()) {
            source.sendMessage(Texts.of(TextColors.RED, "This player is not currently online."));
            return CommandResult.success();
        }

        teleporter.get().setLocationSafely(destination.get().getLocation());
        teleporter.get().sendMessage(Texts.of(TextColors.AQUA, "You have been teleported to: ", TextColors.WHITE, destination.get().getName()));
        if (!teleporter.equals(source)) {
            source.sendMessage(Texts.of(TextColors.AQUA, "Teleport successful."));
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.tpo") || source.hasPermission("directessentials.*");
    }
}
