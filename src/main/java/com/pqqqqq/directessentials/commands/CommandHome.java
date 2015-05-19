package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.elements.EssentialsArguments;
import com.pqqqqq.directessentials.data.Home;
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

/**
 * Created by Kevin on 2015-05-12.
 */
public class CommandHome implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandHome(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandHome(plugin)).description(Texts.of(TextColors.AQUA, "Teleports to a home."))
                .arguments(GenericArguments.optional(EssentialsArguments.home(Texts.of("HomeName"), plugin))).build();
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

        if (!arguments.hasAny("HomeName")) {
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
            return CommandResult.success();
        }

        String homeName = arguments.<String>getOne("HomeName").get();
        Home home = user.getHomes().get(homeName);

        if (home == null) {
            source.sendMessage(Texts.of(TextColors.RED, "Invalid home: ", TextColors.WHITE, homeName));
            return CommandResult.success();
        }

        if (home.apply(player)) {
            source.sendMessage(Texts.of(TextColors.GREEN, "Teleported home successfully."));
        } else {
            source.sendMessage(Texts.of(TextColors.RED, "Could not be teleported home."));
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.home") || source.hasPermission("directessentials.*");
    }
}
