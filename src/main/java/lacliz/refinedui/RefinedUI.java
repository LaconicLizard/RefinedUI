package lacliz.refinedui;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class RefinedUI implements ModInitializer {

    public static final String MOD_ID = "refinedui";

    @Override public void onInitialize() {
        // register config, load it
        AutoConfig.register(RUIConfig.class, GsonConfigSerializer::new);
        CONFIG_HOLDER = AutoConfig.getConfigHolder(RUIConfig.class);
        CONFIG_HOLDER.get();
    }

    private static ConfigHolder<RUIConfig> CONFIG_HOLDER = null;

    public static RUIConfig getConfig() {
        ConfigHolder<RUIConfig> ch;
        while ((ch = CONFIG_HOLDER) == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        return ch.get();
    }

}
