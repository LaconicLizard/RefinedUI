package lacliz.refinedui;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static lacliz.refinedui.Util.normalizeJsonElement;

public class Config {

    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(RefinedUI.MOD_ID + ".config.json");
    private static Config INSTANCE = null;

    public final boolean rightClickClear;

    /**
     * Construct a Config object, reading from the given mapping from option name -> value.
     * Default values will be filled in automatically.
     *
     * @param config Map from option name -> value of all values to load.  Absent options will be filled with the
     *               default value.
     * @throws ClassCastException       if an option in config has been given an invalid type
     * @throws IllegalArgumentException if an option in config has been given an invalid value
     */
    private Config(Map<String, Object> config) {
        rightClickClear = (boolean) config.getOrDefault("rightClickClear", true);
    }

    public static Config get() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        String json;
        try {
            json = new String(Files.readAllBytes(CONFIG_FILE));
        } catch (NoSuchFileException e) {
            json = "{}";  // will fill resulting Config will all default values
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        INSTANCE = RefinedUI.GSON.fromJson(json, Config.class);
    }

    public void save() throws IOException {
        String json = RefinedUI.GSON.toJson(this);
        Files.write(CONFIG_FILE, json.getBytes());
    }

    // ----- ----- de/serialization ----- -----

    public static final Jsoner JSONER = new Jsoner();

    private static final class Jsoner implements JsonDeserializer<Config>, JsonSerializer<Config> {

        @Override
        public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) throw new JsonParseException("");
            JsonObject o = json.getAsJsonObject();
            Map<String, Object> m = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : o.entrySet()) {
                m.put(entry.getKey(), normalizeJsonElement(entry.getValue()));
            }
            try {
                return new Config(m);
            } catch (ClassCastException | IllegalArgumentException e) {
                throw new JsonParseException("");
            }
        }

        @Override public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject o = new JsonObject();
            o.addProperty("rightClickClear", src.rightClickClear);
            return o;
        }

    }

}
