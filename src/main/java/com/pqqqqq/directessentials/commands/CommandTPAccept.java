package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.Map;

/**
 * Created by Kevin on 2015-05-13.
 */
public class CommandTPAccept implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandTPAccept(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandTPAccept(plugin)).description(Texts.of(TextColors.AQUA, "Accept an incoming TP request"))
                .arguments(GenericArguments.optional(GenericArguments.player(Texts.of("Player"), plugin.getGame()))).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        if (!(source instanceof Player)) {
            source.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) source;
        EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());

        if (!arguments.hasAny("Player")) {
            // List teleport requests
            TextBuilder requestText = Texts.builder("Pending requests (* = here): ").color(TextColors.AQUA);
            int num = 0;

            for (Map.Entry<EssentialsUser, Boolean> tpRequestEntry : user.getTpRequests().entrySet()) {
                requestText.append(Texts.builder((num++ > 0 ? ", " : "") + (tpRequestEntry.getValue() ? "*" : "") + tpRequestEntry.getKey().getLastCachedUsername()).color(TextColors.WHITE)
                        .onClick(TextActions.runCommand("/tpaccept " + tpRequestEntry.getKey().getLastCachedUsername())).onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Accept this request."))).build());
            }

            if (num == 0) {
                source.sendMessage(Texts.of(TextColors.AQUA, "You have no pending teleportation requests."));
            } else {
                source.sendMessage(requestText.build());
            }
        } else {
            Optional<Player> accept = arguments.<Player>getOne("Player");
            if (!accept.isPresent()) {
                source.sendMessage(Texts.of(TextColors.RED, "This player is currently not online."));
                return CommandResult.success();
            }

            EssentialsUser acceptUser = plugin.getEssentialsGame().getOrCreateUser(accept.get().getUniqueId().toString());
            Boolean teleportHere = user.getTpRequests().remove(acceptUser);
            if (teleportHere == null) {
                source.sendMessage(Texts.of(TextColors.WHITE, accept.get().getName(), TextColors.AQUA, " has not requested a teleportation from you."));
                return CommandResult.success();
            }

            acceptUser.setRequestingTeleport(false);
            if (teleportHere) {
                player.setLocationSafely(accept.get().getLocation());
                player.sendMessage(Texts.of(TextColors.AQUA, "You accepted the teleport request, teleported to ", TextColors.WHITE, accept.get().getName()));
                accept.get().sendMessage(Texts.of(TextColors.WHITE, player.getName(), TextColors.AQUA, " accepted your teleport request and was teleported to you."));
            } else {
                accept.get().setLocationSafely(player.getLocation());
                player.sendMessage(Texts.of(TextColors.AQUA, "You accepted the teleport request and ", TextColors.WHITE, accept.get().getName(), TextColors.AQUA, " was teleported to you."));
                accept.get().sendMessage(Texts.of(TextColors.WHITE, player.getName(), TextColors.AQUA, " accepted your teleport request, teleported to ", TextColors.WHITE, player.getName()));
            }
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.tpaccept") || source.hasPermission("directessentials.*");
    }
}
