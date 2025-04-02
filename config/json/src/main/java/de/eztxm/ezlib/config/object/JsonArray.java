package de.eztxm.ezlib.config.object;

import java.util.ArrayList;

import de.eztxm.ezlib.config.parse.JsonParser;
import lombok.Getter;

@Getter
public class JsonArray<T> extends ArrayList<T> implements JsonValue {

    public JsonArray() {
        super();
    }

    public ObjectConverter get(String key) {
        for (T type : this) {
            if (type instanceof JsonObject jsonObject) {
                if (!jsonObject.containsKey(key)) {
                    continue;
                }
                return new ObjectConverter(jsonObject.get(key));
            }
        }
        return null;
    }

    @Override
    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        boolean pretty = JsonUtil.prettyPrint;
        sb.append("[");

        if (pretty)
            sb.append("\n");

        boolean first = true;
        for (Object value : this) {
            if (!first)
                sb.append(pretty ? ",\n" : ",");
            if (pretty)
                sb.append(JsonUtil.indent(4));
            sb.append(JsonUtil.valueToJson(value));
            first = false;
        }

        if (pretty) {
            sb.append("\n").append(JsonUtil.indent(4));
        }
        sb.append("]");
        return sb.toString();
    }

    public Class<?> getElementType() {
        Class<?> type = null;
        for (Object item : this) {
            if (item == null)
                continue;
            if (type == null) {
                return item.getClass();
            }
            return Object.class;
        }
        return Object.class;
    }

    public static <T> JsonArray<T> parse(String json, Class<T> clazz) {
        JsonParser parser = new JsonParser();
        Object result = parser.parse(json);

        if (!(result instanceof JsonArray<?> rawArray)) {
            throw new IllegalArgumentException("JSON is not an array");
        }

        JsonArray<T> typedArray = new JsonArray<>();
        for (Object element : rawArray) {
            if (clazz.isInstance(element)) {
                typedArray.add(clazz.cast(element));
            } else {
                throw new IllegalArgumentException(
                        "Element type mismatch: expected " + clazz + " but got " + element.getClass());
            }
        }
        return typedArray;
    }
}
