package lacliz.refinedui.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import lacliz.refinedui.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.GraphicsMode;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.VideoWarningManager;
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
                // the following special cases have badly implemented .cycle(...) methods, and
                // require specific overrides to their behavior
                if (option == Option.GRAPHICS) {
                    // this block adapted from Option class' static initializer where it sets GRAPHICS
                    VideoWarningManager videoWarningManager = client.getVideoWarningManager();
                    GraphicsMode nextMode = GraphicsMode.byId(options.graphicsMode.getId() - 1);
                    // check whether next graphics option is fabulous, warn if so
                    if (nextMode == GraphicsMode.FABULOUS && videoWarningManager.canWarn()) {
                        videoWarningManager.scheduleWarning();
                    } else {
                        options.graphicsMode = nextMode;
                        if (options.graphicsMode == GraphicsMode.FABULOUS && (!GlStateManager.supportsGl30() || videoWarningManager.hasCancelledAfterWarning())) {
                            options.graphicsMode = GraphicsMode.FAST;
                        }
                        client.worldRenderer.reload();
                    }
                } else {  // normal case
                    co.cycle(options, -1);  // cycle back instead of forwards
                }
                // taken from CyclingOption.createButton
                setMessage(co.getMessage(options));  // update button message
                return true;
            }
            return false;
        } else {  // act normal
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
