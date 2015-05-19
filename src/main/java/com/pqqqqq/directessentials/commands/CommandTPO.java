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
        return CommandSpec.builder().executor(new CommandTPO(plugin)).description(Texts.of(TextColors.AQUA, "Teleports a player to another.")).permission("directessentials.tpo")
                .arguments(GenericArguments.seq(GenericArguments.player(Texts.of("DestinationPlayer"), plugin.getGame()), GenericArguments.playerOrSource(Texts.of("TeleporterPlayer"), plugin.getGame()))).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        Optional<Player> teleporter = arguments.<Player>getOne("TeleporterPlayer");
        Optional<Player> destination = arguments.<Player>getOne("DestinationPlayer");

        teleporter.get().setLocationSafely(destination.get().getLocation());
        teleporter.get().sendMessage(Texts.of(TextColors.AQUA, "You have been teleported to: ", TextColors.WHITE, destination.get().getName()));
        if (!teleporter.equals(source)) {
            source.sendMessage(Texts.of(TextColors.AQUA, "Teleport successful."));
        }
        return CommandResult.success();
    }
}
