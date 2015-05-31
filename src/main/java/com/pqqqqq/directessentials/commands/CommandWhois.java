package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.elements.EssentialsArguments;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMessageFormatting;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

/**
 * Created by Kevin on 2015-05-19.
 */
public class CommandWhois implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandWhois(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandWhois(plugin)).description(Texts.of(TextColors.AQUA, "Displays information about a user.")).permission("directessentials.whois")
                .arguments(EssentialsArguments.user(Texts.of("User"), plugin)).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        EssentialsUser user = args.<EssentialsUser>getOne("User").get();
        Optional<Player> playerOptional = user.getPlayer();

        TextBuilder builder = Texts.builder();
        builder.append(Texts.of(TextColors.AQUA, "User: ", TextColors.WHITE, user.getLastCachedUsername()))
                .append(CommandMessageFormatting.NEWLINE_TEXT)
                .append(Texts.of(TextColors.AQUA, "IP: ", TextColors.WHITE, user.getLastCachedIPString()))
                .append(CommandMessageFormatting.NEWLINE_TEXT)
                .append(Texts.of(TextColors.AQUA, "UUID: ", TextColors.WHITE, user.getUuid()))
                .append(CommandMessageFormatting.NEWLINE_TEXT)
                .append(Texts.of(TextColors.AQUA, "Online: ", TextColors.WHITE, playerOptional.isPresent()));

        src.sendMessage(builder.build());
        return CommandResult.success();
    }
}
