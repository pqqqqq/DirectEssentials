package com.pqqqqq.directessentials.util;

import org.spongepowered.api.text.Texts;

/**
 * Created by Kevin on 2015-05-15.
 */
public class Utilities {

    @SuppressWarnings("deprecation")
    public static String formatColour(String str) {
        if (str == null) {
            return null;
        }

        return str.replaceAll("&([0-9a-fA-FkKlLmMnNoOrR])", Texts.getLegacyChar() + "$1");
    }

    @SuppressWarnings("deprecation")
    public static String unformatColour(String str) {
        if (str == null) {
            return null;
        }

        return str.replaceAll(Texts.getLegacyChar() + "([0-9a-fA-FkKlLmMnNoOrR])", "&$1");
    }
}
