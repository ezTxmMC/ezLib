package de.eztxm.ezlib.config.object;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;

@Getter
public class JsonArray implements Iterable<Object> {
    private final JSONArray jsonArray;

    public JsonArray(Object object) {
        this.jsonArray = (JSONArray) object;
    }

    public JsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JsonArray() {
        this.jsonArray = new JSONArray();
    }

    public void add(Object value) {
        this.jsonArray.put(value);
    }

    public void remove(Object value) {
        for (int i = 0; i < this.jsonArray.length(); i++) {
            String currentString = this.jsonArray.getString(i);
            if (currentString.equals(value)) {
                this.jsonArray.remove(i);
                break;
            }
        }
    }

    public ObjectConverter get(String key) {
        try {
            Object object = null;
            for (int i = 0; i < this.jsonArray.length(); i++) {
                String currentString = this.jsonArray.getString(i);
                if (currentString.equals(key)) {
                    object = this.jsonArray.get(i);
                    break;
                }
            }
            return new ObjectConverter(object);
        } catch (JSONException | NullPointerException e) {
            return null;
        }
    }

    public Object getAsObject(String key) {
        try {
            Object object = null;
            for (int i = 0; i < this.jsonArray.length(); i++) {
                String currentString = this.jsonArray.getString(i);
                if (currentString.equals(key)) {
                    object = this.jsonArray.get(i);
                    break;
                }
            }
            return object;
        } catch (JSONException | NullPointerException e) {
            return null;
        }
    }

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return this.jsonArray.iterator();
    }

}
