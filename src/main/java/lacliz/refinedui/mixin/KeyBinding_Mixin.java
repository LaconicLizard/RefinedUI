package lacliz.refinedui.mixin;

import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lacliz.refinedui.RefinedUI.MOD_ID;

@Mixin(KeyBinding.class)
public abstract class KeyBinding_Mixin {

    @Shadow @Final private String translationKey;

    @Inject(method = "equals(Lnet/minecraft/client/options/KeyBinding;)Z",
            at = @At("HEAD"), cancellable = true)
    public void pre_equals(KeyBinding other, CallbackInfoReturnable<Boolean> cir) {
        // use this to hide key conflicts between RefinedUI keybindings and other keybindings
        // this is a hack
        if (this.translationKey.startsWith("key." + MOD_ID + ".") || (other != null
                && other.getTranslationKey().startsWith("key." + MOD_ID + "."))) {
            cir.setReturnValue((Object) this == other);
            cir.cancel();
        }
    }

}
