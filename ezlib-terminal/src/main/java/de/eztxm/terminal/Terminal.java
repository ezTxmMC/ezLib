package de.eztxm.terminal;

import de.eztxm.logger.LoggerColor;
import de.eztxm.logger.SimpleLogger;
import de.eztxm.terminal.command.CommandMap;
import de.eztxm.terminal.util.JavaColor;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

public class Terminal {
    private final SimpleLogger logger;
    private final Scanner scanner;
    @Getter @Setter
    private String prompt;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private CommandMap commandMap;

    public Terminal(String name, String prompt) {
        this.logger = new SimpleLogger();
        this.scanner = new Scanner(System.in);
        this.name = name;
        this.prompt = prompt;
        this.commandMap = new CommandMap();
    }

    public Terminal(String name, CommandMap commandMap) {
        this.logger = new SimpleLogger();
        this.scanner = new Scanner(System.in);
        this.name = name;
        this.prompt = JavaColor.apply("&9ezLib&8>&7");
        this.commandMap = commandMap;
    }

    public Terminal(String name, String prompt, CommandMap commandMap) {
        this.logger = new SimpleLogger();
        this.scanner = new Scanner(System.in);
        this.name = name;
        this.prompt = prompt;
        this.commandMap = commandMap;
    }

    public Terminal(String name) {
        this.logger = new SimpleLogger();
        this.scanner = new Scanner(System.in);
        this.name = name;
        this.prompt = JavaColor.apply("&9ezLib &8» &7");
        this.commandMap = new CommandMap();
    }

    public void start() {
        handleTerminal();
    }

    public void writeLine(String prefix, String message) {
        logger.custom(JavaColor.apply(prefix + message));
        System.out.flush();
    }

    public void writeLine(String message) {
        logger.custom(JavaColor.apply(message));
        System.out.flush();
    }

    public void writeLineColored(String hex, String message) {
        logger.custom(JavaColor.colored(hex, message));
        System.out.flush();
    }

    public void writeLineColored(LoggerColor color, String message) {
        logger.custom(color, message);
        System.out.flush();
    }

    public void emptyLine() {
        System.out.println("\n");
        System.out.flush();
    }

    public void handleTerminal() {
        Thread thread = new Thread(() -> {
            while (Thread.currentThread().isAlive()) {
                System.out.print(prompt);
                if(scanner.hasNext()) {
                    String rawCommand = scanner.nextLine().trim();
                    String[] splitRawCommand = rawCommand.split(" ");
                    commandMap.getCommands().forEach((commandName, command) -> {
                        if(splitRawCommand[0].equalsIgnoreCase(commandName)) {
                            splitRawCommand[0] = "";
                            command.execute(commandName, splitRawCommand);
                            return;
                        }
                        for(String alias : command.aliases()) {
                            if(splitRawCommand[0].equalsIgnoreCase(alias)) {
                                splitRawCommand[0] = "";
                                command.execute(commandName, splitRawCommand);
                                return;
                            }
                        }
                    });
                }
            }
        }, name);
        thread.start();
    }
}
