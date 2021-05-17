package lacliz.refinedui.mixin;

import lacliz.refinedui.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.util.OrderableTooltip;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OptionButtonWidget.class)
public abstract class OptionButtonWidget_Mixin extends ButtonWidget implements OrderableTooltip {

    @Shadow @Final private Option option;

    public OptionButtonWidget_Mixin() {
        super(0, 0, 0, 0, null, null);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // when pressing a cyclic option button, cycle backwards when the correct mouse button is pressed
        if (Config.get().cycleButtonBack  // check enabled
                && Config.cycleButtonBack_keyBinding.matchesMouse(button)  // and it's the right button
                && this.option instanceof CyclingOption) {  // and the option is cyclic
            // modified from sauce: AbstractButtonWiget.mouseClicked
            if (this.active && this.visible && this.clicked(mouseX, mouseY)) {
                CyclingOption co = (CyclingOption) this.option;
                MinecraftClient client = MinecraftClient.getInstance();
                this.playDownSound(client.getSoundManager());
                GameOptions options = client.options;
                // taken from CyclingOption.createButton
                co.cycle(options, -1);  // cycle back instead of forwards
                setMessage(co.getMessage(options));  // update button message
                return true;
            }
            return false;
        } else {  // act normal
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
