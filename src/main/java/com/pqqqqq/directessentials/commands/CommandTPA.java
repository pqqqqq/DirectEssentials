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

/**
 * Created by Kevin on 2015-05-13.
 */
public class CommandTPA extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text>of(Texts.of(TextColors.AQUA, "Requests a teleport."));
    public static final Optional<Text> help = Optional.<Text>of(Texts.of(TextColors.AQUA, "Requests a teleport."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/tpa <player>");

    public CommandTPA(DirectEssentials plugin) {
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

        final Player player = (Player) source;
        final EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());

        String[] args = arguments.trim().split(" ");
        if (arguments.trim().isEmpty()) {
            source.sendMessage(getUsage(player));
            return Optional.of(CommandResult.success());
        }

        Optional<Player> request = plugin.getGame().getServer().getPlayer(args[0]);
        if (!request.isPresent()) {
            source.sendMessage(Texts.of(TextColors.RED, "Invalid player: ", TextColors.WHITE, args[0]));
            return Optional.of(CommandResult.success());
        }

        if (!request.get().hasPermission("directessentials.tpaccept") && !request.get().hasPermission("directessentials.*")) {
            source.sendMessage(Texts.of(TextColors.RED, "This player cannot accept teleport requests."));
            return Optional.of(CommandResult.success());
        }

        final EssentialsUser tpRequestUser = plugin.getEssentialsGame().getOrCreateUser(request.get().getUniqueId().toString());
        if (tpRequestUser.isRequestingTeleport()) {
            source.sendMessage(Texts.of(TextColors.RED, "You have a pending teleportation request."));
            return Optional.of(CommandResult.success());
        }

        user.setRequestingTeleport(true);
        tpRequestUser.getTpRequests().put(user, false);
        player.sendMessage(Texts.of(TextColors.AQUA, "Successfully requested a teleport from ", TextColors.WHITE, request.get().getName()));

        TextBuilder requestText = Texts.builder().onClick(TextActions.runCommand("/tpaccept " + player.getName())).onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Accept this teleport request.")));
        requestText.append(Texts.of(TextColors.WHITE, player.getName()));
        requestText.append(Texts.of(TextColors.AQUA, " has requested a teleport to you."));

        request.get().sendMessage(requestText.build());

        final long st = System.currentTimeMillis();
        // Start a timeout schedule
        plugin.getGame().getSyncScheduler().runTaskAfter(plugin, new Runnable() {
            public void run() {
                if (user.isRequestingTeleport()) {
                    user.setRequestingTeleport(false);
                    tpRequestUser.getTpRequests().remove(user);
                    player.sendMessage(Texts.of(TextColors.AQUA, "Your teleport request timed out."));
                    player.sendMessage(Texts.of(TextColors.AQUA, (System.currentTimeMillis() - st))); // Tentative, remove when this is fixed.
                }
            }
        }, 200);
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.tpa") || source.hasPermission("directessentials.*");
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
