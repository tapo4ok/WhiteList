package mdk.whitelist;

import mdk.mutils.api.config.Static;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Event implements Listener {

    public static boolean b(String name) {
        if (WhiteList.config.getConfig().eneble_check_Name) {
            char[] chars = name.toCharArray();
            char[] cr = WhiteList.config.getConfig().char_set.toCharArray();
            for (char c : chars) {
                boolean found = false;
                for (char aCr : cr) {
                    if (c == aCr) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
        }
        return true;
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onConnect(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (p != null) {
            WhiteListConfig config = WhiteList.config.getConfig();
            if (!b(p.getName())) {
                e.disallow(PlayerLoginEvent.Result.KICK_FULL, Static.lang.format("char.kick"));
            }
            if (config.enable) {
                if (!(WhiteList.list.contains(p.getName()))) {
                    e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, Static.lang.format("whitelist.kick"));
                }
            }
        }
    }
}
