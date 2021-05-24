package lacliz.refinedui.internal;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static lacliz.refinedui.RefinedUI.MOD_ID;

@Config(name = MOD_ID)
public class RUIConfig implements ConfigData {

    public boolean textFieldClear = true;
    public boolean cycleButtonBack = true;
    public boolean hotbarCounts = true;
    public float hotbarCountsScale = 0.7f;
    public int hotbarCountsXOffset = 0, hotbarCountsYOffset = 0;

    public boolean emptySlotCount = true;
    @ConfigEntry.Gui.Excluded
    public int emptySlotCountX = 333, emptySlotCountY = 232;

}
