package com.pqqqqq.directessentials.commands.elements;

import com.pqqqqq.directessentials.DirectEssentials;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.args.CommandElement;

/**
 * Created by Kevin on 2015-05-14.
 */
public class EssentialsArguments {

    public static CommandElement home(Text key, DirectEssentials plugin) {
        return new HomeElement(key, plugin);
    }

    public static CommandElement warp(Text key, DirectEssentials plugin) {
        return new WarpElement(key, plugin);
    }

    public static CommandElement region(Text key, DirectEssentials plugin) {
        return new RegionElement(key, plugin);
    }

    public static CommandElement user(Text key, DirectEssentials plugin) {
        return new EssentialsUserElement(key, plugin);
    }
}
