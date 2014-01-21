package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.objects.Variable;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.logging.Level;

public class DataHandler {

    private final HashSet<Variable> variables;

    public DataHandler() {
        variables = new HashSet<>();
        loadVariables();
    }

    public HashSet<Variable> getVariables() {
        return variables;
    }

    public String formatName(ProxiedPlayer p) {
        String name = p.getName();
        for (String c : new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "l", "m", "n", "o", "k", "r"}) {
            name = (p.hasPermission("proxy.tablist." + c) ? "§" + c + name : name);
        }
        String prefix = ProxyTablist.getInstance().getConfig().getString("prefixes." + p.getServer().getInfo().getName());
        return (prefix != null ? prefix + name : name);
    }

    public void loadVariables() {
        ClassLoader loader = null;
        try {
            loader = new URLClassLoader(new URL[]{new File(ProxyTablist.getInstance().getDataFolder() + "/variables").toURI().toURL()}, Variable.class.getClassLoader());
        } catch (Exception ignored) {
        }
        for (File file : new File(ProxyTablist.getInstance().getDataFolder() + "/variables").listFiles()) {
            try {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                Class<?> clazz = loader.loadClass(name);
                Object object = clazz.newInstance();
                if (!(object instanceof Variable)) {
                    ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (No Variable)");
                    continue;
                }
                variables.add((Variable) object);
            } catch (Exception ignored) {
                ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (Unspecified Error)");
            }
        }
    }
}
