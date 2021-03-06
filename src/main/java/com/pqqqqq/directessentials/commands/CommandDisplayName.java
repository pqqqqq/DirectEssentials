package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.data.manipulator.DisplayNameData;
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
 * Created by Kevin on 2015-05-15.
 */
public class CommandDisplayName implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandDisplayName(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandDisplayName(plugin)).description(Texts.of(TextColors.AQUA, "Changes a player's display name.")).permission("directessentials.displayname")
                .arguments(GenericArguments.string(Texts.of("DisplayName")), GenericArguments.playerOrSource(Texts.of("Player"), plugin.getGame())).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = args.<Player>getOne("Player").get();
        String displayName = args.<String>getOne("DisplayName").get();

        Optional<DisplayNameData> displayNameData = player.getOrCreate(DisplayNameData.class);
        if (displayNameData.isPresent()) {
            DisplayNameData dnd = displayNameData.get();
            player.offer(dnd.setDisplayName(Texts.of(displayName)));

            src.sendMessage(Texts.of(TextColors.GREEN, "Display name set."));
        }
        return CommandResult.success();
    }
}
