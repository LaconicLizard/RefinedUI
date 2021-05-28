package laconiclizard.refinedui.mixin;

import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(MoreOptionsDialog.class)
public interface MoreOptionsDialog_Accessor {

    @Accessor("mapFeaturesButton")
    ButtonWidget getMapFeaturesButton();

    @Accessor("bonusItemsButton")  // this one's actually public, but we'll put it here anyways for ease of use
    ButtonWidget getBonusItemsButton();

    @Accessor("mapTypeButton")
    ButtonWidget getMapTypeButton();

    @Accessor("generatorType")
    Optional<GeneratorType> getGeneratorType();

    @Accessor("generatorType")
    void setGeneratorType(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<GeneratorType> v);

    @Accessor("generatorOptions")
    GeneratorOptions getGeneratorOptions();

    @Accessor("generatorOptions")
    void setGeneratorOptions(GeneratorOptions v);

    @Accessor("registryManager")
    DynamicRegistryManager.Impl getRegistryManager();

}
