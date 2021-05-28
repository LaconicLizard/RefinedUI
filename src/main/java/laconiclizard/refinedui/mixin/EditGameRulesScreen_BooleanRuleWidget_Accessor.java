package laconiclizard.refinedui.mixin;

import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EditGameRulesScreen.BooleanRuleWidget.class)
public interface EditGameRulesScreen_BooleanRuleWidget_Accessor {

    @Accessor("toggleButton")
    ButtonWidget getToggleButton();

}
