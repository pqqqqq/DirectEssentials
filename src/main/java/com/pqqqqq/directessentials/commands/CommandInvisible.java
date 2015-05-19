package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.data.manipulators.entities.InvisibilityData;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

/**
 * Created by Kevin on 2015-05-15.
 */
public class CommandInvisible implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandInvisible(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandInvisible(plugin)).description(Texts.of(TextColors.AQUA, "Goes invisible to some players.")).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!testPermission(src)) {
            src.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        if (!(src instanceof Player)) {
            src.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) src;
        EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());

        Optional<InvisibilityData> invisibilityDataOptional = player.getData(InvisibilityData.class);
        if (invisibilityDataOptional.isPresent()) {
            InvisibilityData invisibilityData = invisibilityDataOptional.get();

            if (user.isInvisible()) {
                for (Player online : plugin.getGame().getServer().getOnlinePlayers()) {
                    if (!online.equals(player) && invisibilityData.isInvisibleTo(online)) {
                        invisibilityData.setInvisibleTo(online, false);
                    }
                }

                player.sendMessage(Texts.of(TextColors.GREEN, "You reappeared."));
                user.setInvisible(false);
            } else {
                for (Player online : plugin.getGame().getServer().getOnlinePlayers()) {
                    if (!online.equals(player) && !online.hasPermission("directessentials.invisible.override")) {
                        invisibilityData.setInvisibleTo(online, true);
                    }
                }

                player.sendMessage(Texts.of(TextColors.GREEN, "You're now a ghost."));
                user.setInvisible(true);
            }

            player.offer(invisibilityData);
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource src) {
        return src.hasPermission("directessentials.invisible");
    }
}
