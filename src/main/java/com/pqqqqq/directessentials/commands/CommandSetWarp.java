package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.Warp;
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
 * Created by Kevin on 2015-05-12.
 */
public class CommandSetWarp implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSetWarp(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandSetWarp(plugin)).description(Texts.of(TextColors.AQUA, "Sets a new server warp."))
                .arguments(GenericArguments.string(Texts.of("WarpName"))).build();
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

        String warpName = arguments.<String>getOne("WarpName").get();
        if (plugin.getEssentialsGame().getWarps().contains(warpName)) {
            source.sendMessage(Texts.of(TextColors.RED, "A warp already exists with this name."));
            return CommandResult.success();
        }

        Warp warp = new Warp(warpName);
        warp.setLocation(player.getLocation());
        plugin.getEssentialsGame().getWarps().put(warpName, warp);

        source.sendMessage(Texts.of(TextColors.GREEN, "Warp created successfully."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.setwarp") || source.hasPermission("directessentials.*");
    }
}
