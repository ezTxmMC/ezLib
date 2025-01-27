package de.eztxm.ezlib.config.object;

import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
public class JsonObject {
    private final JSONObject jsonObject;

    public JsonObject() {
        this.jsonObject = new JSONObject();
    }

    public JsonObject(String jsonString) {
        this.jsonObject = new JSONObject(jsonString);
    }

    public JsonObject(Object object) {
        this.jsonObject = (JSONObject) object;
    }

    public JsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void set(String key, String value) {
        this.jsonObject.put(key, value);
    }

    public void remove(String key) {
        this.jsonObject.remove(key);
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
