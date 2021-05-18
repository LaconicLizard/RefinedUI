package lacliz.refinedui.mixin;

import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EditGameRulesScreen.class)
public interface EditGameRulesScreen_Accessor {

    @Accessor("ruleListWidget")
    EditGameRulesScreen.RuleListWidget getRuleListWidget();

}
