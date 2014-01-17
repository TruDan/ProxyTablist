package eu.scrayos.proxytablist.handlers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.TabListAdapter;

public class ProxyTablistHandler extends TabListAdapter {

    @Override
    public boolean onListUpdate(String s, boolean b, int i) {
        return false;
    }

    private String formatName(ProxiedPlayer p, String prefix) {
        String name = p.getName();
        for (String c : new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "l", "m", "n", "o", "k", "r"}) {
            if (p.hasPermission("proxy.tablist." + c)) {
                name = "§" + c + name;
            }
        }
        return ((prefix + name).length() > 16 ? (prefix + name).substring(0, 16) : (prefix + name));
    }
}
