package com.pqqqqq.directessentials;

import com.google.inject.Inject;
import com.pqqqqq.directessentials.commands.RegistrarCMD;
import com.pqqqqq.directessentials.config.Config;
import com.pqqqqq.directessentials.config.DataConfig;
import com.pqqqqq.directessentials.events.CoreEvents;
import com.pqqqqq.directessentials.events.ProtectionEvents;
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
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.event.EventManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 2015-05-11.
 */
@Plugin(id = DirectEssentials.ID, name = DirectEssentials.NAME, version = DirectEssentials.VERSION)
public class DirectEssentials {
    public static final String ID = "directessentials";
    public static final String NAME = "DirectEssentials";
    public static final String VERSION = "0.2 BETA";

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
        eventManager.register(this, new ProtectionEvents(this));

        // Register commands
        new RegistrarCMD(this).register();
        
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
