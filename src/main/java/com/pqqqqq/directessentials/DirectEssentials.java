package com.pqqqqq.directessentials;

import com.google.inject.Inject;
import com.pqqqqq.directessentials.commands.*;
import com.pqqqqq.directessentials.commands.essentials.CommandReload;
import com.pqqqqq.directessentials.commands.essentials.CommandSave;
import com.pqqqqq.directessentials.config.Config;
import com.pqqqqq.directessentials.config.DataConfig;
import com.pqqqqq.directessentials.events.CoreEvents;
import com.pqqqqq.directessentials.wrappers.game.EssentialsGame;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.util.command.dispatcher.SimpleDispatcher;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 2015-05-11.
 */
@Plugin(id = DirectEssentials.ID, name = DirectEssentials.NAME, version = DirectEssentials.VERSION)
public class DirectEssentials {
    public static final String ID = "directessentials";
    public static final String NAME = "DirectEssentials";
    public static final String VERSION = "0.1 BETA";

    private Game game;

    private EssentialsGame essentialsGame;
    private DataConfig dcfg;
    private Config cfg;

    public static DirectEssentials plugin;

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File configFile;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Inject
    public DirectEssentials(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void init(InitializationEvent event) {
        plugin = this;
        game = event.getGame();

        // Register events
        EventManager eventManager = game.getEventManager();
        eventManager.register(this, new CoreEvents(this));

        // Register commands
        CommandService commandService = game.getCommandDispatcher();

        // Warp commands
        commandService.register(this, CommandWarp.build(this), "warp");
        commandService.register(this, CommandSetWarp.build(this), "setwarp", "swarp");
        commandService.register(this, CommandDeleteWarp.build(this), "deletewarp", "dwarp", "removewarp", "delwarp");

        // Home commands
        commandService.register(this, CommandHome.build(this), "home");
        commandService.register(this, CommandSetHome.build(this), "sethome", "shome");
        commandService.register(this, CommandDeleteHome.build(this), "deletehome", "dhome", "removehome", "delhome");

        // Spawn commands
        commandService.register(this, CommandSpawn.build(this), "spawn");
        commandService.register(this, CommandSetSpawn.build(this), "setspawn");

        // Teleport commands
        commandService.register(this, CommandTPA.build(this), "tpa");
        commandService.register(this, CommandTPAccept.build(this), "tpaccept");
        commandService.register(this, CommandTPAHere.build(this), "tpahere");
        commandService.register(this, CommandTPO.build(this), "tpo", "tp", "teleport", "tele");

        // Essentials main plugin commands
        SimpleDispatcher essentialsCommand = new SimpleDispatcher();
        essentialsCommand.register(CommandSave.build(this), "save");
        essentialsCommand.register(CommandReload.build(this), "reload");

        commandService.register(this, essentialsCommand, "essentials", "ess", "de", "directessentials", "dessentials", "dess");

        // Instantiate managers
        essentialsGame = new EssentialsGame(game);
    }

    @Subscribe
    public void started(ServerStartedEvent event) {
        // Main config
        cfg = new Config(this, configFile, configLoader);
        cfg.init();
        cfg.load();

        // Data config
        dcfg = new DataConfig(this, new File(configFile.getParentFile() + "/data.json"));
        dcfg.init();
        dcfg.load();

        // Start async saver task
        game.getAsyncScheduler().runRepeatingTaskAfter(this, new EssentialsGame.SaveTask(), TimeUnit.SECONDS, Config.saverTaskSeconds, Config.saverTaskSeconds);
    }

    @Subscribe
    public void stopping(ServerStoppingEvent event) {
        dcfg.save();
    }

    public Logger getLogger() {
        return logger;
    }

    public Game getGame() {
        return game;
    }

    public Config getConfig() {
        return cfg;
    }

    public DataConfig getDataConfig() {
        return dcfg;
    }

    public EssentialsGame getEssentialsGame() {
        return essentialsGame;
    }
}
