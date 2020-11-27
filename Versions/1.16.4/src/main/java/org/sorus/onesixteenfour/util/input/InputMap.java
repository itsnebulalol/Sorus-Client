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

package org.sorus.onesixteenfour.util.input;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.Key;

/**
 * Provides mappings of input from the LWJGL2 keycode to the {@link Key} enum.
 */
public class InputMap {

    /** Mappings for keys. */
    private static final BiMap<Integer, Key> KEY_MAP = HashBiMap.create();

    static {
        KEY_MAP.put(256, Key.ESCAPE);
        KEY_MAP.put(290, Key.F1);
        KEY_MAP.put(291, Key.F2);
        KEY_MAP.put(292, Key.F3);
        KEY_MAP.put(293, Key.F4);
        KEY_MAP.put(294, Key.F5);
        KEY_MAP.put(295, Key.F6);
        KEY_MAP.put(296, Key.F7);
        KEY_MAP.put(297, Key.F8);
        KEY_MAP.put(298, Key.F9);
        KEY_MAP.put(299, Key.F10);
        KEY_MAP.put(300, Key.F11);
        KEY_MAP.put(301, Key.F12);
        KEY_MAP.put(96, Key.GRAVE);
        KEY_MAP.put(49, Key.ONE);
        KEY_MAP.put(50, Key.TWO);
        KEY_MAP.put(51, Key.THREE);
        KEY_MAP.put(52, Key.FOUR);
        KEY_MAP.put(53, Key.FIVE);
        KEY_MAP.put(54, Key.SIX);
        KEY_MAP.put(55, Key.SEVEN);
        KEY_MAP.put(56, Key.EIGHT);
        KEY_MAP.put(57, Key.NINE);
        KEY_MAP.put(48, Key.ZERO);
        KEY_MAP.put(45, Key.MINUS);
        KEY_MAP.put(61, Key.EQUALS);
        KEY_MAP.put(259, Key.BACKSPACE);
        KEY_MAP.put(258, Key.TAB);
        KEY_MAP.put(81, Key.Q);
        KEY_MAP.put(87, Key.W);
        KEY_MAP.put(69, Key.E);
        KEY_MAP.put(82, Key.R);
        KEY_MAP.put(84, Key.T);
        KEY_MAP.put(89, Key.Y);
        KEY_MAP.put(85, Key.U);
        KEY_MAP.put(73, Key.I);
        KEY_MAP.put(79, Key.O);
        KEY_MAP.put(80, Key.P);
        KEY_MAP.put(91, Key.BRACKET_OPEN);
        KEY_MAP.put(93, Key.BRACKET_CLOSE);
        KEY_MAP.put(92, Key.SLASH_BACK);
        KEY_MAP.put(280, Key.CAPSLOCK);
        KEY_MAP.put(65, Key.A);
        KEY_MAP.put(83, Key.S);
        KEY_MAP.put(68, Key.D);
        KEY_MAP.put(70, Key.F);
        KEY_MAP.put(71, Key.G);
        KEY_MAP.put(72, Key.H);
        KEY_MAP.put(74, Key.J);
        KEY_MAP.put(75, Key.K);
        KEY_MAP.put(76, Key.L);
        KEY_MAP.put(59, Key.SEMICOLON);
        KEY_MAP.put(39, Key.APOSTROPHE);
        KEY_MAP.put(257, Key.ENTER);
        KEY_MAP.put(340, Key.SHIFT_LEFT);
        KEY_MAP.put(90, Key.Z);
        KEY_MAP.put(88, Key.X);
        KEY_MAP.put(67, Key.C);
        KEY_MAP.put(86, Key.V);
        KEY_MAP.put(66, Key.B);
        KEY_MAP.put(78, Key.N);
        KEY_MAP.put(77, Key.M);
        KEY_MAP.put(44, Key.COMMA);
        KEY_MAP.put(46, Key.PERIOD);
        KEY_MAP.put(47, Key.SLASH_FORWARD);
        KEY_MAP.put(344, Key.SHIFT_RIGHT);
        KEY_MAP.put(341, Key.CONTROL_LEFT);
        KEY_MAP.put(219, Key.META_LEFT);
        KEY_MAP.put(342, Key.ALT_LEFT);
        KEY_MAP.put(32, Key.SPACE);
        KEY_MAP.put(346, Key.ALT_RIGHT);
        KEY_MAP.put(347, Key.META_RIGHT);
        KEY_MAP.put(348, Key.MENU);
        KEY_MAP.put(345, Key.CONTROL_RIGHT);
    }

    /** Mappings for mouse buttons. */
    private static final BiMap<Integer, Button> BUTTON_MAP = HashBiMap.create();

    static {
        BUTTON_MAP.put(0, Button.ONE);
    }

    public static Key getKey(int keyCode) {
        return KEY_MAP.getOrDefault(keyCode, Key.NULL);
    }

    public static int getKeyCode(Key key) {
        return KEY_MAP.inverse().get(key);
    }

    public static Button getButton(int buttonCode) {
        return BUTTON_MAP.getOrDefault(buttonCode, Button.NULL);
    }

    public static int getButtonCode(Button button) {
        return BUTTON_MAP.inverse().get(button);
    }

}
