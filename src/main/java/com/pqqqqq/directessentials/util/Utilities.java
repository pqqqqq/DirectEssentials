package com.pqqqqq.directessentials.util;

import org.spongepowered.api.text.Texts;

/**
 * Created by Kevin on 2015-05-15.
 */
public class Utilities {

    // TODO: Revisit formatting colours, as the latest builds of Sponge broke these commands.

    @SuppressWarnings("deprecation")
    public static String formatColour(String str) {
        if (str == null) {
            return null;
        }

        return Texts.replaceCodes(str, '&', Texts.getLegacyChar());
    }

    @SuppressWarnings("deprecation")
    public static String unformatColour(String str) {
        if (str == null) {
            return null;
        }

        return Texts.replaceCodes(str, Texts.getLegacyChar(), '&');
    }

    @SuppressWarnings("deprecation")
    public static String stripColour(String str) {
        if (str == null) {
            return null;
        }

        return Texts.stripCodes(str);
    }
}
