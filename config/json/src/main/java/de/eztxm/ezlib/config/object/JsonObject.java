package de.eztxm.ezlib.config.object;

import java.util.LinkedHashMap;
import java.util.Map;

import de.eztxm.ezlib.config.parse.JsonParser;
import lombok.Getter;

@Getter
public class JsonObject extends LinkedHashMap<String, Object> implements JsonValue {

    public JsonObject() {
        super();
    }

    public ObjectConverter getConverted(String key) {
        return new ObjectConverter(get(key));
    }

    @Override
    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        boolean pretty = JsonUtil.prettyPrint;
        sb.append("{");

        if (pretty)
            sb.append("\n");

        boolean first = true;
        for (Map.Entry<String, Object> entry : this.entrySet()) {
            if (!first)
                sb.append(pretty ? ",\n" : ",");
            if (pretty)
                sb.append(JsonUtil.indent(4));
            sb.append("\"").append(JsonUtil.escapeString(entry.getKey())).append("\":");
            if (pretty)
                sb.append(" ");
            sb.append(JsonUtil.valueToJson(entry.getValue()));
            first = false;
        }

        if (pretty) {
            sb.append("\n").append(JsonUtil.indent(4));
        }
        sb.append("}");
        return sb.toString();
    }

    public static JsonObject parse(String json) {
        JsonParser parser = new JsonParser();
        Object result = parser.parse(json);
        if (result instanceof JsonObject jo) {
            return jo;
        }
        throw new IllegalArgumentException("JSON is not an object");
    }
}
