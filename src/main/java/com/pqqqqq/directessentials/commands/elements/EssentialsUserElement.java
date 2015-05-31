package com.pqqqqq.directessentials.commands.elements;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.PatternMatchingCommandElement;

import javax.annotation.Nullable;

/**
 * Created by Kevin on 2015-05-19.
 */
public class EssentialsUserElement extends PatternMatchingCommandElement {
    private DirectEssentials plugin;

    protected EssentialsUserElement(Text key, DirectEssentials plugin) {
        super(key);
        this.plugin = plugin;
    }

    @Override
    protected Iterable<String> getChoices(CommandSource source) {
        return Iterables.transform(plugin.getEssentialsGame().getUsers().values(), new Function<EssentialsUser, String>() {
            @Nullable
            public String apply(@Nullable EssentialsUser input) {
                return input == null ? null : input.getLastCachedUsername();
            }
        });
    }

    @Override
    protected Object getValue(String choice) throws IllegalArgumentException {
        for (EssentialsUser user : plugin.getEssentialsGame().getUsers()) {
            if (user.getLastCachedUsername() != null && user.getLastCachedUsername().equalsIgnoreCase(choice)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Provided argument " + choice + " did not match a User");
    }
}
