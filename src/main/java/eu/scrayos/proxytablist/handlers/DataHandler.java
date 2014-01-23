package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.api.Variable;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class DataHandler {

    private final HashSet<Variable> variables;
    private int refreshID;
    private final HashMap<ProxiedPlayer, HashSet<String>> columnCache;

    public DataHandler() {
        variables = new HashSet<>();
        columnCache = new HashMap<>();
        refreshID = 0;

        File[] files = new File(ProxyTablist.getInstance().getDataFolder() + "/variables").listFiles();
        if (files != null) {
            HashSet<URL> urls = new HashSet<>(files.length);
            for (File file : files) {
                try {
                    if (file.getName().endsWith(".jar")) {
                        urls.add(file.toURI().toURL());
                    }
                } catch (MalformedURLException ignored) {
                }
            }
            ClassLoader loader = null;
            try {
                loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), Variable.class.getClassLoader());
            } catch (Exception ignored) {
            }
            for (File file : files) {
                try {
                    if (!file.getName().endsWith(".jar")) {
                        continue;
                    }
                    Class<?> aClass = loader.loadClass(file.getName().substring(0, file.getName().lastIndexOf(".")));
                    Object object = aClass.newInstance();
                    if (!(object instanceof Variable)) {
                        ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (No Variable)");
                        continue;
                    }
                    variables.add((Variable) object);
                } catch (Exception ex) {
                    ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (Unspecified Error)");
                    ex.printStackTrace();
                }
            }
        }
    }

    public int getRefreshID() {
        refreshID++;
        return refreshID - 1;
    }

    public HashSet<Variable> getVariables() {
        return variables;
    }

    public HashSet<String> getStrings(ProxiedPlayer pp) {
        if (!columnCache.containsKey(pp)) {
            columnCache.put(pp, new HashSet<String>());
        }
        return columnCache.get(pp);
    }

    public void addString(String arg, ProxiedPlayer pp) {
        if (!columnCache.containsKey(pp)) {
            columnCache.put(pp, new HashSet<String>());
        }
        columnCache.get(pp).add(arg);
    }

    public void resetStrings(ProxiedPlayer pp) {
        columnCache.remove(pp);
    }

    public String verifyEntry(String arg) {
        return (arg.length() > 16 ? arg.substring(0, 16) : arg);
    }
}
