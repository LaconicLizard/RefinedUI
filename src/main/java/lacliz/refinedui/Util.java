package lacliz.refinedui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Util {

    /**
     * Map from item -> total amount of that item in inv for all items in inv.
     *
     * @param inv inventory of interest
     * @return map item -> total amount of that item in inv
     */
    public static Map<Item, Integer> itemCounts(Inventory inv) {
        Map<Item, Integer> result = new HashMap<>(inv.size());
        for (int j = 0; j < inv.size(); ++j) {
            ItemStack stack = inv.getStack(j);
            result.compute(stack.getItem(), (key, oldValue) -> oldValue == null
                    ? stack.getCount()
                    : oldValue + stack.getCount());
        }
        return result;
    }

    /**
     * Normalizes a JSON element into its corresponding java form.
     * Note that this method is recursive, and as such should not be used on highly-nested input.
     *
     * @param elt JsonElement to normalize
     * @return normalized element
     */
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
