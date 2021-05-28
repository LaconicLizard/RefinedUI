package laconiclizard.refinedui.mixin;

import laconiclizard.refinedui.api.RefinedUI_API;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(BooleanListEntry.class)
public class ClothConfig_BooleanListEntry_Mixin {

    @Shadow @Final private ButtonWidget buttonWidget;

    @Inject(method= "<init>(Lnet/minecraft/text/Text;ZLnet/minecraft/text/Text;Ljava/util/function/Supplier;Ljava/util/function/Consumer;Ljava/util/function/Supplier;Z)V",
            at=@At("RETURN"))
    public void post_init(Text fieldName, boolean bool, Text resetButtonKey, Supplier<Boolean> defaultValue,
                          Consumer<Boolean> saveConsumer, Supplier<Optional<Text[]>> tooltipSupplier,
                          boolean requiresRestart, CallbackInfo ci) {
        RefinedUI_API.registerReversibleButton(buttonWidget, ButtonWidget::onPress);
    }

}
