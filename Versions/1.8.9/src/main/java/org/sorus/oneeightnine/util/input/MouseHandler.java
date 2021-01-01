

package org.sorus.oneeightnine.util.input;

import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.IInput;


public class MouseHandler {


    public static void handleMouse(Button button, boolean buttonState) {
        if(buttonState) {
            Sorus.getSorus().getEventManager().post(new MousePressEvent(button, Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(), Sorus.getSorus().getVersion().getData(IInput.class).getMouseY()));
        } else if(button != Button.NULL) {
            Sorus.getSorus().getEventManager().post(new MouseReleaseEvent(button));
        }
    }

}
