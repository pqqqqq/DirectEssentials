package com.pqqqqq.directessentials.commands;

import com.flowpowered.math.vector.Vector3i;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by Kevin on 2015-05-18.
 */
public class CommandTop implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandTop(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandTop(plugin)).description(Texts.of(TextColors.AQUA, "Teleports to highest buildable Y coordinate of the current location.")).permission("directessentials.top").build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) src;
        World world = player.getWorld();

        Vector3i playerLoc = player.getLocation().getBlockPosition();
        Vector3i top = new Vector3i(playerLoc.getX(), world.getBlockMax().getY(), playerLoc.getZ());

        // Set a block there
        world.setBlockType(top, BlockTypes.GLASS);

        // Teleport one up
        player.setLocation(new Location(world, top.add(0, 1, 0)));
        player.sendMessage(Texts.of(TextColors.GREEN, "Up you go!"));
        return CommandResult.success();
    }
}
