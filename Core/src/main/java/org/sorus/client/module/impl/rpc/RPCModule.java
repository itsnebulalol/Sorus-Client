package org.sorus.client.module.impl.rpc;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.settings.Setting;

public class RPCModule extends ModuleConfigurable {


    public RPCModule() {
        super("DISCORD RPC");
    }

    @Override
    public void addConfigComponents(Collection collection) {
    }

    @Override
    public void onEnable() {
        Sorus.getSorus().getSorusRpc().init();
    }

    @Override
    public void onLoad() {
        if(isEnabled()) {
            Sorus.getSorus().getSorusRpc().init();
        }
    }

    @Override
    public void onDisable() {
        Sorus.getSorus().getSorusRpc().shutdown();
    }


}
