package eu.scrayos.proxytablist;

import eu.scrayos.proxytablist.handlers.DataHandler;
import eu.scrayos.proxytablist.include.Metrics;
import eu.scrayos.proxytablist.listeners.PlayerDisconnectListener;
import eu.scrayos.proxytablist.listeners.PostLoginListener;
import eu.scrayos.proxytablist.listeners.ServerConnectListener;
import eu.scrayos.proxytablist.objects.Tablist;
import net.craftminecraft.bungee.bungeeyaml.pluginapi.ConfigurablePlugin;
import net.md_5.bungee.api.plugin.Listener;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ProxyTablist extends ConfigurablePlugin implements Listener {

    private static ProxyTablist is;
    private Tablist tl;
    private DataHandler dh;

    @Override
    public void onEnable() {
        is = this;
        new File(getDataFolder() + "/variables").mkdirs();
        saveDefaultConfig();
        tl = new Tablist();
        dh = new DataHandler();
        getProxy().getPluginManager().registerListener(this, new ServerConnectListener());
        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());
        getProxy().getScheduler().schedule(this, new Runnable() {

            @Override
            public void run() {
                tl.refresh();
            }
        }, getConfig().getInt("autorefresh"), getConfig().getInt("autorefresh"), TimeUnit.SECONDS);
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

    public DataHandler getDataHandler() {
        return dh;
    }

    public Tablist getTablist() {
        return tl;
    }
}
