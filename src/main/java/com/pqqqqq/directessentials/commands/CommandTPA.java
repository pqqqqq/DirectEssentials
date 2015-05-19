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

import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 2015-05-13.
 */
public class CommandTPA implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandTPA(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandTPA(plugin)).description(Texts.of(TextColors.AQUA, "Requests a teleport from a player."))
                .arguments(GenericArguments.player(Texts.of("Player"), plugin.getGame())).build();
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

        final Player player = (Player) source;
        final EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());

        Optional<Player> request = arguments.<Player>getOne("Player");
        if (!request.isPresent()) {
            source.sendMessage(Texts.of(TextColors.RED, "This player is not currently online."));
            return CommandResult.success();
        }

        if (!request.get().hasPermission("directessentials.tpaccept") && !request.get().hasPermission("directessentials.*")) {
            source.sendMessage(Texts.of(TextColors.RED, "This player cannot accept teleport requests."));
            return CommandResult.success();
        }

        final EssentialsUser tpRequestUser = plugin.getEssentialsGame().getOrCreateUser(request.get().getUniqueId().toString());
        if (tpRequestUser.isRequestingTeleport()) {
            source.sendMessage(Texts.of(TextColors.RED, "You have a pending teleportation request."));
            return CommandResult.success();
        }

        user.setRequestingTeleport(true);
        tpRequestUser.getTpRequests().put(user, false);
        player.sendMessage(Texts.of(TextColors.AQUA, "Successfully requested a teleport from ", TextColors.WHITE, request.get().getName()));

        TextBuilder requestText = Texts.builder().onClick(TextActions.runCommand("/tpaccept " + player.getName())).onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Accept this teleport request.")));
        requestText.append(Texts.of(TextColors.WHITE, player.getName()));
        requestText.append(Texts.of(TextColors.AQUA, " has requested a teleport to you."));

        request.get().sendMessage(requestText.build());

        // Start a timeout schedule
        // TODO: Change to a sync scheduler when fixed, but async is fine for now.
        plugin.getGame().getAsyncScheduler().runTaskAfter(plugin, new Runnable() {

            public void run() {
                user.setRequestingTeleport(false);
                tpRequestUser.getTpRequests().remove(user);
                player.sendMessage(Texts.of(TextColors.AQUA, "Your teleport request timed out."));
            }
        }, TimeUnit.SECONDS, 10);
        /*plugin.getGame().getSyncScheduler().runTaskAfter(plugin, new Runnable() {
            public void run() {
                if (user.isRequestingTeleport()) {
                    user.setRequestingTeleport(false);
                    tpRequestUser.getTpRequests().remove(user);
                    player.sendMessage(Texts.of(TextColors.AQUA, "Your teleport request timed out."));
                }
            }
        }, 200);*/
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.tpa") || source.hasPermission("directessentials.*");
    }
}
