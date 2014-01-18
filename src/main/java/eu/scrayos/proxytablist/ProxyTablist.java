package eu.scrayos.proxytablist;

import eu.scrayos.proxytablist.handlers.ProxyTablistHandler;
import eu.scrayos.proxytablist.include.Metrics;
import eu.scrayos.proxytablist.listeners.ServerConnectListener;
import net.craftminecraft.bungee.bungeeyaml.pluginapi.ConfigurablePlugin;
import net.md_5.bungee.api.plugin.Listener;

import java.io.IOException;

public class ProxyTablist extends ConfigurablePlugin implements Listener {

    private static ProxyTablist is;
    private ProxyTablistHandler th;

    @Override
    public void onEnable() {
        is = this;
        th = new ProxyTablistHandler();
        saveDefaultConfig();
        getProxy().getPluginManager().registerListener(this, new ServerConnectListener());
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ignored) {
            System.out.println("Failed to initialize Metrics!");
        }
    }

    public static ProxyTablist getInstance() {
        return is;
    }

    public ProxyTablistHandler getTablistHandler() {
        return th;
    }
}
