package com.pqqqqq.directessentials.commands.region;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.region.Cuboid;
import com.pqqqqq.directessentials.data.region.Region;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
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
 * Created by Kevin on 2015-05-14.
 */
public class CommandCreate implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandCreate(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandCreate(plugin)).description(Texts.of(TextColors.AQUA, "Creates a new region")).permission("directessentials.region.create")
                .arguments(GenericArguments.string(Texts.of("RegionName"))).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Texts.of(TextColors.RED, "Player only command."));
            return CommandResult.success();
        }

        Player player = (Player) src;
        EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());
        String regionName = args.<String>getOne("RegionName").get();

        if (plugin.getEssentialsGame().getRegions().contains(regionName)) {
            src.sendMessage(Texts.of(TextColors.RED, "A region already exists with that name."));
            return CommandResult.success();
        }

        Cuboid selection = user.getRegionSelection();
        if (!selection.isStable()) {
            src.sendMessage(Texts.of(TextColors.RED, "Please use a wooden hoe and select the region you'd like to create."));
            return CommandResult.success();
        }

        Region newRegion = new Region(regionName, player.getUniqueId().toString(), selection);
        plugin.getEssentialsGame().getRegions().put(regionName, newRegion);
        src.sendMessage(Texts.of(TextColors.GREEN, "Region created successfully."));
        return CommandResult.success();
    }
}
