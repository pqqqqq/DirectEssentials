package com.pqqqqq.directessentials.commands.region;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.elements.EssentialsArguments;
import com.pqqqqq.directessentials.data.region.Region;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
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
 * Created by Kevin on 2015-05-20.
 */
public class CommandSetOwner implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSetOwner(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandSetOwner(plugin)).description(Texts.of(TextColors.AQUA, "Sets the owner of a region."))
                .arguments(EssentialsArguments.region(Texts.of("Region"), plugin), EssentialsArguments.user(Texts.of("User"), plugin)).build();
    }

    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        if (!(commandSource instanceof Player)) {
            commandSource.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) commandSource;
        EssentialsUser add = commandContext.<EssentialsUser>getOne("User").get();
        Region region = commandContext.<Region>getOne("Region").get();

        if (region.getOwner() != null && !region.getOwner().equals(player.getUniqueId().toString()) && !player.hasPermission("directessentials.region.setowner.override")) {
            commandSource.sendMessage(Texts.of(TextColors.RED, "You cannot set the owner of someone else's region."));
            return CommandResult.success();
        }

        if (region.getOwner() != null && region.getOwner().equalsIgnoreCase(add.getUuid())) {
            commandSource.sendMessage(Texts.of(TextColors.RED, "This player is already the owner."));
            return CommandResult.success();
        }

        region.setOwner(add.getUuid());
        commandSource.sendMessage(Texts.of(TextColors.GREEN, "Player set as owner successfully."));
        add.sendMessage(Texts.of(TextColors.AQUA, "You've been set as the owner for the region: ", TextColors.WHITE, region.getName()));
        return CommandResult.success();
    }
}
