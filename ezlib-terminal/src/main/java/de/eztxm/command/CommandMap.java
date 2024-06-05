package de.eztxm.command;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {
    private Map<String, Command> commandMap;

    public CommandMap(Map<String, Command> commandMap) {
        this.commandMap = new HashMap<>();
    }

    public CommandMap() {}

    public void register(Command... commands) {
        for (Command command : commands) {
            this.register(command);
        }
    }

    public void register(Command command) {
        this.commandMap.put(command.name(), command);
    }

    public void unregister(String... commandNames) {
        for (String name : commandNames) {
            this.unregister(name);
        }
    }

    public void unregister(String commandName) {
        this.commandMap.forEach((name, command) -> {
            if(name.equalsIgnoreCase(commandName))
                this.commandMap.remove(commandName);
        });
    }

    public Map<String, Command> getCommands() {
        return this.commandMap;
    }
}
