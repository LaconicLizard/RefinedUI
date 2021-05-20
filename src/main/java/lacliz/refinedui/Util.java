package lacliz.refinedui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class Util {

    // weak set of ButtonWidgets that are really boolean buttons
    public static final Set<ButtonWidget> SOME_BOOLEAN_BUTTONS = Collections.synchronizedSet(
            Collections.newSetFromMap(new WeakHashMap<>()));

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

}
