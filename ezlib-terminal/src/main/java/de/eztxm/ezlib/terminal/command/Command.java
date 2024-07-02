package de.eztxm.ezlib.terminal.command;

public abstract class Command {
    public abstract CommandResponse execute(String command, String[] args);
    public abstract String name();
    public abstract String[] aliases();
    public abstract String description();
}
