package lacliz.refinedui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import java.io.IOException;

public class RefinedUI implements ModInitializer {

    public static final String MOD_ID = "refinedui";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Config.class, Config.JSONER)
            .create();

    // todo get icon

    @Override public void onInitialize() {
        Config.load();  // to ensure that it is loaded and any issues are detected immediately
        try {  // to write the file so that user can edit it if they want (and see all the options)
            Config.get().save();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

}
