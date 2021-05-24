# RefinedUI

*Miscellaneous unobtrusive user-interface improvements for Minecraft.*

This is a small client-side mod that fixes a number of minor inconveniences I have experienced with various 
Minecraft UIs.  

This mod requires `cloth-config`.  It is recommended that you also have `modmenu` installed, but it is not strictly 
necessary.

The scope of the mod is limited as follows:

1. Unobtrusive; the improvements the mod makes should be fairly intuitive.  It is not necessary to explicate every 
function of the mod, but most users should be able to figure out how things work without trying.
1. Improvements; the mod will only improve existing UIs and not create any new ones (except possibly as necessary to 
configure the mod itself)
1. User-interface; the mod will only interact with the user-interface.  In particular, inventory automation is highly
unlikely to ever be implemented, though adjacent improvements may be.

Currently it has the following functionality:

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
- `emptySlotCount` (`true` / `false` ; default `true`) - whether or not `emptySlotCount` functionality is active.  
When active, displays the total number of empty (non-armor) slots in your inventory to the right of the hotbar.  
Includes your offhand, but not any slots added by mods (as it cannot distinguish whether these slots are restricted 
like your armor slots are).

### For Modders

To ensure your text fields interact correctly with this mod, simply use or extend `TextFieldWidget` 
(you were probably doing this anyways).  

To ensure that your buttons interact correctly with this mod, you should use `ReversibleCyclicButton` on all buttons 
that cycle through multiple options.  For example:
```
// old code:
ButtonWidget buttonWidget = new ButtonWidget(..., (widget) -> {
    this.value = (this.value + 1) % this.size;  // this moves value "forward"
});

// new code:
ButtonWidget buttonWiget = new ReversibleCyclicButton(..., (widget) -> {
    this.value = (this.value + 1) % this.size;
}, (widget) -> {
    this.value = (this.value - 1) % this.size;  // this moves value "backwards"
});
```
In the event that you are already using a non-`ButtonWidget` superclass, you may implement `ReversibleCyclicButtonI` to get the same functionality on your custom subclass:
```
public class MyButtonWidget extends MyOtherButtonWidget implements ReversibleCyclicButtonI {
    ...
    void cycleBackwards() {
        ...  // cycle this button backwards by one
    }
}
``` 