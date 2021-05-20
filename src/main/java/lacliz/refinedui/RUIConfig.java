package lacliz.refinedui;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static lacliz.refinedui.RefinedUI.MOD_ID;

@Config(name = MOD_ID)
public class RUIConfig implements ConfigData {

    public boolean textFieldClear = true;
    public boolean cycleButtonBack = true;
    public boolean hotbarCounts = true;

}
