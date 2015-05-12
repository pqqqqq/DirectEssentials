package com.pqqqqq.directessentials.user;

import com.pqqqqq.directessentials.util.MappedManager;

/**
 * Created by Kevin on 2015-05-11.
 */
public class EssentialsUser {
    private final String uuid;

    public EssentialsUser(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public static class Manager extends MappedManager<String, EssentialsUser> {
    }
}
