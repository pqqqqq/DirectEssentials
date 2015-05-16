package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
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
public class CommandHat implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandHat(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().setExecutor(new CommandHat(plugin)).setDescription(Texts.of(TextColors.AQUA, "A wardrobe change")).build();
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
        Optional<ItemStack> hand = player.getItemInHand();

        if (!hand.isPresent()) {
            src.sendMessage(Texts.of(TextColors.RED, "You can't do that, you air-head."));
            return CommandResult.success();
        }

        player.setHelmet(hand.get());
        player.sendMessage(Texts.of(TextColors.GREEN, "You've prepared for war with your ", TextColors.WHITE, hand.get().getItem().getName(), TextColors.GREEN, " hat."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource src) {
        return src.hasPermission("directessentials.hat");
    }
}
