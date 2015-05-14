package com.pqqqqq.directessentials.commands.elements;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.Warp;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.PatternMatchingCommandElement;

import javax.annotation.Nullable;

/**
 * Created by Kevin on 2015-05-14.
 */
public class WarpElement extends PatternMatchingCommandElement {
    private DirectEssentials plugin;

    protected WarpElement(Text key, DirectEssentials plugin) {
        super(key);
        this.plugin = plugin;
    }

    @Override
    protected Iterable<String> getChoices(CommandSource source) {
        return Iterables.transform(plugin.getEssentialsGame().getWarps().values(), new Function<Warp, String>() {
            @Nullable
            public String apply(@Nullable Warp input) {
                return input == null ? null : input.getName();
            }
        });
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        return choice; // This element is strictly for completion.
    }
}
