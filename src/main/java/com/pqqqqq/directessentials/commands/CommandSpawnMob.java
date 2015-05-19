package com.pqqqqq.directessentials.commands;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
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
import org.spongepowered.api.world.World;

/**
 * Created by Kevin on 2015-05-18.
 */
public class CommandSpawnMob implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandSpawnMob(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandSpawnMob(plugin)).description(Texts.of(TextColors.AQUA, "Spawns a mob into the world.")).permission("directessentials.spawnmob")
                .arguments(GenericArguments.catalogedElement(Texts.of("EntityType"), plugin.getGame(), CatalogTypes.ENTITY_TYPE), GenericArguments.optional(GenericArguments.integer(Texts.of("Amount")))).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) src;
        World world = player.getWorld();
        EntityType entityType = args.<EntityType>getOne("EntityType").get();

        // Get amount
        int amount = 1;
        if (args.hasAny("Amount")) {
            amount = args.<Integer>getOne("Amount").get();
        }

        Optional<Entity> entityOptional = world.createEntity(entityType, player.getLocation().getPosition());
        if (!entityOptional.isPresent()) {
            src.sendMessage(Texts.of(TextColors.RED, "Failed to create entity."));
        } else {
            Entity entity = entityOptional.get();

            // Spawn the entity (amount) time(s)
            for (int counter = 0; counter < amount; counter++) {
                world.spawnEntity(entity);
            }
        }
        return CommandResult.success();
    }
}
