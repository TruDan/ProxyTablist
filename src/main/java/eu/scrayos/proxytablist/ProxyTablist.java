package eu.scrayos.proxytablist;

import eu.scrayos.proxytablist.include.Metrics;
import net.craftminecraft.bungee.bungeeyaml.pluginapi.ConfigurablePlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

public class ProxyTablist extends ConfigurablePlugin implements Listener {

    @Override
    public void onEnable() {
        ProxyTablistFormatter.addCodes();
        this.saveDefaultConfig();
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent e) {
    }
}
