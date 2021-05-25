package lacliz.refinedui.api;

import lacliz.refinedui.RefinedUI;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.function.Consumer;

public class RefinedUI_API {

    /**
     * Registers the given ButtonWidget as reversible, so RefinedUI can reverse it when the user alt-clicks on it.
     *
     * @param button  button to register
     * @param reverse reversing function - this will be invoked on button when the user attempt to reverse button
     */
    public static void registerReversibleButton(ButtonWidget button, Consumer<ButtonWidget> reverse) {
        RefinedUI.API_BUTTONS.put(button, reverse);
    }

}
