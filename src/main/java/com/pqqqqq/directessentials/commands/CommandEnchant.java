package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.data.manipulators.items.EnchantmentData;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
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
public class CommandEnchant implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandEnchant(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandEnchant(plugin)).description(Texts.of(TextColors.AQUA, "Enchants the item in hand."))
                .arguments(GenericArguments.catalogedElement(Texts.of("Enchantment"), plugin.getGame(), CatalogTypes.ENCHANTMENT), GenericArguments.optional(GenericArguments.integer(Texts.of("Level")))).build();
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
            player.sendMessage(Texts.of(TextColors.RED, "Enchanting air wouldn't be very useful."));
            return CommandResult.success();
        }

        ItemStack hnd = handOptional.get();
        Enchantment enchantment = args.<Enchantment>getOne("Enchantment").get();
        int level = 0;

        if (args.hasAny("Level")) {
            level = args.<Integer>getOne("Level").get();
        }

        Optional<EnchantmentData> enchantmentDataOptional = hnd.getData(EnchantmentData.class);
        if (enchantmentDataOptional.isPresent()) {
            EnchantmentData enchantmentData = enchantmentDataOptional.get();
            enchantmentData.set(enchantment, level);
            hnd.offer(enchantmentData);
            player.sendMessage(Texts.of(TextColors.GREEN, "Enchantment applied successfully."));
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource src) {
        return src.hasPermission("directessentials.enchant");
    }
}
