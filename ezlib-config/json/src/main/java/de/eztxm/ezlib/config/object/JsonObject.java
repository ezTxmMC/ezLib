package de.eztxm.ezlib.config.object;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple standalone implementation of a JSON object.
 * Supports basic operations (set, get, remove) as well as minimal JSON output and parsing.
 */
@Getter
public class JsonObject {

    private final Map<String, Object> elements;

    /**
     * Creates an empty JsonObject.
     */
    public JsonObject() {
        this.elements = new LinkedHashMap<>();
    }

    /**
     * Sets a value under the given key.
     *
     * @param key   the key.
     * @param value the value.
     */
    public void set(String key, Object value) {
        elements.put(key, value);
    }

    /**
     * Removes the entry with the given key.
     *
     * @param key the key.
     */
    public void remove(String key) {
        elements.remove(key);
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key the key.
     * @return the corresponding value or null if not found.
     */
    public Object get(String key) {
        return elements.get(key);
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    /**
     * Produces a JSON-formatted string of this object.
     *
     * @return JSON string.
     */
    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : elements.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("\"").append(escape(entry.getKey())).append("\":")
                    .append(valueToString(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Produces a formatted JSON string.
     *
     * @param prettyPrint Whether to format the JSON with indentation.
     * @return JSON string.
     */
    public String toJsonString(boolean prettyPrint) {
        if (!prettyPrint) return toJsonString();
        return formatJson(toJsonString(), 4);
    }

    /**
     * Formats a JSON string with indentation.
     *
     * @param json  The raw JSON string.
     * @param indentFactor Number of spaces for indentation.
     * @return Formatted JSON string.
     */
    private static String formatJson(String json, int indentFactor) {
        StringBuilder formattedJson = new StringBuilder();
        int indent = 0;
        boolean inQuotes = false;
        for (char c : json.toCharArray()) {
            switch (c) {
                case '{', '[' -> {
                    formattedJson.append(c);
                    if (!inQuotes) {
                        indent += indentFactor;
                        formattedJson.append("\n").append(" ".repeat(indent));
                    }
                }
                case '}', ']' -> {
                    if (!inQuotes) {
                        indent -= indentFactor;
                        formattedJson.append("\n").append(" ".repeat(indent));
                    }
                    formattedJson.append(c);
                }
                case '"' -> {
                    formattedJson.append(c);
                    inQuotes = !inQuotes;
                }
                case ',' -> {
                    formattedJson.append(c);
                    if (!inQuotes) {
                        formattedJson.append("\n").append(" ".repeat(indent));
                    }
                }
                case ':' -> {
                    formattedJson.append(c);
                    if (!inQuotes) {
                        formattedJson.append(" ");
                    }
                }
                default -> formattedJson.append(c);
            }
        }
        return formattedJson.toString();
    }

    /**
     * Converts a given value into its JSON representation.
     *
     * @param value the value to convert.
     * @return the JSON string representation.
     */
    private String valueToString(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return "\"" + escape((String) value) + "\"";
        if (value instanceof Number || value instanceof Boolean) return value.toString();
        if (value instanceof JsonObject || value instanceof JsonArray) return value.toString();
        if (value instanceof Iterable) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            boolean first = true;
            for (Object item : (Iterable<?>) value) {
                if (!first) sb.append(",");
                first = false;
                sb.append(valueToString(item));
            }
            sb.append("]");
            return sb.toString();
        }
        if (value.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                if (i > 0) sb.append(",");
                Object item = Array.get(value, i);
                sb.append(valueToString(item));
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + escape(value.toString()) + "\"";
    }

    /**
     * Escapes special characters in a string according to the JSON specification.
     *
     * @param s the input string.
     * @return the escaped string.
     */
    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Parses a JSON-formatted string into a JsonObject.
     * This minimal implementation supports simple, flat JSON objects.
     *
     * @param json the JSON string.
     * @return the parsed JsonObject.
     * @throws IllegalArgumentException if the string is not a valid JSON object.
     */
    public static JsonObject parse(String json) {
        JsonObject obj = new JsonObject();
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith(" "))
            throw new IllegalArgumentException("Invalid JSON object");
        json = json.substring(1, json.length() - 1).trim();
        if (json.isEmpty()) return obj;
        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
            if (keyValue.length != 2) continue;
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            if (key.startsWith("\"") && key.endsWith("\""))
                key = key.substring(1, key.length() - 1);
            Object parsedValue = parseValue(value);
            obj.set(key, parsedValue);
        }
        return obj;
    }

    /**
     * Converts a JSON value (as a String) into the corresponding Java object.
     *
     * @param value the JSON value as a string.
     * @return the corresponding Java object (String, Number, Boolean, or null).
     */
    public static Object parseValue(String value) {
        if (value.equals("null")) return null;
        if (value.equals("true")) return Boolean.TRUE;
        if (value.equals("false")) return Boolean.FALSE;
        if (value.startsWith("\"") && value.endsWith("\""))
            return unescape(value.substring(1, value.length() - 1));
        try {
            if (value.contains(".")) return Double.parseDouble(value);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }


    public JsonObject deepCopy() {
        JsonObject copy = new JsonObject();
        for (Map.Entry<String, Object> entry : elements.entrySet()) {
            copy.set(entry.getKey(), deepCopyValue(entry.getValue()));
        }
        return copy;
    }

    private static Object deepCopyValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof JsonObject) {
            return ((JsonObject) value).deepCopy();
        }
        if (value instanceof JsonArray) {
            return ((JsonArray) value).deepCopy();
        }
        if (value instanceof List<?>) {
            List<Object> copyList = new java.util.ArrayList<>();
            for (Object item : (List<?>) value) {
                copyList.add(deepCopyValue(item));
            }
            return copyList;
        }
        if (value instanceof Map<?, ?>) {
            Map<Object, Object> copyMap = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                copyMap.put(entry.getKey(), deepCopyValue(entry.getValue()));
            }
            return copyMap;
        }
        if (value.getClass().isArray()) {
            int len = Array.getLength(value);
            Object copyArray = Array.newInstance(value.getClass().getComponentType(), len);
            for (int i = 0; i < len; i++) {
                Array.set(copyArray, i, deepCopyValue(Array.get(value, i)));
            }
            return copyArray;
        }
        return value;
    }

    /**
     * Converts an escaped string back to its original form.
     *
     * @param s the escaped string.
     * @return the unescaped string.
     */
    private static String unescape(String s) {
        return s.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\b", "\b")
                .replace("\\f", "\f")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
 }
