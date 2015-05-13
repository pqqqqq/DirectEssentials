package com.pqqqqq.directessentials.commands.config;

import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2015-05-12.
 */
public class EventCommand implements IWeakValue {
    private String command;

    private int timerSeconds;
    private List<String> shortcuts = new ArrayList<String>();

    public EventCommand() {
    }

    public EventCommand(String command) {
        this.init(command);
    }

    public String getCommand() {
        return command;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public void setTimerSeconds(int timerSeconds) {
        this.timerSeconds = timerSeconds;
    }

    public List<String> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(List<String> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public void init(Object... args) {
        this.command = (String) args[0];
    }
}
