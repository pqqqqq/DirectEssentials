package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.Warp;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

/**
 * Created by Kevin on 2015-05-12.
 */
public class CommandWarp extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text> of(Texts.of(TextColors.AQUA, "Warps to a place."));
    public static final Optional<Text> help = Optional.<Text> of(Texts.of(TextColors.AQUA, "Warps to a place."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/warp <warp> [player]");

    public CommandWarp(DirectEssentials plugin) {
        super(plugin);
    }

    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        if (!testPermission(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return Optional.of(CommandResult.success());
        }

        String[] args = arguments.trim().split(" ");
        if (arguments.trim().isEmpty()) {
            // List warps
            TextBuilder builder = Texts.builder("Available warps: ").color(TextColors.AQUA);
            int num = 0;

            for (Warp warp : plugin.getEssentialsGame().getWarps()) {
                if (warp.isPermissible(source)) {
                    builder.append(Texts.builder((num++ > 0 ? ", " : "") + warp.getName()).color(TextColors.WHITE).onClick(TextActions.runCommand("/warp " + warp.getName()))
                            .onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Warp here."))).build());
                }
            }

            if (num == 0) {
                source.sendMessage(Texts.of(TextColors.AQUA, "No warps have been created."));
            } else {
                source.sendMessage(builder.build());
            }
            return Optional.of(CommandResult.success());
        }

        Player warper = null;
        if (args.length > 1) {
            if (!warper.hasPermission("directessentials.otherwarp") && !warper.hasPermission("directessentials.*")) {
                source.sendMessage(Texts.of(TextColors.RED, "You don't have permissions to warp others."));
                return Optional.of(CommandResult.success());
            }

            Optional<Player> wp = plugin.getGame().getServer().getPlayer(args[1]);
            if (!wp.isPresent()) {
                source.sendMessage(Texts.of(TextColors.RED, "Invalid player."));
                return Optional.of(CommandResult.success());
            }
            warper = wp.get();
        } else {
            if (!(source instanceof Player)) {
                source.sendMessage(Texts.of(TextColors.RED, "Either specify a player, or run as one."));
                return Optional.of(CommandResult.success());
            }

            warper = (Player) source;
        }

        Warp warp = plugin.getEssentialsGame().getWarps().get(args[0]);
        if (warp == null) {
            source.sendMessage(Texts.of(TextColors.RED, "Invalid warp: ", TextColors.WHITE, args[0]));
            return Optional.of(CommandResult.success());
        }

        if (!warp.isPermissible(source)) {
            source.sendMessage(Texts.of(TextColors.RED, "You don't have permissions for this warp."));
            return Optional.of(CommandResult.success());
        }

        if (warp.apply(warper)) {
            source.sendMessage(Texts.of(TextColors.GREEN, "Warped successfully."));
        } else {
            source.sendMessage(Texts.of(TextColors.RED, "Could not be warped."));
        }
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource source) {
        return true;
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
