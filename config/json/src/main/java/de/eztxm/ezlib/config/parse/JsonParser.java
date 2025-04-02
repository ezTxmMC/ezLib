package de.eztxm.ezlib.config.parse;

import de.eztxm.ezlib.config.object.JsonArray;
import de.eztxm.ezlib.config.object.JsonObject;

public class JsonParser {
    private String json;
    private int index;

    public Object parse(String input) {
        this.json = input.trim();
        this.index = 0;
        return parseValue();
    }

    private Object parseValue() {
        skipWhitespace();
        if (index >= json.length())
            throw new RuntimeException("Unexpected end");

        char ch = json.charAt(index);
        if (ch == '{')
            return parseObject();
        if (ch == '[')
            return parseArray();
        if (ch == '"')
            return parseString();
        if (ch == 't' && json.startsWith("true", index)) {
            index += 4;
            return true;
        }
        if (ch == 'f' && json.startsWith("false", index)) {
            index += 5;
            return false;
        }
        if (ch == 'n' && json.startsWith("null", index)) {
            index += 4;
            return null;
        }
        if (ch == '-' || Character.isDigit(ch))
            return parseNumber();

        throw new RuntimeException("Unexpected character: " + ch);
    }

    private JsonObject parseObject() {
        JsonObject obj = new JsonObject();
        index++; // skip '{'
        skipWhitespace();
        if (json.charAt(index) == '}') {
            index++;
            return obj;
        }

        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            if (json.charAt(index++) != ':')
                throw new RuntimeException("Expected ':'");
            skipWhitespace();
            Object value = parseValue();
            obj.put(key, value);
            skipWhitespace();
            char ch = json.charAt(index++);
            if (ch == '}')
                break;
            if (ch != ',')
                throw new RuntimeException("Expected ',' or '}'");
        }
        return obj;
    }

    private JsonArray<Object> parseArray() {
        JsonArray<Object> arr = new JsonArray<>();
        index++; // skip '['
        skipWhitespace();
        if (json.charAt(index) == ']') {
            index++;
            return arr;
        }

        while (true) {
            skipWhitespace();
            arr.add(parseValue());
            skipWhitespace();
            char ch = json.charAt(index++);
            if (ch == ']')
                break;
            if (ch != ',')
                throw new RuntimeException("Expected ',' or ']'");
        }
        return arr;
    }

    private String parseString() {
        index++; // skip opening "
        StringBuilder sb = new StringBuilder();
        while (json.charAt(index) != '"') {
            char ch = json.charAt(index++);
            if (ch == '\\') {
                char next = json.charAt(index++);
                switch (next) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    default -> sb.append(next);
                }
            } else {
                sb.append(ch);
            }
        }
        index++; // skip closing "
        return sb.toString();
    }

    private Number parseNumber() {
        int start = index;
        while (index < json.length() && "-0123456789.eE".indexOf(json.charAt(index)) >= 0) {
            index++;
        }
        String num = json.substring(start, index);
        return num.contains(".") || num.contains("e") || num.contains("E")
                ? Double.parseDouble(num)
                : Long.parseLong(num);
    }

    private void skipWhitespace() {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }
}
