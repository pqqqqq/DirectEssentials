package com.pqqqqq.directessentials.config;

import com.pqqqqq.directessentials.DirectEssentials;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;

/**
 * Created by Kevin on 2015-05-12.
 */
public class Config {
    private DirectEssentials plugin;
    private File file;
    private ConfigurationLoader<CommentedConfigurationNode> cfg;

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


            cfg.save(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
