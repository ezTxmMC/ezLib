package dev.eztxm.logger;

public class SimpleLogger {
    private final String prefix;

    public SimpleLogger(String prefix) {
        this.prefix = prefix;
    }

    public void info(String message) {
        System.out.println(LoggerColor.ANSI_WHITE + prefix + message);
    }

    public void marked(String message) {
        System.out.println(LoggerColor.ANSI_GREEN + prefix + message);
    }

    public void warn(String message) {
        System.out.println(LoggerColor.ANSI_YELLOW + prefix + message);
    }

    public void error(String message) {
        System.out.println(LoggerColor.ANSI_RED + prefix + message);
    }
}
