package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.data.manipulator.entity.HealthData;
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
public class CommandHeal implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandHeal(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandHeal(plugin)).description(Texts.of(TextColors.AQUA, "Nurtures a player back to full health.")).permission("directessentials.heal")
                .arguments(GenericArguments.playerOrSource(Texts.of("Player"), plugin.getGame())).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = args.<Player>getOne("Player").get();
        Optional<HealthData> healthData = player.getOrCreate(HealthData.class);

        if (healthData.isPresent()) {
            HealthData hd = healthData.get();
            player.offer(hd.setHealth(hd.getMaxHealth()));

            src.sendMessage(Texts.of(TextColors.GREEN, "Health refilled."));
        }
        return CommandResult.success();
    }
}
