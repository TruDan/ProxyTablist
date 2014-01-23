package eu.scrayos.proxytablist.listeners;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectEvent e) {
        ProxyTablist.getInstance().getProxy().getScheduler().schedule(ProxyTablist.getInstance(), new Runnable() {

            @Override
            public void run() {
                ProxyTablist.getInstance().getTablist().refresh();
            }
        }, 100, TimeUnit.MILLISECONDS);
        ProxyTablist.getInstance().getTablist().refresh();
    }
}
