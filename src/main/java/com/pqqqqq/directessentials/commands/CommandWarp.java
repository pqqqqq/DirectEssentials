package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.elements.EssentialsArguments;
import com.pqqqqq.directessentials.data.Warp;
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

/**
 * Created by Kevin on 2015-05-12.
 */
public class CommandWarp implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandWarp(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandWarp(plugin)).description(Texts.of(TextColors.AQUA, "Warp to a destination."))
                .arguments(GenericArguments.optional(EssentialsArguments.warp(Texts.of("WarpName"), plugin)), GenericArguments.playerOrSource(Texts.of("Warper"), plugin.getGame())).build();
    }

    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (!arguments.hasAny("WarpName")) {
            // List warps
            TextBuilder builder = Texts.builder("Available warps: ").color(TextColors.AQUA);
            int num = 0;

            for (Warp warp : plugin.getEssentialsGame().getWarps()) {
                if (source.hasPermission("directessentials.warp." + warp.getName()) || source.hasPermission("directessentials.warp.*")) {
                    builder.append(Texts.builder((num++ > 0 ? ", " : "") + warp.getName()).color(TextColors.WHITE).onClick(TextActions.runCommand("/warp " + warp.getName()))
                            .onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Warp here."))).build());
                }
            }

            if (num == 0) {
                source.sendMessage(Texts.of(TextColors.AQUA, "No warps have been created."));
            } else {
                source.sendMessage(builder.build());
            }
            return CommandResult.success();
        }

        Player warper = arguments.<Player>getOne("Warper").get();

        String warpName = arguments.<String>getOne("WarpName").get();
        Warp warp = plugin.getEssentialsGame().getWarps().get(warpName);
        if (warp == null) {
            source.sendMessage(Texts.of(TextColors.RED, "Invalid warp: ", TextColors.WHITE, warpName));
            return CommandResult.success();
        }

        if (!source.hasPermission("directessentials.warp." + warp.getName()) && !source.hasPermission("directessentials.warp.*")) {
            source.sendMessage(Texts.of(TextColors.RED, "You don't have permissions for this warp."));
            return CommandResult.success();
        }

        if (warp.apply(warper)) {
            source.sendMessage(Texts.of(TextColors.GREEN, "Warped successfully."));
        } else {
            source.sendMessage(Texts.of(TextColors.RED, "Could not be warped."));
        }
        return CommandResult.success();
    }
}
