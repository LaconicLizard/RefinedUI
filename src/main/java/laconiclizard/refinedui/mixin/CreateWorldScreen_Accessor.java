package laconiclizard.refinedui.mixin;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreateWorldScreen.class)
public interface CreateWorldScreen_Accessor {

    @Accessor("gameModeSwitchButton")
    ButtonWidget getGameModeSwitchButton();

    @Accessor("difficultyButton")
    ButtonWidget getDifficultyButton();

    @Accessor("enableCheatsButton")
    ButtonWidget getEnableCheatsButton();

    // these fields are used to indicate the current difficulty
    // idk why there's two

    @Accessor("field_24289")
    Difficulty getField_24289();

    @Accessor("field_24289")
    void setField_24289(Difficulty v);

    @Accessor("field_24290")
    Difficulty getField_24290();

    @Accessor("field_24290")
    void setField_24290(Difficulty v);

    // continuing

    @Accessor("currentMode")
    CreateWorldScreen.Mode getCurrentMode();

    @Invoker("tweakDefaultsTo")
    void invokeTweakDefaultsTo(CreateWorldScreen.Mode mode);

}
