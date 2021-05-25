# RefinedUI

_Miscellaneous unobtrusive user-interface improvements for Minecraft._

This is a Fabric mod.  Forge is not and will not be supported.

THIS MOD IS NOT YET READY FOR PUBLIC USE.  DO NOT USE THIS MOD UNTIL THIS MESSAGE HAS BEEN REMOVED.

This is a client-side mod that fixes a number of minor inconveniences in various Minecraft UIs.  All features are independently configurable.

### Dependencies

This mod requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), 
[Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) and 
[HudElements](https://github.com/LaconicLizard/HudElements).  It is recommended that you also have 
[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) installed, but it is not strictly 
necessary.

### Using this Mod: Players

You can just drop the jar for this mod in your mods folder (make sure to also get the dependencies!) and start playing.  Most features of the mod should be fairly intuitive.  A detailed description is below.  

Currently this mod has the following functionality:

- `textFieldClear` - clear any text field by right-clicking on it
- `cycleButtonBack` - for buttons that cycle through options, right click to cycle backwards
- `hotbarCounts` - displays total number of an item in your inventory over the corresponding hotbar slot
- 'emptySlotCount' - displays the total number of empty slots in inventory to right of hotbar

Every piece of functionality listed above can be enabled or disabled via the configuration file.  Additional options 
may also be present there once further functionality has been added.

Here is detailed list of all configuration options:

- `textFieldClear` (`true` / `false` ; default `true`) - whether the `textFieldClear` functionality is active.  
When active, pressing the bound mouse button on a focused `TextFieldWidget` will clear the contents of that widget.
- `cycleButtonBack` (`true` / `false` ; default `true`) - whether the `cycleButtonBack` functionality is active.  
When active, pressing the bound mouse button on a cyclic options button will cycle it backwards instead of forwards.
- `hotbarCounts` (`true` / `false` ; default `true`) - whether or not `hotbarCounts` functionality is active.  
When active, displays the total number of an item in your inventory over the corresponding hotbar slot.  For example, 
if you have two stacks of bricks in your inventory and are holding one brick in your hotbar, then `129` will be 
displayed over that hotbar slot.
- `hotbarCountsScale` (`float` ; default `0.7f`) - scale of the text of `hotbarCounts`
- `hotbarCountsXOffset`, `hotbarCountsYOffset` (`float` ; default `0`) - offset of the `hotbarCounts` text from 
its default position
- `emptySlotCount` (`true` / `false` ; default `true`) - whether or not `emptySlotCount` functionality is active.  
When active, displays the total number of empty (non-armor) slots in your inventory to the right of the hotbar.  
Includes your offhand, but not any slots added by mods (as it cannot distinguish whether these slots are restricted 
like your armor slots are).
- `emptySlotCountX`, `emptySlotCountY` (`int` ; default `333`, `232`) - position of the `emptySlotCount` 
element on the HUD

### Using this Mod: Modders

To ensure your text fields interact correctly with this mod, simply use or extend `TextFieldWidget` 
(you were probably doing this anyways).  

To ensure that your buttons interact correctly with this mod, you should register all buttons 
that cycle through multiple options with the following code:
```
RefinedUI_API.registerReversibleButton(myButton, (button) -> {
    // code that reverses myButton
});
```

For example, if `myButton` cycles through integer values from 0 to 8, you might have the following:
```
RefinedUI_API.registerReversibleButton(myButton, (button) -> {
    button.value = (button.value - 1 + 8) % 8;
});
```

Make sure to only invoke `registerReversibleButton` if `RefinedUI` is installed!  You can detect this at initialization with `FabricLoader.getInstance().isModLoaded("refinedui")`.

Building against this mod is easy; just add the following to your `build.gradle`:
```
repositories {
    ... other repositories, if any
    maven { url 'https://jitpack.io' }
}

dependencies {
    ... other dependencies
    modCompileOnly "com.github.LaconicLizard:RefinedUI:<version>"
}
```
See the [jitpack website](https://jitpack.io/) for more information on how this works.

### Scope of This Mod

The scope of the mod is limited as follows:

1. Unobtrusive; the improvements the mod makes should be fairly intuitive.  It is not necessary to explicate every 
function of the mod, but most users should be able to figure out how things work without trying.
1. Improvements; the mod will only improve existing UIs and not create any new ones (except possibly as necessary to 
configure the mod itself)
1. User-interface; the mod will only interact with the user-interface.  In particular, inventory automation is highly
unlikely to ever be implemented, though adjacent improvements may be.