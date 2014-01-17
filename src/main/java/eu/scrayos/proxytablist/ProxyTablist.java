package eu.scrayos.proxytablist;

import eu.scrayos.proxytablist.include.Metrics;
import net.craftminecraft.bungee.bungeeyaml.pluginapi.ConfigurablePlugin;
import net.md_5.bungee.api.plugin.Listener;

import java.io.IOException;

public class ProxyTablist extends ConfigurablePlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getProxy().getPluginManager().registerListener(this, this);
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ignored) {
            System.out.println("Failed to initialize Metrics!");
        }
    }
}
