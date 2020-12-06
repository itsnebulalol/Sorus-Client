package org.sorus.client.util;

import org.sorus.client.Sorus;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;

public class KeyHelper {

    public static String getTypedChar(Key key) {
        if(key.getName().length() == 1) {
            IInput input = Sorus.getSorus().getVersion().getData(IInput.class);
            if(input.isKeyDown(Key.CONTROL_LEFT) || input.isKeyDown(Key.CONTROL_RIGHT)) {
                return "";
            }
            String typedChar = key.getName().toLowerCase();
            if(input.isKeyDown(Key.SHIFT_LEFT) || input.isKeyDown(Key.SHIFT_RIGHT)) {
                typedChar = typedChar.toUpperCase();
            }
            return typedChar;
        }
        return "";
    }

}
