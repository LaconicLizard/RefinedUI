package lacliz.refinedui.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import lacliz.refinedui.RUIKeybinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.*;
import net.minecraft.client.resource.VideoWarningManager;
import net.minecraft.client.util.OrderableTooltip;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static lacliz.refinedui.RefinedUI.LOGGER;
import static lacliz.refinedui.RefinedUI.getConfig;

@Mixin(OptionButtonWidget.class)
public abstract class OptionButtonWidget_Mixin extends ButtonWidget implements OrderableTooltip {

    @Shadow @Final private Option option;

    public OptionButtonWidget_Mixin() {
        super(0, 0, 0, 0, null, null);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // when pressing a cyclic option button, cycle backwards when the correct mouse button is pressed
        if (getConfig().cycleButtonBack  // check enabled
                && RUIKeybinds.cycleButtonBack_keyBinding.matchesMouse(button)  // and it's the right button
                && this.clicked(mouseX, mouseY)  // and clicked within boundaries
                && (this.option instanceof CyclingOption || this.option instanceof BooleanOption)) {  // and the option is supported
            // modified from sauce: AbstractButtonWiget.mouseClicked
            if (this.active && this.visible && this.clicked(mouseX, mouseY)) {
                MinecraftClient client = MinecraftClient.getInstance();
                this.playDownSound(client.getSoundManager());
                GameOptions options = client.options;
                if (this.option instanceof CyclingOption) {
                    CyclingOption co = (CyclingOption) this.option;
                    // the following special cases have badly implemented .cycle(...) methods, and
                    // require specific overrides to their behavior
                    if (option == Option.GRAPHICS) {
                        LOGGER.info("backcycling OptionButtonWidget - Graphics ButtonWidget");
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
                        // no return yet
                    } else {  // normal case
                        co.cycle(options, -1);  // cycle back instead of forwards
                        // no return yet
                    }
                    // taken from CyclingOption.createButton
                    setMessage(co.getMessage(options));  // update button message
                    return true;
                } else if (this.option instanceof BooleanOption) {  // check not necessary, but doing it for organization
                    LOGGER.info("backcycling OptionButtonWidget with BooleanOption");
                    BooleanOption bo = (BooleanOption) this.option;
                    bo.toggle(options);
                    // from BooleanOption.createButton
                    setMessage(bo.getDisplayString(options));
                    return true;
                }
            }
        }
        // otherwise let the normal implemenation handle it
        return super.mouseClicked(mouseX, mouseY, button);
    }

}
