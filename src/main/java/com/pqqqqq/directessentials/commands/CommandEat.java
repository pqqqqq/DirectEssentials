package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.data.manipulators.entities.FoodData;
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
public class CommandEat implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandEat(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandEat(plugin)).description(Texts.of(TextColors.AQUA, "Satisfies hunger."))
                .arguments(GenericArguments.playerOrSource(Texts.of("Player"), plugin.getGame())).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!testPermission(src)) {
            src.sendMessage(Texts.of(TextColors.RED, "Insufficient permissions."));
            return CommandResult.success();
        }

        Optional<Player> player = args.<Player>getOne("Player");
        if (!player.isPresent()) {
            src.sendMessage(Texts.of(TextColors.RED, "Specify an online player or run as a player."));
            return CommandResult.success();
        }

        Optional<FoodData> foodData = player.get().getData(FoodData.class);
        if (foodData.isPresent()) {
            FoodData fd = foodData.get();
            fd.setFoodLevel(20);
            player.get().offer(fd);

            src.sendMessage(Texts.of(TextColors.GREEN, "Hunger refilled."));
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource src) {
        return src.hasPermission("directessentials.eat");
    }
}
