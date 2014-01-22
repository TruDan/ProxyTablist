package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.objects.Variable;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.logging.Level;

public class DataHandler {

    private final HashSet<Variable> variables;
    private int refreshID;
    private final HashSet<String> strings;

    public DataHandler() {
        variables = new HashSet<>();
        strings = new HashSet<>();
        refreshID = 0;
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

    public int getRefreshID() {
        refreshID++;
        return refreshID - 1;
    }

    public HashSet<Variable> getVariables() {
        return variables;
    }

    public HashSet<String> getStrings() {
        return strings;
    }

    public void addString(String arg) {
        strings.add(arg);
    }

    public void resetStrings() {
        strings.clear();
    }

    public String verifyEntry(String arg) {
        return (arg.length() > 16 ? arg.substring(0, 16) : arg);
    }
}
