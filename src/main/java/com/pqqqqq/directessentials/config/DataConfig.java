package com.pqqqqq.directessentials.config;

import com.pqqqqq.directessentials.DirectEssentials;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.json.FieldValueSeparatorStyle;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;

/**
 * Created by Kevin on 2015-05-12.
 */
public class DataConfig {
    private DirectEssentials plugin;
    private File file;
    private ConfigurationLoader<? extends ConfigurationNode> cfg;

    public DataConfig(DirectEssentials plugin, File file) {
        this.plugin = plugin;
        this.file = file;
    }

    public void init() {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            cfg = JSONConfigurationLoader.builder().setFile(file).setIndent(4).setFieldValueSeparatorStyle(FieldValueSeparatorStyle.SPACE_AFTER).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            ConfigurationNode root = cfg.load();
            plugin.getEssentialsGame().load(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationNode root = cfg.createEmptyNode(ConfigurationOptions.defaults());
            plugin.getEssentialsGame().save(root);
            cfg.save(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
