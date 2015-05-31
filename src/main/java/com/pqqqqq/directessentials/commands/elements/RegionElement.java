package com.pqqqqq.directessentials.commands.elements;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.region.Region;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.PatternMatchingCommandElement;

import javax.annotation.Nullable;

/**
 * Created by Kevin on 2015-05-14.
 */
public class RegionElement extends PatternMatchingCommandElement {
    private DirectEssentials plugin;

    protected RegionElement(Text key, DirectEssentials plugin) {
        super(key);
        this.plugin = plugin;
    }

    @Override
    protected Iterable<String> getChoices(CommandSource source) {
        return Iterables.transform(plugin.getEssentialsGame().getRegions().values(), new Function<Region, String>() {
            @Nullable
            public String apply(@Nullable Region input) {
                return input == null ? null : input.getName();
            }
        });
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        Region region = plugin.getEssentialsGame().getRegions().get(choice);
        if (region == null) {
            throw new IllegalArgumentException("Provided argument " + choice + " did not match a Region");
        }
        return region;
    }
}
