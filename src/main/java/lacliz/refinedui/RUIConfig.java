package lacliz.refinedui;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

import static lacliz.refinedui.RefinedUI.MOD_ID;

@Config(name = MOD_ID)
public class RUIConfig implements ConfigData {

    @Tooltip
    public boolean textFieldClear = true;
    @Tooltip
    public boolean cycleButtonBack = true;
    @Tooltip
    public boolean hotbarCounts = true;

}
