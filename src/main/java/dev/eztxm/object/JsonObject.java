package dev.eztxm.object;

import dev.eztxm.config.JsonConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObject {
    private final JsonConfig jsonConfig;
    private final JSONObject jsonObject;

    public JsonObject(JsonConfig jsonConfig, Object object) {
        this.jsonConfig = jsonConfig;
        this.jsonObject = (JSONObject) object;
    }

    public JsonObject(JsonConfig jsonConfig, JSONObject jsonObject) {
        this.jsonConfig = jsonConfig;
        this.jsonObject = jsonObject;
    }

    public void set(String key, String value) {
        this.jsonObject.put(key, value);
        jsonConfig.save();
    }

    public void remove(String key) {
        this.jsonObject.remove(key);
        jsonConfig.save();
    }

    public ObjectConverter get(String key) {
        try {
            Object object = jsonObject.get(key);
            return new ObjectConverter(object);
        } catch (JSONException e) {
            return null;
        }
    }
}
