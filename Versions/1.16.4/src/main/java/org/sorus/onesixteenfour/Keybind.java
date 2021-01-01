

package org.sorus.onesixteenfour;

import net.minecraft.client.settings.KeyBinding;
import org.sorus.client.version.input.IKeybind;
import org.sorus.client.version.input.Key;
import org.sorus.onesixteenfour.util.input.InputMap;

public class Keybind implements IKeybind {

    private final KeyBinding keyBinding;

    public Keybind(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    @Override
    public Key getKey() {
        return InputMap.getKey(keyBinding.getDefault().getKeyCode());
    }

    @Override
    public void setState(boolean state) {
        KeyBinding.setKeyBindState(keyBinding.getDefault(), state);
    }

}
