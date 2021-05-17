package lacliz.refinedui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public static Object normalizeJsonElement(JsonElement elt) {
        if (elt.isJsonPrimitive()) {
            JsonPrimitive p = elt.getAsJsonPrimitive();
            if (p.isBoolean()) return p.getAsBoolean();
            else if (p.isString()) return p.getAsString();
            else if (p.isNumber()) return p.getAsBigDecimal();
            else throw new AssertionError("Unrecognized json primitive type: " + elt);
        } else if (elt.isJsonObject()) {
            JsonObject o = elt.getAsJsonObject();
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : o.entrySet()) {
                result.put(entry.getKey(), normalizeJsonElement(entry.getValue()));
            }
            return result;
        } else if (elt.isJsonNull()) {
            return null;
        } else if (elt.isJsonArray()) {
            JsonArray arr = elt.getAsJsonArray();
            Object[] result = new Object[arr.size()];
            for (int i = 0; i < arr.size(); i += 1) {
                result[i] = normalizeJsonElement(arr.get(i));
            }
            return result;
        } else {
            throw new AssertionError("Unrecognized json type: " + elt);
        }
    }

}
