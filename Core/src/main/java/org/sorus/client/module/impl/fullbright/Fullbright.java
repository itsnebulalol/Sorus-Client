package org.sorus.client.module.impl.fullbright;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.settings.Setting;

public class Fullbright extends ModuleConfigurable {

    public Fullbright() {
        super("FULLBRIGHT");
    }

    @Override
    public void onEnable() {
        Sorus.getSorus().getVersion().getGame().toggleFullbright(true);
    }

    @Override
    public void onDisable() {
        Sorus.getSorus().getVersion().getGame().toggleFullbright(false);
    }
}
