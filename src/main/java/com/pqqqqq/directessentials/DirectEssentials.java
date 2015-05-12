package com.pqqqqq.directessentials;

import com.google.inject.Inject;
import com.pqqqqq.directessentials.commands.CommandTPA;
import com.pqqqqq.directessentials.events.CoreEvents;
import com.pqqqqq.directessentials.user.EssentialsUser;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.event.EventManager;

/**
 * Created by Kevin on 2015-05-11.
 */
@Plugin(id = DirectEssentials.ID, name = DirectEssentials.NAME, version = DirectEssentials.VERSION)
public class DirectEssentials {
    public static final String ID = "directessentials";
    public static final String NAME = "DirectEssentials";
    public static final String VERSION = "0.1 BETA";

    private Game game;

    private EssentialsUser.Manager users;

    @Inject
    private Logger logger;

    @Inject
    public DirectEssentials(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void init(InitializationEvent event) {
        game = event.getGame();

        // Register events
        EventManager eventManager = game.getEventManager();
        eventManager.register(this, new CoreEvents(this));

        // Register commands
        CommandService commandService = game.getCommandDispatcher();
        commandService.register(this, new CommandTPA(this));

        // Instantiate managers
        users = new EssentialsUser.Manager();
    }

    public Logger getLogger() {
        return logger;
    }

    public EssentialsUser.Manager getUserManager() {
        return users;
    }
}
