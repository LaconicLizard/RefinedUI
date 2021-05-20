package lacliz.refinedui.mixin;

import lacliz.refinedui.RUIKeybinds;
import lacliz.refinedui.Util;
import lacliz.refinedui.accessors.Difficulty_Accessor;
import lacliz.refinedui.api.ReversibleCyclicButton;
import lacliz.refinedui.api.ReversibleCyclicButtonI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

import static lacliz.refinedui.RefinedUI.getConfig;

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
            } else if (getConfig().cycleButtonBack && RUIKeybinds.cycleButtonBack_keyBinding.matchesMouse(button)) {
                //noinspection ConstantConditions
                if ((Object) this instanceof ReversibleCyclicButton) {  // for api
                    playSound();
                    ((ReversibleCyclicButton) (Object) this).cycleBackwards.onPress((ButtonWidget) (Object) this);
                    return true;
                }
                if (this instanceof ReversibleCyclicButtonI) {
                    playSound();
                    ((ReversibleCyclicButtonI) this).cycleBackwards();
                    return true;
                }
                //noinspection SuspiciousMethodCalls
                if (Util.SOME_BOOLEAN_BUTTONS.contains(this)) {
                    playSound();
                    onClick(mouseX, mouseY);
                    return true;
                }
                Screen cs = MinecraftClient.getInstance().currentScreen;
                if (cs == null) return false;
                if (cs instanceof CreateWorldScreen) {
                    // ----- main screen (not more options dialog) -----
                    CreateWorldScreen_Accessor cwsa = (CreateWorldScreen_Accessor) cs;
                    MoreOptionsDialog_Accessor moda = (MoreOptionsDialog_Accessor) ((CreateWorldScreen) cs).moreOptionsDialog;
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
                    // ----- more options dialog -----
                    else if ((Object) this == moda.getBonusItemsButton()) {
                        playSound();
                        onPress();
                    } else if ((Object) this == moda.getMapTypeButton()) {
                        playSound();
                        // adapted from mapTypeButton initializer on MoreOptionsDialog
                        while (true) {
                            Optional<GeneratorType> gt = moda.getGeneratorType();
                            if (gt.isPresent()) {
                                List<GeneratorType> vals = GeneratorType_Accessor.getVALUES();
                                // the big change:
                                int i = vals.indexOf(gt.get()) - 1;
                                if (i < 0) {
                                    i = vals.size() - 1;
                                }

                                GeneratorType generatorType = vals.get(i);
                                moda.setGeneratorType(Optional.of(generatorType));
                                GeneratorOptions gopts = moda.getGeneratorOptions();
                                gopts = generatorType.createDefaultOptions(moda.getRegistryManager(),
                                        gopts.getSeed(), gopts.shouldGenerateStructures(),
                                        gopts.hasBonusChest());
                                moda.setGeneratorOptions(gopts);
                                if (gopts.isDebugWorld() && !Screen.hasShiftDown()) {
                                    continue;
                                }
                            }
                            ((CreateWorldScreen) cs).setMoreOptionsOpen();
                            queueNarration(250);
                            return true;
                        }
                    } else if ((Object) this == moda.getMapFeaturesButton()) {
                        playSound();
                        moda.setGeneratorOptions(moda.getGeneratorOptions().toggleGenerateStructures());
                        queueNarration(250);
                    }
                } else if (cs instanceof EditGameRulesScreen) {
                    // handling boolean game rules, so they respond to cycleButtonBack
                    EditGameRulesScreen_Accessor egrsa = (EditGameRulesScreen_Accessor) cs;
                    EditGameRulesScreen.RuleListWidget rlw = egrsa.getRuleListWidget();
                    EditGameRulesScreen_BooleanRuleWidget_Accessor brwa;
                    for (EditGameRulesScreen.AbstractRuleWidget arw : rlw.children()) {
                        if (arw instanceof EditGameRulesScreen.BooleanRuleWidget) {
                            brwa = (EditGameRulesScreen_BooleanRuleWidget_Accessor) arw;
                            if (brwa.getToggleButton() == (Object) this) {
                                playSound();
                                onPress();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
