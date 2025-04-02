package de.eztxm.ezlib.config.object;

import de.eztxm.ezlib.config.mapper.JsonMapper;

public class JsonUtil {
    public static boolean prettyPrint = false;

    public static String valueToJson(Object value) {
        if (value == null) {
            return "null";
        }

        if (value instanceof String) {
            return "\"" + escapeString((String) value) + "\"";
        }

        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }

        if (value instanceof JsonObject) {
            return ((JsonObject) value).toJsonString();
        }

        if (value instanceof JsonArray<?> array) {
            return array.toJsonString();
        }

        if (!isPrimitiveOrWrapper(value.getClass())) {
            String json = JsonMapper.toJson(value);
            return JsonObject.parse(json).toJsonString();
        }

        throw new RuntimeException("Unsupported type: " + value.getClass());
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || Number.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class;
    }

    public static String escapeString(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static String indent(int level) {
        return "  ".repeat(level);
    }
}
