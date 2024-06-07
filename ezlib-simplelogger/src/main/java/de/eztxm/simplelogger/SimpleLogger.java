package de.eztxm.simplelogger;

import de.eztxm.simplelogger.color.LoggerColor;

public class SimpleLogger {
    private String prefix;

    public SimpleLogger() {}

    public SimpleLogger(String prefix) {
        this.prefix = prefix;
    }

    public void info(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_WHITE + message);
        }
        System.out.println(LoggerColor.ANSI_WHITE + prefix + message);
    }

    public void marked(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_GREEN + message);
        }
        System.out.println(LoggerColor.ANSI_GREEN + prefix + message);
    }

    public void warn(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_YELLOW + message);
        }
        System.out.println(LoggerColor.ANSI_YELLOW + prefix + message);
    }

    public void error(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_RED + message);
        }
        System.out.println(LoggerColor.ANSI_RED + prefix + message);
    }

    public void custom(String message) {
        if (prefix == null) {
            System.out.println(message);
        }
        System.out.println(prefix + message);
    }

    public void custom(LoggerColor color, String message) {
        if (prefix == null) {
            System.out.println(message);
        }
        System.out.println(color + prefix + message);
    }
}
