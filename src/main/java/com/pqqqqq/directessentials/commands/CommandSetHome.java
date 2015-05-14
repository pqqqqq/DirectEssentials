package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.config.Config;
import com.pqqqqq.directessentials.data.Home;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
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
public class CommandSetHome implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSetHome(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().setExecutor(new CommandSetHome(plugin)).setDescription(Texts.of(TextColors.AQUA, "Sets a new home location."))
                .setArguments(GenericArguments.string(Texts.of("HomeName"))).build();
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

        if (user.getHomes().size() >= Config.homesLimit) {
            source.sendMessage(Texts.of(TextColors.RED, "You cannot have more than ", TextColors.WHITE, Config.homesLimit, TextColors.RED, " home(s)."));
            return CommandResult.success();
        }

        String homeName = arguments.<String>getOne("HomeName").get();
        if (homeName.length() > 10) {
            source.sendMessage(Texts.of(TextColors.RED, "Maximum name length is 10 characters for homes."));
            return CommandResult.success();
        }

        if (user.getHomes().contains(homeName)) {
            source.sendMessage(Texts.of(TextColors.RED, "A home already exists with this name."));
            return CommandResult.success();
        }

        Home home = new Home(homeName, player.getUniqueId().toString());
        home.setLocation(player.getLocation());
        user.getHomes().put(homeName, home);

        source.sendMessage(Texts.of(TextColors.GREEN, "Home created successfully."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource source) {
        return source.hasPermission("directessentials.home") || source.hasPermission("directessentials.*");
    }
}
