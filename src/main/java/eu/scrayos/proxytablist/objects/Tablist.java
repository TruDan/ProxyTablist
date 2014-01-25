package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;

public class Tablist implements CustomTabList {
    @Override
    public synchronized void clear() {
        //Do nothing
    }

    @Override
    public int getColumns() {
        return (int) Math.floor(ProxyTablist.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getTabListSize() / 20);
    }

    @Override
    public int getRows() {
        int size = ProxyTablist.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getTabListSize();
        return (size > 20 ? 20 : size);
    }

    @Override
    public int getSize() {
        return ProxyTablist.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getTabListSize();
    }

    @Override
    public String setSlot(int i, int i2, String s) {
        return setSlot(i, i2, s, true);
    }

    @Override
    public String setSlot(int i, int i2, String s, boolean b) {
        return null;
    }

    @Override
    public void update() {
        //Do nothing
    }

    @Override
    public void init(ProxiedPlayer proxiedPlayer) {
        //DO NOTHING
    }

    @Override
    public void onConnect() {
        //DO NOTHING
    }

    @Override
    public void onServerChange() {
        //DO NOTHING
    }

    @Override
    public void onPingChange(int i) {
        //DO NOTHING
    }

    @Override
    public void onDisconnect() {
        //DO NOTHING
    }

    @Override
    public boolean onListUpdate(String s, boolean b, int i) {
        return false;
    }
}
