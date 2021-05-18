# RefinedUI

*Miscellaneous unobtrusive user-interface improvements for Minecraft.*

This is a small client-side mod that fixes a number of minor inconveniences I have experienced with various Minecraft UIs.  

The scope of the mod is limited as follows:

1. Unobtrusive; the improvements the mod makes should be fairly intuitive.  It is not necessary to explicate every function of the mod, but no most users should be able to figure out how things work without trying.
1. Improvements; the mod will only improve existing UIs and not create any new ones (except possibly as necessary to configure the mod itself)
1. User-interface; the mod will only interact with the user-interface.  In particular, inventory automation will not be provided except when specifically invoked by the user (eg. press a button to use item in off hand, even if item in main hand can be used)

Currently it has the following functionality:

- `textFieldClear` - clear any text field by right-clicking on it
- `cycleButtonBack` - for buttons that cycle through options, right click to cycle backwards

Every piece of functionality listed above can be enabled or disabled via the configuration file.  Additional options may also be present there once additional functionality has been added.

Here is detailed list of all configuration options:

- `textFieldClear` (`true` / `false` ; default `true`) - whether the `textFieldClear` functionality is active.  When active, pressing the bound mouse button on a focused `TextFieldWidget` will clear the contents of that widget.
- `cycleButtonBack` (`true` / `false` ; default `true`) - whether the `cycleButtonBack` functionality is active.  When active, pressing the bound mouse button on a cyclic options button will cycle it backwards instead of forwards.
