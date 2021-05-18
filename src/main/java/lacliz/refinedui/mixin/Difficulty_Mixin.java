package lacliz.refinedui.mixin;

import lacliz.refinedui.accessors.Difficulty_Accessor;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Difficulty.class)
public abstract class Difficulty_Mixin implements Difficulty_Accessor {

    @Shadow public abstract int getId();

    @Accessor("BY_NAME")
    public static Difficulty[] getBY_NAME() {
        throw new AssertionError("");
    }

    @Override public Difficulty cycleBack() {
        Difficulty[] diffs = getBY_NAME();
        return diffs[(this.getId() - 1 + diffs.length) % diffs.length];
    }

}
