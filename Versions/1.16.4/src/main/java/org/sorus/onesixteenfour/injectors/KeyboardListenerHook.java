package org.sorus.onesixteenfour.injectors;

import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.KeyReleaseEvent;
import org.sorus.onesixteenfour.util.input.InputMap;

public class KeyboardListenerHook {

    public static void onKeyEvent(int key, int scanCode, int action, int modifiers) {
        switch(action) {
            case 0:
                Sorus.getSorus().getEventManager().post(new KeyPressEvent(InputMap.getKey(key), 'a', false));
                break;
            case 1:
                Sorus.getSorus().getEventManager().post(new KeyReleaseEvent(InputMap.getKey(key)));
                break;
            case 2:
                Sorus.getSorus().getEventManager().post(new KeyPressEvent(InputMap.getKey(key), 'a', true));
                break;
        }
    }

}
