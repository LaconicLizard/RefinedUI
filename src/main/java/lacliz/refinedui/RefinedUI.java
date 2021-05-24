package lacliz.refinedui;

import lacliz.refinedui.internal.EmptySlotsHudElement;
import lacliz.refinedui.internal.RUIConfig;
import lacliz.refinedui.internal.RUIKeybinds;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RefinedUI implements ModInitializer {

    public static final String MOD_ID = "refinedui";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override public void onInitialize() {
        // initialize keybinds
        RUIKeybinds.initialize();
        // register config, load it
        AutoConfig.register(RUIConfig.class, GsonConfigSerializer::new);
        ConfigHolder<RUIConfig> ch = AutoConfig.getConfigHolder(RUIConfig.class);
        ch.registerLoadListener((holder, config) -> {
            if (config.emptySlotCount) {
                EmptySlotsHudElement.INSTANCE.enable();
            } else {
                EmptySlotsHudElement.INSTANCE.disable();
            }
            return ActionResult.SUCCESS;
        });
        ch.load();
        CONFIG_HOLDER = ch;
    }

    private static ConfigHolder<RUIConfig> CONFIG_HOLDER = null;

    public static RUIConfig getConfig() {
        return getConfigHolder().get();
    }

    public static ConfigHolder<RUIConfig> getConfigHolder() {
        ConfigHolder<RUIConfig> ch;
        while ((ch = CONFIG_HOLDER) == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        return ch;
    }

}
