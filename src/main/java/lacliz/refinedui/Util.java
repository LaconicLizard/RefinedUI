package lacliz.refinedui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    public static final List<String> POSITIVE_SI_SUFFIXES = Arrays.asList("", "k", "M", "G", "T", "P", "E", "Z", "Y");

    /**
     * The least-abbreviated string representing v, possibly using SI suffixes, with the given rounding mode.
     * <p>
     * Examples:
     * siRoundedString(new BigDecimal(10100), 4, RoundingMode.FLOOR) -> "10k"
     * siRoundedString(new BigDecimal(10100), 5, RoundingMode.FLOOR) -> "10100"
     * siRoundedString(new BigDecimal(1100000), 4, RoundingMode.FLOOR) -> "1.1M"
     * siRoundedString(new BigDecimal(1100000), 5, RoundingMode.FLOOR) -> "1100k"
     * siRoundedString(new BigDecimal(10100000), 4, RoundingMode.FLOOR) -> "10M"
     * siRoundedString(new BigDecimal(10100000), 5, RoundingMode.FLOOR) -> "10.1M"
     * siRoundedString(new BigDecimal(10100000), 6, RoundingMode.FLOOR) -> "10100k"
     *
     * @param v     value to represent
     * @param chars maximum number of characters of result
     * @param rm    RoundingMode of representation
     * @return rounded SI representation of v
     */
    public static String siRoundedString(BigDecimal v, int chars, RoundingMode rm) {
        if (chars <= 0) throw new IllegalArgumentException("chars < 0: " + chars);
        v = v.stripTrailingZeros();
        String s = v.toPlainString();
        if (s.length() <= chars) return s;

        int maxPrec = v.signum() == 0 ? 1 : v.precision() - v.scale(), prec = maxPrec,
                ord, lastOrd = 0;
        while (prec > 0) {
            ord = (maxPrec - prec + 1) / 3;  // +1 so we change orders one loop early
            if (ord != lastOrd) {
                v = v.movePointLeft(3);
                lastOrd = ord;
            } else {  // only decrement precision if we have not changed orders
                prec -= 1;
            }
            v = v.round(new MathContext(prec, rm)).stripTrailingZeros();
            s = v.toPlainString() + POSITIVE_SI_SUFFIXES.get(ord);
            if (s.length() <= chars) return s;
        }
        throw new IllegalArgumentException("Insufficient space to represent " + v + " (" + chars + " chars, rm=" + rm + ")");
    }

    /** Scales the given matrix stack about the given point. */
    public static void scaleAbout(MatrixStack stack, double x, double y, double z, float xscale, float yscale, float zscale) {
        stack.translate(x, y, z);
        stack.scale(xscale, yscale, zscale);
        stack.translate(-x, -y, -z);
    }

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
