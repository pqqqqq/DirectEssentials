package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.Home;
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
 * Created by Kevin on 2015-05-12.
 */
public class CommandHome extends CommandBase {
    public static final Optional<Text> desc = Optional.<Text>of(Texts.of(TextColors.AQUA, "Teleports to a home."));
    public static final Optional<Text> help = Optional.<Text>of(Texts.of(TextColors.AQUA, "Teleports to a home."));
    public static final Text usage = Texts.of(TextColors.AQUA, "/home <name>");

    public CommandHome(DirectEssentials plugin) {
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
            // List warps
            TextBuilder builder = Texts.builder("Available homes: ").color(TextColors.AQUA);
            int num = 0;

            for (Home home : user.getHomes()) {
                builder.append(Texts.builder((num++ > 0 ? ", " : "") + home.getName()).color(TextColors.WHITE).onClick(TextActions.runCommand("/home " + home.getName()))
                        .onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Teleport to this home."))).build());
            }

            if (num == 0) {
                source.sendMessage(Texts.of(TextColors.AQUA, "You have not created any homes."));
            } else {
                source.sendMessage(builder.build());
            }
            return Optional.of(CommandResult.success());
        }

        Home home = user.getHomes().get(args[0]);
        if (home == null) {
            source.sendMessage(Texts.of(TextColors.RED, "Invalid home: ", TextColors.WHITE, args[0]));
            return Optional.of(CommandResult.success());
        }

        if (home.apply(player)) {
            source.sendMessage(Texts.of(TextColors.GREEN, "Teleported home successfully."));
        } else {
            source.sendMessage(Texts.of(TextColors.RED, "Could not be teleported home."));
        }
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.home") || source.hasPermission("directessentials.*");
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
