package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.api.Variable;
import eu.scrayos.proxytablist.objects.GlobalTablistView;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.regex.Matcher;

public class DataHandler {
    private HashSet<Variable> loadedVariables = new HashSet<>();
    private VariableContainer[] variableContainers;
    private int refreshID = 0;

    public DataHandler() {
        //Load variables
        loadVariables();

        //Read in the config
        loadConfigTablist();
    }

    public void loadVariables() {
        File[] files = new File(ProxyTablist.getInstance().getDataFolder() + "/variables").listFiles();
        if (files != null) {
            //Check for Files who end with a .jar and add them to the be loaded list
            HashSet<URL> urls = new HashSet<>(files.length);
            for (File file : files) {
                try {
                    if (file.getName().endsWith(".jar")) {
                        urls.add(file.toURI().toURL());
                    }
                } catch (MalformedURLException ignored) {
                }
            }

            //Try to build the Classloader with which the JARs should be loaded
            ClassLoader loader = null;
            try {
                loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), Variable.class.getClassLoader());
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

            //If there is a valid Classloader load the jars
            if(loader != null) {
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

                        loadedVariables.add((Variable) object);
                    } catch (Exception ex) {
                        ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (Unspecified Error)");
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This reads in the current custom columns and parses them into the correct Variable
     */
    public void loadConfigTablist() {
        variableContainers = new VariableContainer[ProxyTablist.getInstance().getTablist().getSize()];

        //Keep track of the Array slot :)
        int slot = 0;

        //Check each Row first so we check them as they get delivered
        for (int r = 0; r < ProxyTablist.getInstance().getTablist().getRows(); r++) {
            for (int c = 0; c < ProxyTablist.getInstance().getTablist().getColumns(); c++) {
                //If there is a new Column which is not in the Config, create a new empty column for it
                if (ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).size() == 0) {
                    ProxyTablist.getInstance().getConfig().set("customcolumns." + (c + 1), new ArrayList<>(new HashSet<>(Arrays.asList(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}))));
                    ProxyTablist.getInstance().saveConfig();
                }

                //Check which Column handles this slot
                String columnvalue = ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r);
                for (Variable v : loadedVariables) {
                    Matcher m = v.getPattern().matcher(columnvalue);

                    if(m.find()) {
                        variableContainers[slot] = new VariableContainer(v, m.group());
                        break;
                    }
                }

                slot++;
            }
        }
    }

    public void update() {
        //Keep track of the Array slot :)
        int slot = 0;

        int refreshId = getRefreshID();

        //Check each Row first so we check them as they get delivered
        for (int r = 0; r < ProxyTablist.getInstance().getTablist().getRows(); r++) {
            for (int c = 0; c < ProxyTablist.getInstance().getTablist().getColumns(); c++) {
                //If there is a new Column which is not in the Config, create a new empty column for it
                if (ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).size() == 0) {
                    ProxyTablist.getInstance().getConfig().set("customcolumns." + (c + 1), new ArrayList<>(new HashSet<>(Arrays.asList(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}))));
                    ProxyTablist.getInstance().saveConfig();
                }

                //Check which Column handles this slot
                String columnvalue = ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r);
                if(variableContainers[slot] == null) {
                    //Check for static text change
                    if((GlobalTablistView.getSlot(slot+1) == null && !columnvalue.equals("")) || !GlobalTablistView.getSlot(slot+1).equals(columnvalue)) {
                        GlobalTablistView.setSlot(slot+1, columnvalue, (short) 0);
                    }
                } else {
                    VariableContainer currentVariable = variableContainers[slot];

                    for(ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                        Short ping = 0;
                        Boolean global = true;
                        String text = currentVariable.getVariable().getText(currentVariable.getFoundStr(), refreshId, ping, pp, global);

                        if(global) {
                            GlobalTablistView.setSlot(slot+1, text, ping);
                        } else {
                            GlobalTablistView.getPlayerTablistView(pp).setSlot(slot+1, text, ping);
                        }
                    }
                }

                slot++;
            }
        }

        //Let the Tablist refresh
        GlobalTablistView.fireUpdate();
    }

    public int getRefreshID() {
        refreshID++;
        return refreshID - 1;
    }
}
