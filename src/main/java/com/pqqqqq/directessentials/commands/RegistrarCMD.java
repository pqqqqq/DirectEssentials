package com.pqqqqq.directessentials.commands;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.essentials.CommandEssentials;
import com.pqqqqq.directessentials.commands.region.CommandRegion;
import org.spongepowered.api.service.command.CommandService;

/**
 * Created by Kevin on 2015-05-18.
 * The class that registers all commands.
 */
public class RegistrarCMD {
    private DirectEssentials plugin;

    public RegistrarCMD(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    // Main method
    public void register() {
        // Register commands
        CommandService commandService = plugin.getGame().getCommandDispatcher();

        // Warp commands
        commandService.register(plugin, CommandWarp.build(plugin), "warp");
        commandService.register(plugin, CommandSetWarp.build(plugin), "setwarp", "swarp");
        commandService.register(plugin, CommandDeleteWarp.build(plugin), "deletewarp", "dwarp", "removewarp", "delwarp");

        // Home commands
        commandService.register(plugin, CommandHome.build(plugin), "home");
        commandService.register(plugin, CommandSetHome.build(plugin), "sethome", "shome");
        commandService.register(plugin, CommandDeleteHome.build(plugin), "deletehome", "dhome", "removehome", "delhome");

        // Spawn commands
        commandService.register(plugin, CommandSpawn.build(plugin), "spawn");
        commandService.register(plugin, CommandSetSpawn.build(plugin), "setspawn");

        // Teleport commands
        commandService.register(plugin, CommandTPA.build(plugin), "tpa");
        commandService.register(plugin, CommandTPAccept.build(plugin), "tpaccept");
        commandService.register(plugin, CommandTPAHere.build(plugin), "tpahere");
        commandService.register(plugin, CommandTPO.build(plugin), "tpo", "tp", "teleport", "tele");

        // Region commands
        commandService.register(plugin, CommandRegion.build(plugin), "region", "rg");

        // Essentials main plugin commands
        commandService.register(plugin, CommandEssentials.build(plugin), "essentials", "ess", "de", "directessentials", "dessentials", "dess");

        // Miscellaneous commands
        commandService.register(plugin, CommandMotd.build(plugin), "motd");
        commandService.register(plugin, CommandHat.build(plugin), "hat", "head");
        commandService.register(plugin, CommandHeal.build(plugin), "heal");
        commandService.register(plugin, CommandEat.build(plugin), "feed", "eat");
        commandService.register(plugin, CommandDisplayName.build(plugin), "displayname", "display", "dn", "nick", "nickname");
        commandService.register(plugin, CommandMore.build(plugin), "more");
        commandService.register(plugin, CommandRepair.build(plugin), "repair", "fix");
        commandService.register(plugin, CommandEnchant.build(plugin), "enchant");
        commandService.register(plugin, CommandInvisible.build(plugin), "invis", "invisible", "hide", "vanish", "v");
        commandService.register(plugin, CommandSudo.build(plugin), "sudo", "run");
        commandService.register(plugin, CommandSpawnMob.build(plugin), "spawnmob", "mob");
        commandService.register(plugin, CommandTop.build(plugin), "top");
        commandService.register(plugin, CommandPing.build(plugin), "ping", "pong", "echo");
    }
}
