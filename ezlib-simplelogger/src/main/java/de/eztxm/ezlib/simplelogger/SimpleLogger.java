package de.eztxm.ezlib.simplelogger;

import de.eztxm.ezlib.simplelogger.color.LoggerColor;

public class SimpleLogger {
    private String prefix;

    public SimpleLogger() {}

    public SimpleLogger(String prefix) {
        this.prefix = prefix;
    }

    public void info(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_WHITE.getColorCode() + message);
        }
        System.out.println(LoggerColor.ANSI_WHITE.getColorCode() + prefix + message);
    }

    public void marked(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_GREEN.getColorCode() + message);
        }
        System.out.println(LoggerColor.ANSI_GREEN.getColorCode() + prefix + message);
    }

    public void warn(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_YELLOW.getColorCode() + message);
        }
        System.out.println(LoggerColor.ANSI_YELLOW.getColorCode() + prefix + message);
    }

    public void error(String message) {
        if (prefix == null) {
            System.out.println(LoggerColor.ANSI_RED.getColorCode() + message);
        }
        System.out.println(LoggerColor.ANSI_RED.getColorCode() + prefix + message);
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
