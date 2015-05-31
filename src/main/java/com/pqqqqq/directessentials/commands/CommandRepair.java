package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.data.manipulator.item.DurabilityData;
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
public class CommandRepair implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandRepair(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandRepair(plugin)).description(Texts.of(TextColors.AQUA, "Repairs the item in hand.")).permission("directessentials.repair").build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) src;
        Optional<ItemStack> handOptional = player.getItemInHand();

        if (!handOptional.isPresent()) {
            player.sendMessage(Texts.of(TextColors.RED, "Repairing air wouldn't be very useful."));
            return CommandResult.success();
        }

        ItemStack hnd = handOptional.get();
        Optional<DurabilityData> durabilityDataOptional = hnd.getOrCreate(DurabilityData.class);

        if (durabilityDataOptional.isPresent()) {
            DurabilityData durabilityData = durabilityDataOptional.get();
            durabilityData.setDurability(durabilityData.getMaxValue());
            hnd.offer(durabilityData);

            player.sendMessage(Texts.of(TextColors.GREEN, "Item repaired successfully."));
        }
        return CommandResult.success();
    }
}
