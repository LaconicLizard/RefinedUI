package laconiclizard.refinedui;

import laconiclizard.refinedui.internal.EmptySlotsHudElement;
import laconiclizard.refinedui.internal.RUIConfig;
import laconiclizard.refinedui.internal.RUIKeybinds;
import laconiclizard.hudelements.api.HudElement;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class RefinedUI implements ModInitializer {

    public static final String MOD_ID = "refinedui";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final Map<ButtonWidget, Consumer<ButtonWidget>> API_BUTTONS
            = Collections.synchronizedMap(new WeakHashMap<>());

    @Override public void onInitialize() {
        // initialize keybinds
        RUIKeybinds.initialize();
        // register config, load it
        AutoConfig.register(RUIConfig.class, GsonConfigSerializer::new);
        ConfigHolder<RUIConfig> ch = AutoConfig.getConfigHolder(RUIConfig.class);
        ch.registerLoadListener((holder, config) -> {
            HudElement he = EmptySlotsHudElement.INSTANCE;
            if (config.emptySlotCount) {
                he.enable();
            } else {
                he.disable();
            }
            he.setPos(config.emptySlotCountX, config.emptySlotCountY);
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
