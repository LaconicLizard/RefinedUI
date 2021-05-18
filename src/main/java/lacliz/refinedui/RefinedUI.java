package lacliz.refinedui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.options.ControlsListWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.world.GameMode;

import java.io.IOException;

public class RefinedUI implements ModInitializer {

    public static final String MOD_ID = "refinedui";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Config.class, Config.JSONER)
            .create();

    @Override public void onInitialize() {
        Config.load();  // to ensure that it is loaded and any issues are detected immediately
        try {  // to write the file so that user can edit it if they want (and see all the options)
            Config.get().save();
        } catch (IOException e) {
            throw new Error(e);
        }
        
    }

}
