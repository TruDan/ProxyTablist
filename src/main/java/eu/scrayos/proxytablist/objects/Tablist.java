package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;
import net.md_5.bungee.protocol.packet.PlayerListItem;

public class Tablist implements CustomTabList {

    public void refresh() {
        clear();
        int refreshID = ProxyTablist.getInstance().getDataHandler().getRefreshID();
        for (int c = 0; c < getColumns(); c++) {
            for (int r = 0; r < getRows(); r++) {
                String columnvalue = (ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r));
                if (columnvalue.startsWith("$")) {
                    boolean placed = false;
                    for (Variable v : ProxyTablist.getInstance().getDataHandler().getVariables()) {
                        if (v.getPattern().matcher(columnvalue.substring(1)).find()) {
                            for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                                pp.unsafe().sendPacket(new PlayerListItem(v.getText(columnvalue.substring(1), refreshID), true, (short) 0));
                            }
                            placed = true;
                        }
                    }
                    if (!placed) {
                        for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                            pp.unsafe().sendPacket(new PlayerListItem(columnvalue, true, (short) 0));
                        }
                    }
                } else {
                    for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                        pp.unsafe().sendPacket(new PlayerListItem(columnvalue, true, (short) 0));
                    }
                }
            }
        }
    }

    @Override
    public void clear() {
        for (String s : ProxyTablist.getInstance().getDataHandler().getStrings()) {
            for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                pp.unsafe().sendPacket(new PlayerListItem(s, false, (short) 0));
            }
        }
        ProxyTablist.getInstance().getDataHandler().resetStrings();
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
        //EMPTY-FUNCTION
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
