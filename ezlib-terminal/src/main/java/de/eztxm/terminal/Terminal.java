package de.eztxm.terminal;

import de.eztxm.terminal.command.CommandMap;
import de.eztxm.terminal.util.JavaColor;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

public class Terminal {
    private final Scanner scanner;
    @Getter @Setter
    private String prompt;
    @Getter @Setter
    private CommandMap commandMap;

    public Terminal(String prompt) {
        this.scanner = new Scanner(System.in);
        this.prompt = prompt;
        this.commandMap = new CommandMap();
    }

    public Terminal(CommandMap commandMap) {
        this.scanner = new Scanner(System.in);
        this.prompt = JavaColor.apply("&9ezLib&8>&7");
        this.commandMap = commandMap;
    }

    public Terminal(String prompt, CommandMap commandMap) {
        this.scanner = new Scanner(System.in);
        this.prompt = prompt;
        this.commandMap = commandMap;
    }

    public Terminal() {
        this.scanner = new Scanner(System.in);
        this.prompt = JavaColor.apply("&9ezLib&8>&7");
        this.commandMap = new CommandMap();
    }

    public void start() {
        handleTerminal();
    }

    public void handleTerminal() {
        Thread commandThread = new Thread(() -> {
            while (Thread.currentThread().isAlive()) {
                System.out.print(prompt);
                if(scanner.hasNext()) {
                    String rawCommand = scanner.nextLine();
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
        });

        commandThread.start();
    }
}
