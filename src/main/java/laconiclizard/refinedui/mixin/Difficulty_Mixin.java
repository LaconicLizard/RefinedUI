package laconiclizard.refinedui.mixin;

import laconiclizard.refinedui.accessors.Difficulty_Accessor;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Difficulty.class)
public abstract class Difficulty_Mixin implements Difficulty_Accessor {

    @Shadow @Final private int id;

    @Accessor("BY_NAME")
    public static Difficulty[] getBY_NAME() {
        throw new AssertionError("");
    }

    @Override public Difficulty cycleBack() {
        Difficulty[] diffs = getBY_NAME();
        return diffs[(this.id - 1 + diffs.length) % diffs.length];
    }

}
