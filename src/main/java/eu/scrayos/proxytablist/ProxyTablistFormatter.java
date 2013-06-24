package eu.scrayos.proxytablist;

import java.util.LinkedHashSet;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxyTablistFormatter {

    private ProxyTablist plugin;

    public ProxyTablistFormatter(ProxyTablist plugin) {
        this.plugin = plugin;
    }
    public static LinkedHashSet<String> codes = new LinkedHashSet<String>();

    public static String nameFormatter(ProxiedPlayer player, String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        String name = prefix + player.getName();
        for (String c : codes) {
            if (player.hasPermission("proxy.tablist." + c)) {
                name = "§" + c + name;
            }
        }
        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        return name;
    }

    public static void addCodes() {
        codes.add("r");
        codes.add("k");
        codes.add("o");
        codes.add("n");
        codes.add("m");
        codes.add("l");
        codes.add("f");
        codes.add("e");
        codes.add("d");
        codes.add("c");
        codes.add("b");
        codes.add("a");
        codes.add("9");
        codes.add("8");
        codes.add("7");
        codes.add("6");
        codes.add("5");
        codes.add("4");
        codes.add("3");
        codes.add("2");
        codes.add("1");
        codes.add("0");
    }
}
