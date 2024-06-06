package mdk.whitelist.bungee;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event implements Listener {
    public static boolean b(String name) {
        if (WhiteListPlugin.config.getConfig().eneble_check_Name) {
            char[] chars = name.toCharArray();
            char[] cr = WhiteListPlugin.config.getConfig().char_set.toCharArray();
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
    @EventHandler(
            priority = 5
    )
    public void onLogin(PreLoginEvent event) {
        PendingConnection c = event.getConnection();
        if (!b(c.getName())) {
            c.disconnect(new TextComponent(WhiteListPlugin.lang.format("char.kick")));
            event.setCancelled(true);
        }
        if (WhiteListPlugin.config.getConfig().enable) {
            if (!(WhiteListPlugin.list.is(c.getName()))) {
                c.disconnect(new TextComponent(WhiteListPlugin.lang.format("whitelist.kick")));
                event.setCancelled(true);
            }
        }
    }
}
