package de.eztxm.ezlib.config.object;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;            // External dependency for org.json
import com.google.gson.JsonElement;     // External dependency for Gson

/**
 * A simple standalone implementation of a JSON object.
 * Supports basic operations (set, get, remove) as well as minimal JSON output and parsing.
 */
public class JsonObject {

    private final Map<String, Object> map;

    /**
     * Creates an empty JsonObject.
     */
    public JsonObject() {
        this.map = new LinkedHashMap<>();
    }

    /**
     * Constructs a JsonObject from an org.json.JSONObject.
     *
     * @param jsonObj the org.json.JSONObject
     */
    public JsonObject(JSONObject jsonObj) {
        this();
        for (String key : jsonObj.keySet()) {
            this.set(key, jsonObj.get(key));
        }
    }

    /**
     * Constructs a JsonObject from a Gson JsonObject.
     *
     * @param gsonObj the Gson JsonObject.
     */
    public JsonObject(com.google.gson.JsonObject gsonObj) {
        this();
        for (Map.Entry<String, JsonElement> entry : gsonObj.entrySet()) {
            // Convert the Gson element to a Java object using our parseValue method.
            this.set(entry.getKey(), parseValue(entry.getValue().toString()));
        }
    }

    /**
     * Sets a value under the given key.
     *
     * @param key   the key.
     * @param value the value.
     */
    public void set(String key, Object value) {
        map.put(key, value);
    }

    /**
     * Removes the entry with the given key.
     *
     * @param key the key.
     */
    public void remove(String key) {
        map.remove(key);
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key the key.
     * @return the corresponding value or null if not found.
     */
    public Object get(String key) {
        return map.get(key);
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
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append("\"").append(escape(entry.getKey())).append("\":")
                    .append(valueToString(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Converts a given value into its JSON representation.
     *
     * @param value the value to convert.
     * @return the JSON string representation.
     */
    private String valueToString(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escape((String) value) + "\"";
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof JsonObject || value instanceof JsonArray) {
            return value.toString();
        } else if (value instanceof Iterable) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            boolean first = true;
            for (Object item : (Iterable<?>) value) {
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }
                sb.append(valueToString(item));
            }
            sb.append("]");
            return sb.toString();
        } else if (value.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                Object item = Array.get(value, i);
                sb.append(valueToString(item));
            }
            sb.append("]");
            return sb.toString();
        } else {
            // Fallback: use toString() and treat the result as a String.
            return "\"" + escape(value.toString()) + "\"";
        }
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
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON object");
        }
        // Remove the outer curly braces.
        json = json.substring(1, json.length() - 1).trim();
        if (json.isEmpty()) {
            return obj;
        }
        // Split on commas not inside quotes.
        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
            if (keyValue.length != 2) continue;
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            // Remove quotes from the key.
            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
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
        switch (value) {
            case "null" -> {
                return null;
            }
            case "true" -> {
                return Boolean.TRUE;
            }
            case "false" -> {
                return Boolean.FALSE;
            }
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return unescape(value.substring(1, value.length() - 1));
        }
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            // Fallback: return the value as a String.
            return value;
        }
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
