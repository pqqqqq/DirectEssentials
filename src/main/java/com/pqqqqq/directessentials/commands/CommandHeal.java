package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.data.manipulators.entities.HealthData;
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
        return CommandSpec.builder().executor(new CommandHeal(plugin)).description(Texts.of(TextColors.AQUA, "Nurtures a player back to full health."))
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

        Optional<HealthData> healthData = player.get().getData(HealthData.class);
        if (healthData.isPresent()) {
            HealthData hd = healthData.get();
            hd.setHealth(hd.getMaxHealth());
            player.get().offer(hd);

            src.sendMessage(Texts.of(TextColors.GREEN, "Health refilled."));
        }
        return CommandResult.success();
    }

    public boolean testPermission(CommandSource src) {
        return src.hasPermission("directessentials.heal");
    }
}
