package lacliz.refinedui.mixin;

import lacliz.refinedui.Config;
import lacliz.refinedui.accessors.Difficulty_Accessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ButtonWidget.class)
public abstract class ButtonWidget_Mixin extends AbstractButtonWidget {

    @Shadow public abstract void onPress();

    private ButtonWidget_Mixin() {
        super(0, 0, 0, 0, null);
    }

    private void playSound() {
        this.playDownSound(MinecraftClient.getInstance().getSoundManager());
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // adapted from AbstractButtonWidget
        if (this.active && this.visible && this.clicked(mouseX, mouseY)) {
            if (this.isValidClickButton(button)) {  // normal functionality (normal click)
                playSound();
                this.onClick(mouseX, mouseY);
                return true;
            } else if (Config.cycleButtonBack_keyBinding.matchesMouse(button)) {
                Screen cs = MinecraftClient.getInstance().currentScreen;
                if (cs == null) return false;
                if (cs instanceof CreateWorldScreen) {
                    CreateWorldScreen_Accessor cwsa = (CreateWorldScreen_Accessor) cs;
                    if ((Object) this == cwsa.getDifficultyButton()) {  // cycle difficulty back
                        // adapted from original lambda in CreateWorldScreen
                        playSound();
                        //noinspection ConstantConditions
                        cwsa.setField_24289(((Difficulty_Accessor) (Object) cwsa.getField_24289()).cycleBack());
                        cwsa.setField_24290(cwsa.getField_24289());
                        this.queueNarration(250);
                        return true;
                    } else if ((Object) this == cwsa.getEnableCheatsButton()) {
                        playSound();
                        this.onPress();  // boolean option, so we don't have to do anything special
                    } else if ((Object) this == cwsa.getGameModeSwitchButton()) {
                        // adapted from original lambda in CreateWorldScreen
                        playSound();
                        switch (cwsa.getCurrentMode()) {
                            case SURVIVAL:
                                cwsa.invokeTweakDefaultsTo(CreateWorldScreen.Mode.CREATIVE);
                                break;
                            case HARDCORE:
                                cwsa.invokeTweakDefaultsTo(CreateWorldScreen.Mode.SURVIVAL);
                                break;
                            case CREATIVE:
                                cwsa.invokeTweakDefaultsTo(CreateWorldScreen.Mode.HARDCORE);
                        }
                        queueNarration(250);
                    }
                }
            }
        }
        return false;
    }

}
