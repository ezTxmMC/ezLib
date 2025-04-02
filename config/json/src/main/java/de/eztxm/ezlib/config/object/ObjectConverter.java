package de.eztxm.ezlib.config.object;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ObjectConverter {
    private final Object object;

    public ObjectConverter(Object object) {
        this.object = object;
    }

    public Object asObject() {
        return object;
    }

    public String asString() {
        if (object != null) {
            return object.toString();
        }
        return null;
    }

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

    public JsonObject asJsonObject() {
        try {
            if (object instanceof JsonObject) {
                return (JsonObject) object;
            }
            if (object instanceof String) {
                return JsonObject.parse((String) object);
            }
            if (object instanceof Map<?, ?> map) {
                JsonObject jObj = new JsonObject();
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    jObj.put(entry.getKey().toString(), entry.getValue());
                }
                return jObj;
            }
            return JsonObject.parse(object.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public JsonArray<?> asJsonArray() {
        try {
            if (object instanceof JsonArray jsonArray) {
                return jsonArray;
            }
            if (object instanceof String jsonStr) {
                return JsonArray.parse(jsonStr, Object.class);
            }
            if (object instanceof List<?> list) {
                JsonArray<Object> arr = new JsonArray<>();
                for (Object item : list) {
                    arr.add(item);
                }
                return arr;
            }
            JsonArray<Object> arr = new JsonArray<>();
            arr.add(object);
            return arr;
        } catch (Exception e) {
            return null;
        }
    }
}