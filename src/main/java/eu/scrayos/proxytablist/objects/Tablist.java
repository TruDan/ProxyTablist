package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;

import java.util.Iterator;

public class Tablist implements CustomTabList {

    public void refresh() {
        Iterator it = ProxyTablist.getInstance().getProxy().getPlayers().iterator();
        for (int c = 0; c < getColumns(); c++) {
            for (int r = 0; r < getRows(); r++) {
                String columnvalue = (ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r));
                if (columnvalue.startsWith("$player")) {
                    //TODO: Get expanded Args
                    setSlot(r, c, (it.hasNext() ? ProxyTablist.getInstance().getDataHandler().formatName((ProxiedPlayer) it.next()) : ""));
                } else if (columnvalue.startsWith("$")) {
                    for (Variable v : ProxyTablist.getInstance().getDataHandler().getVariables()) {
                        if (v.getPattern().matcher(columnvalue.substring(1)).find()) {
                            setSlot(r, c, v.getText(columnvalue.substring(1)));
                        }
                    }
                } else {
                    setSlot(r, c, columnvalue);
                }
            }
        }
    }

    @Override
    public void clear() {

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

    }

    @Override
    public void init(ProxiedPlayer proxiedPlayer) {

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onServerChange() {

    }

    @Override
    public void onPingChange(int i) {
        //DO NOTHING
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public boolean onListUpdate(String s, boolean b, int i) {
        return false;
    }
}
