package de.eztxm.ezlib.config.object;

import de.eztxm.ezlib.config.JsonConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A helper class to convert objects into different types.
 * Provides methods to interpret the underlying object as a specific type.
 */
public class ObjectConverter {
    private final JsonConfig jsonConfig;
    private final Object object;

    public ObjectConverter(JsonConfig jsonConfig, Object object) {
        this.jsonConfig = jsonConfig;
        this.object = object;
    }

    public ObjectConverter(Object object) {
        this.jsonConfig = null;
        this.object = object;
    }

    /**
     * Returns the underlying object.
     *
     * @return the object.
     */
    public Object asObject() {
        return object;
    }

    /**
     * Returns the object as a String.
     *
     * @return the string representation, or null if the object is null.
     */
    public String asString() {
        return object != null ? object.toString() : null;
    }

    /**
     * Returns the object as a boolean.
     *
     * @return the boolean value, or false if conversion fails.
     */
    public boolean asBoolean() {
        try {
            if (object instanceof Boolean) {
                return (Boolean) object;
            }
            return Boolean.parseBoolean(asString());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the object as an integer.
     *
     * @return the integer value, or 0 if conversion fails.
     */
    public int asInteger() {
        try {
            if (object instanceof Number) {
                return ((Number) object).intValue();
            }
            return Integer.parseInt(asString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Returns the object as a double.
     *
     * @return the double value, or 0 if conversion fails.
     */
    public double asDouble() {
        try {
            if (object instanceof Number) {
                return ((Number) object).doubleValue();
            }
            return Double.parseDouble(asString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Returns the object as a float.
     *
     * @return the float value, or 0 if conversion fails.
     */
    public float asFloat() {
        try {
            if (object instanceof Number) {
                return ((Number) object).floatValue();
            }
            return Float.parseFloat(asString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Returns the object as a list.
     * If the object is not a list, it is wrapped in a singleton list.
     *
     * @return a List of objects.
     */
    @SuppressWarnings("unchecked")
    public List<Object> asList() {
        try {
            if (object instanceof List) {
                return (List<Object>) object;
            }
            return Collections.singletonList(object);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Attempts to interpret the object as a JsonObject.
     * - If it is already a JsonObject, it is returned directly.
     * - If it is an org.json.JSONObject or a Gson JsonObject, a new JsonObject is created.
     * - If it is a JSON string, it is parsed.
     * - If it is a Map, a new JsonObject is built from it.
     * - Otherwise, object.toString() is parsed as JSON.
     *
     * @return the corresponding JsonObject, or null if conversion fails.
     */
    public JsonObject asJsonObject() {
        try {
            if (object instanceof JsonObject) {
                return (JsonObject) object;
            }
            if (object instanceof org.json.JSONObject) {
                return new JsonObject((org.json.JSONObject) object);
            }
            if (object instanceof com.google.gson.JsonObject) {
                return new JsonObject((com.google.gson.JsonObject) object);
            }
            if (object instanceof String) {
                return JsonObject.parse((String) object);
            }
            if (object instanceof Map) {
                JsonObject jObj = new JsonObject();
                Map<?, ?> map = (Map<?, ?>) object;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    jObj.set(entry.getKey().toString(), entry.getValue());
                }
                return jObj;
            }
            return JsonObject.parse(object.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Attempts to interpret the object as a JsonArray.
     * - If it is already a JsonArray, it is returned.
     * - If it is an org.json.JSONArray or a Gson JsonArray, a new JsonArray is created.
     * - If it is a JSON string, it is parsed.
     * - If it is a List, its elements are added to a new JsonArray.
     * - Otherwise, the object is wrapped as a single element in a JsonArray.
     *
     * @return the corresponding JsonArray, or null if conversion fails.
     */
    public JsonArray asJsonArray() {
        try {
            if (object instanceof JsonArray) {
                return (JsonArray) object;
            }
            if (object instanceof org.json.JSONArray) {
                return new JsonArray((org.json.JSONArray) object);
            }
            if (object instanceof com.google.gson.JsonArray) {
                return new JsonArray((com.google.gson.JsonArray) object);
            }
            if (object instanceof String) {
                return JsonArray.parse((String) object);
            }
            if (object instanceof List) {
                JsonArray arr = new JsonArray();
                for (Object item : (List<?>) object) {
                    arr.add(item);
                }
                return arr;
            }
            JsonArray arr = new JsonArray();
            arr.add(object);
            return arr;
        } catch (Exception e) {
            return null;
        }
    }
}
