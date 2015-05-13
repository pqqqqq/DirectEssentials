package com.pqqqqq.directessentials.config;

import com.google.common.base.Function;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.commands.config.EventCommand;
import com.pqqqqq.directessentials.wrappers.WeakEssentialsMap;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by Kevin on 2015-05-12.
 */
public class Config {
    private DirectEssentials plugin;
    private File file;
    private ConfigurationLoader<CommentedConfigurationNode> cfg;

    public static int homesLimit;

    public static WeakEssentialsMap<String, EventCommand> commands = new WeakEssentialsMap<String, EventCommand>();

    public Config(DirectEssentials plugin, File file, ConfigurationLoader<CommentedConfigurationNode> cfg) {
        this.plugin = plugin;
        this.file = file;
        this.cfg = cfg;
    }

    public void init() {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            CommentedConfigurationNode root = cfg.load();

            CommentedConfigurationNode users = root.getNode("users");
            homesLimit = users.getNode("homes", "limit").getInt(3);

            commands.clear();
            CommentedConfigurationNode commandNode = root.getNode("commands");
            for (CommentedConfigurationNode command : commandNode.getChildrenMap().values()) {
                EventCommand eventCommand = new EventCommand(command.getKey().toString());
                eventCommand.setTimerSeconds(command.getNode("delay-seconds").getInt(-1));
                eventCommand.setShortcuts(command.getNode("shortcuts").getList(new Function<Object, String>() {

                    @Nullable
                    public String apply(Object input) {
                        return input == null ? null : input.toString();
                    }
                }));

                commands.put(command.getKey().toString(), eventCommand);
            }

            cfg.save(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
