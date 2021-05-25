package lacliz.refinedui;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Util {

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

    /** The number of empty slots in inv, including offhand. */
    public static int nEmptySlots(PlayerInventory inv) {
        int c = 0;
        for (int i = 0; i < 36; i += 1) {
            if (inv.getStack(i).getCount() == 0) {
                c += 1;
            }
        }
        if (inv.offHand.get(0).getCount() == 0) {
            c += 1;
        }
        return c;
    }

}
