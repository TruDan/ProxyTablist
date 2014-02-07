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
            if (loader != null) {
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

                    while (m.find()) {
                        if (variableContainers[slot] == null) {
                            variableContainers[slot] = new VariableContainer();
                        }

                        variableContainers[slot].addVariableMatch(v, m.toMatchResult());
                        String text = m.replaceFirst("");
                        m = v.getPattern().matcher(text);
                    }
                }

                slot++;
            }
        }
    }

    public void update() {
        int refreshId = getRefreshID();

        //Keep track of the Array slot :)
        int slot = 0;

        for (int r = 0; r < ProxyTablist.getInstance().getTablist().getRows(); r++) {
            for (int c = 0; c < ProxyTablist.getInstance().getTablist().getColumns(); c++) {
                //If there is a new Column which is not in the Config, create a new empty column for it
                if (ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).size() == 0) {
                    ProxyTablist.getInstance().getConfig().set("customcolumns." + (c + 1), new ArrayList<>(new HashSet<>(Arrays.asList(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}))));
                    ProxyTablist.getInstance().saveConfig();
                }

                //Check which Column handles this slot
                String columnvalue = ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r);
                if (variableContainers[slot] == null) {
                    //Check for static text change
                    if ((GlobalTablistView.getSlot(slot) == null && !columnvalue.equals("")) || !GlobalTablistView.getSlot(slot).getText().equals(columnvalue)) {
                        GlobalTablistView.setSlot(slot + 1, columnvalue, (short) 0);
                    }
                } else {
                    VariableContainer currentVariable = variableContainers[slot];

                    //Check each Row first so we check them as they get delivered
                    for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                        Short ping = 0;
                        Boolean global = true;
                        Boolean updated = false;
                        String text = columnvalue;

                        for (int i = 0; i < currentVariable.getVariable().size(); i++) {
                            Variable variable = currentVariable.getVariable().get(i);
                            variable.setMatchResult(currentVariable.getFoundStr().get(i));
                            variable.setRefreshId(refreshId);

                            if(variable.hasUpdate(slot, pp)) {
                                text = text.replace(currentVariable.getFoundStr().get(i).group(), variable.getText(ping));

                                global = global && variable.isForGlobalTablist();
                                updated = true;
                            }
                        }

                        if (updated) {
                            if (global) {
                                GlobalTablistView.setSlot(slot + 1, text, ping);
                            } else {
                                GlobalTablistView.getPlayerTablistView(pp).setSlot(slot + 1, text, ping);
                            }
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
