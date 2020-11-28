/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.oneeightnine.util.input;

import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.IInput;

/**
 * Handles mouse input and calls the proper event based on the mouse input.
 */
public class MouseHandler {

    /**
     * Handles mouse input based on the {@link Button} and whether it is pressed or not.
     * @param button the button whos state is being altered
     * @param buttonState the new button state, {@code true} or {@code false}
     */
    public static void handleMouse(Button button, boolean buttonState) {
        if(buttonState) {
            Sorus.getSorus().getEventManager().post(new MousePressEvent(button, Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(), Sorus.getSorus().getVersion().getData(IInput.class).getMouseY()));
        } else if(button != Button.NULL) {
            Sorus.getSorus().getEventManager().post(new MouseReleaseEvent(button));
        }
    }

}
