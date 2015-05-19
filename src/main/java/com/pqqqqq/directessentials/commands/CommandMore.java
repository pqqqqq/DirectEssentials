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
public class CommandMore implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandMore(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandMore(plugin)).description(Texts.of(TextColors.AQUA, "Gets more of an item.")).build();
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
        Optional<ItemStack> handOptional = player.getItemInHand();

        if (!handOptional.isPresent()) {
            player.sendMessage(Texts.of(TextColors.RED, "Getting more air wouldn't be very useful."));
            return CommandResult.success();
        }

        ItemStack hnd = handOptional.get();
        if (hnd.getQuantity() >= hnd.getMaxStackQuantity()) {
            player.getInventory().offer(hnd);
        } else {
            hnd.setQuantity(hnd.getMaxStackQuantity());
        }

        player.sendMessage(Texts.of(TextColors.GREEN, "More ", TextColors.WHITE, hnd.getItem().getName(), TextColors.GREEN, " coming right up."));
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource src) {
        return src.hasPermission("directessentials.more");
    }
}
