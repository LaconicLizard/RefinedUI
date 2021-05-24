package lacliz.refinedui.api;

import net.minecraft.text.Text;

/**
 * A ReversibleCyclicButton with only two options; thus cycling backwards and forwards do the same thing.
 */
public class BooleanReversibleCyclicButton extends ReversibleCyclicButton {

    public BooleanReversibleCyclicButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, onPress);
    }

    public BooleanReversibleCyclicButton(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, onPress, tooltipSupplier);
    }

}
