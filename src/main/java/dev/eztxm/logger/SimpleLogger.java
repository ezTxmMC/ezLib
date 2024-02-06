package dev.eztxm.logger;

public class SimpleLogger {
    private final String prefix;

    public SimpleLogger(String prefix) {
        this.prefix = prefix;
    }

    public SimpleLogger info(String message) {
        System.out.println(LoggerColor.ANSI_WHITE + prefix + message);
        return this;
    }

    public SimpleLogger warn(String message) {
        System.out.println(LoggerColor.ANSI_YELLOW + prefix + message);
        return this;
    }

    public SimpleLogger error(String message) {
        System.out.println(LoggerColor.ANSI_RED + prefix + message);
        return this;
    }
}
