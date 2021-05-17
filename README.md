# RefinedUI

*Miscellaneous unobtrusive user-interface improvements for Minecraft.*

This is a small client-side mod that fixes a number of minor inconveniences I have experienced with various Minecraft UIs.  Currently it has the following functionality:

- `textFieldClear` - clear any text field by right-clicking on it

Every piece of functionality listed above can be enabled or disabled via the configuration file.  Additional options are also present there, such as controlling which mouse button invokes `textFieldClear`.

Here is detailed list of all configuration options:

- `textFieldClear` (`true` / `false` ; default `true`) - whether the `textFieldClear` functionality is active.  When active, pressing the specified mouse button (`textFieldClear_button`) on a focused `TextFieldWidget` will clear the contents of that widget.  
More simply, right-click a place where you type text to erase all of the text currently there.
- `textFieldClear_button` (`int` within `[0,16)` ; default `1`) The mouse button to press to invoke `textFieldClear`.  Defaults to `1`, which is the right mouse button.  Other common values are: `0` for left mouse button, and `2` for middle mouse button (usually).  If you have more than three buttons on your mouse, you likely already know how to identify their codes.
