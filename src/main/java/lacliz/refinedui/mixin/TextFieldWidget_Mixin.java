package lacliz.refinedui.mixin;

import lacliz.refinedui.Config;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidget_Mixin extends AbstractButtonWidget {

    // constructor for super (never used)
    public TextFieldWidget_Mixin() {
        super(0, 0, 0, 0, null);
    }

    @Shadow public abstract boolean isVisible();

    @Shadow public abstract void setText(String text);

    @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        // when clicking a text field with the appropriate button, clear its contents
        // requires that this TextFieldWidget be focused (to prevent accidentally clearing stuff)
        if (Config.get().textFieldClear  // controlled via config
                && isVisible()  // check visible
                // check we clicked on this
                && mouseX >= (double) this.x && mouseX < (double) (this.x + this.width)
                && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height)
                && isFocused()  // check that we're focused
                && Config.textFieldClear_keyBinding.matchesMouse(button)) {  // check that it's the correct button
            this.setText("");
        }
    }

}
