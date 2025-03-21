package de.eztxm.ezlib.config.object;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A simple standalone implementation of a JSON array.
 * Supports basic operations such as adding, removing, and retrieving values,
 * as well as producing a JSON-formatted string and parsing a JSON string.
 */
@Getter
public class JsonArray implements Iterable<Object> {

    private final List<Object> elements;

    /**
     * Creates an empty JsonArray.
     */
    public JsonArray() {
        this.elements = new ArrayList<>();
    }

    /**
     * Adds a value to the array.
     *
     * @param value the value to add (e.g., String, Number, Boolean, JsonObject, JsonArray, array, or Iterable).
     */
    public void add(Object value) {
        elements.add(value);
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index the index of the element to remove.
     */
    public void remove(int index) {
        elements.remove(index);
    }

    /**
     * Check if a value is contained.
     *
     * @param value the value to check if is contained
     * @return if is contained
     */
    public boolean contains(Object value) {
        return elements.contains(value);
    }

    /**
     * Retrieves the element at the specified index.
     *
     * @param index the index.
     * @return the element at that position.
     */
    public Object get(int index) {
        return elements.get(index);
    }

    /**
     * Get the size of the json array.
     *
     * @return the count of elements in json array.
     */
    public int size() {
        return elements.size();
    }

    @Override
    public @NotNull Iterator<Object> iterator() {
        return elements.iterator();
    }

    @Override
    public void forEach(Consumer<? super Object> action) {
        elements.forEach(action);
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    /**
     * Produces a JSON-formatted string of the array.
     *
     * @return the JSON string.
     */
    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Object value : elements) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append(valueToString(value));
        }
        sb.append("]");
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
     * Parses a JSON-formatted string into a JsonArray.
     * This implementation supports simple arrays containing primitive values, strings,
     * and nested JsonObject or JsonArray instances.
     *
     * @param json the JSON string.
     * @return the parsed JsonArray.
     * @throws IllegalArgumentException if the string is not a valid JSON array.
     */
    public static JsonArray parse(String json) {
        JsonArray arr = new JsonArray();
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            throw new IllegalArgumentException("Invalid JSON array");
        }
        // Remove the square brackets.
        json = json.substring(1, json.length() - 1).trim();
        if (json.isEmpty()) {
            return arr;
        }
        // Split on commas that are not inside quotes.
        String[] items = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String item : items) {
            Object parsedItem = JsonObject.parseValue(item.trim());
            arr.add(parsedItem);
        }
        return arr;
    }
}
