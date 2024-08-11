WhiteList Reload It checks players not by username and UUID, but by username. This fixes an issue on pirate servers where adding a player to the whitelist results in a connection error stating that the player is not on the whitelist.
Dency: https://github.com/tapo4ok/MUtilsGlobal


Permission: rwhitelist
Rwhitelist:
- off
- on
- add <username>
- remove <username>
- list
- char <true/false>
- load

The list of players is stored in the file "{serverdir}/whitelist_reload.json". There are additional functions to check the player's nickname for valid characters, the default value is false.

Supports: Spigot, Paper, BungeeCord
Plans:
- Make plugin settings more flexible
- Optimization
- Add new functionality

P.S. the plugin is written for use on my project and adapted for public use, so don't be surprised at how the configs look
