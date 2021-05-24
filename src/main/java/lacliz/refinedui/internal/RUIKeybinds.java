package lacliz.refinedui.internal;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static lacliz.refinedui.RefinedUI.MOD_ID;

public class RUIKeybinds {

    public static final KeyBinding textFieldClear_keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key." + MOD_ID + ".textFieldClearButton",
            InputUtil.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_RIGHT,
            "category." + MOD_ID + ".keys"
    ));
    public static final KeyBinding cycleButtonBack_keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key." + MOD_ID + ".cycleButtonBackButton",
            InputUtil.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_RIGHT,
            "category." + MOD_ID + ".keys"
    ));

    public static void initialize() {
    }

}
