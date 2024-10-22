package de.eztxm.ezlib.config.object;

import de.eztxm.ezlib.config.JsonConfig;

import java.util.Collections;
import java.util.List;

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

    public Object asObject() {
        try {
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    public String asString() {
        try {
            return object.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean asBoolean() {
        try {
            return (boolean) object;
        } catch (Exception e) {
            return false;
        }
    }

    public int asInteger() {
        try {
            return (int) object;
        } catch (Exception e) {
            return -1;
        }
    }

    public double asDouble() {
        try {
            return (double) object;
        } catch (Exception e) {
            return -1;
        }
    }

    public float asFloat() {
        try {
            return (float) object;
        } catch (Exception e) {
            return -1;
        }
    }

    public List<Object> asList() {
        try {
            return Collections.singletonList(object);
        } catch (Exception e) {
            return null;
        }
    }

    public JsonObject asJsonObject() {
        try {
            return new JsonObject(object);
        } catch (Exception e) {
            return null;
        }
    }

    public JsonArray asJsonArray() {
        try {
            return (JsonArray) object;
        } catch (Exception e) {
            return null;
        }
    }
}
