package de.eztxm.ezlib.simplelogger.message;

import de.eztxm.ezlib.simplelogger.color.ILoggerColor;

public class Message {
    private final StringBuilder builder;

    public Message() {
        this.builder = new StringBuilder();
    }

    public Message(String input) {
        this.builder = new StringBuilder(input);
    }

    public Message append(String input) {
        this.builder.append(input);
        return this;
    }

    public Message append(Object input) {
        this.builder.append(input);
        return this;
    }

    public Message appendColor(ILoggerColor loggerColor) {
        this.builder.append(loggerColor.getColorCode());
        return this;
    }

    public String asString() {
        return builder.toString();
    }
}
