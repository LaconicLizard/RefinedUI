package lacliz.refinedui.api;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * A ButtonWidget that can be cycled backwards.  Use this button instead of ButtonWidget to ensure that
 * your buttons interact with RefinedUI correctly.
 */
public class ReversibleCyclicButton extends ButtonWidget {

    public final PressAction cycleBackwards;

    public ReversibleCyclicButton(int x, int y, int width, int height, Text message, PressAction onPress, PressAction cycleBackwards) {
        super(x, y, width, height, message, onPress);
        this.cycleBackwards = cycleBackwards;
    }

    public ReversibleCyclicButton(int x, int y, int width, int height, Text message, PressAction onPress, PressAction cycleBackwards,
                                  TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.cycleBackwards = cycleBackwards;
    }

    // dev note: functionality provided in ButtonWidget_Mixin for clarity

}
