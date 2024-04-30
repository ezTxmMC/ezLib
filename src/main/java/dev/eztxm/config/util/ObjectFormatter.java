package dev.eztxm.config.util;

import org.json.JSONArray;
import org.json.JSONObject;

public record ObjectFormatter(Object object) {

    public String asString() {
        return object.toString();
    }

    public boolean asBoolean() {
        return (boolean) object;
    }

    public int asInteger() {
        return (int) object;
    }

    public double asDouble() {
        return (double) object;
    }

    public float asFloat() {
        return (float) object;
    }

    public JSONObject asJsonObject() {
        return (JSONObject) object;
    }

    public JSONArray asJsonArray() {
        return (JSONArray) object;
    }
}
