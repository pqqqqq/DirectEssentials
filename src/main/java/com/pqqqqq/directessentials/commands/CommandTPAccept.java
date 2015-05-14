package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.Map;

/**
 * Created by Kevin on 2015-05-13.
 */
public class CommandTPAccept extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text>of(Texts.of(TextColors.AQUA, "Accepts a teleport request."));
    public static final Optional<Text> help = Optional.<Text>of(Texts.of(TextColors.AQUA, "Accepts a teleport request."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/tpa [player]");

    public CommandTPAccept(DirectEssentials plugin) {
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
            Optional<Player> accept = plugin.getGame().getServer().getPlayer(args[0]);
            if (!accept.isPresent()) {
                source.sendMessage(Texts.of(TextColors.RED, "Invalid player: ", TextColors.WHITE, args[0]));
                return Optional.of(CommandResult.success());
            }

            EssentialsUser acceptUser = plugin.getEssentialsGame().getOrCreateUser(accept.get().getUniqueId().toString());
            Boolean teleportHere = user.getTpRequests().remove(acceptUser);
            if (teleportHere == null) {
                source.sendMessage(Texts.of(TextColors.WHITE, accept.get().getName(), TextColors.AQUA, " has not requested a teleportation from you."));
                return Optional.of(CommandResult.success());
            }

            acceptUser.setRequestingTeleport(false);
            if (teleportHere) {
                player.setLocation(accept.get().getLocation());
                player.sendMessage(Texts.of(TextColors.AQUA, "You accepted the teleport request, teleported to ", TextColors.WHITE, accept.get().getName()));
                accept.get().sendMessage(Texts.of(TextColors.WHITE, player.getName(), TextColors.AQUA, " accepted your teleport request and was teleported to you."));
            } else {
                accept.get().setLocation(player.getLocation());
                player.sendMessage(Texts.of(TextColors.AQUA, "You accepted the teleport request and ", TextColors.WHITE, accept.get().getName(), TextColors.AQUA, " was teleported to you."));
                accept.get().sendMessage(Texts.of(TextColors.WHITE, player.getName(), TextColors.AQUA, " accepted your teleport request, teleported to ", TextColors.WHITE, player.getName()));
            }
        }
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.tpaccept") || source.hasPermission("directessentials.*");
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
