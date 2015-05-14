package com.pqqqqq.directessentials.commands.elements;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.Home;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.PatternMatchingCommandElement;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kevin on 2015-05-14.
 */
public class HomeElement extends PatternMatchingCommandElement {
    private DirectEssentials plugin;

    protected HomeElement(Text key, DirectEssentials plugin) {
        super(key);
        this.plugin = plugin;
    }

    @Override
    protected Iterable<String> getChoices(CommandSource source) {
        Collection<Home> homes = new ArrayList<Home>();

        if (source instanceof Player) {
            Player player = (Player) source;
            EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());
            homes = user.getHomes().values();
        }

        return Iterables.transform(homes, new Function<Home, String>() {
            @Nullable
            public String apply(@Nullable Home input) {
                return input == null ? null : input.getName();
            }
        });
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        return choice; // This element is strictly for completion.
    }
}
