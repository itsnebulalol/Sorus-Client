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
        KEY_MAP.put(1, Key.ESCAPE);
        KEY_MAP.put(59, Key.F1);
        KEY_MAP.put(60, Key.F2);
        KEY_MAP.put(61, Key.F3);
        KEY_MAP.put(62, Key.F4);
        KEY_MAP.put(63, Key.F5);
        KEY_MAP.put(64, Key.F6);
        KEY_MAP.put(65, Key.F7);
        KEY_MAP.put(66, Key.F8);
        KEY_MAP.put(67, Key.F9);
        KEY_MAP.put(68, Key.F10);
        KEY_MAP.put(87, Key.F11);
        KEY_MAP.put(88, Key.F12);
        KEY_MAP.put(41, Key.GRAVE);
        KEY_MAP.put(2, Key.ONE);
        KEY_MAP.put(3, Key.TWO);
        KEY_MAP.put(4, Key.THREE);
        KEY_MAP.put(5, Key.FOUR);
        KEY_MAP.put(6, Key.FIVE);
        KEY_MAP.put(7, Key.SIX);
        KEY_MAP.put(8, Key.SEVEN);
        KEY_MAP.put(9, Key.EIGHT);
        KEY_MAP.put(10, Key.NINE);
        KEY_MAP.put(11, Key.ZERO);
        KEY_MAP.put(12, Key.MINUS);
        KEY_MAP.put(13, Key.EQUALS);
        KEY_MAP.put(14, Key.BACKSPACE);
        KEY_MAP.put(15, Key.TAB);
        KEY_MAP.put(16, Key.Q);
        KEY_MAP.put(17, Key.W);
        KEY_MAP.put(18, Key.E);
        KEY_MAP.put(19, Key.R);
        KEY_MAP.put(20, Key.T);
        KEY_MAP.put(21, Key.Y);
        KEY_MAP.put(22, Key.U);
        KEY_MAP.put(23, Key.I);
        KEY_MAP.put(24, Key.O);
        KEY_MAP.put(25, Key.P);
        KEY_MAP.put(26, Key.BRACKET_OPEN);
        KEY_MAP.put(27, Key.BRACKET_CLOSE);
        KEY_MAP.put(43, Key.SLASH_BACK);
        KEY_MAP.put(58, Key.CAPSLOCK);
        KEY_MAP.put(30, Key.A);
        KEY_MAP.put(31, Key.S);
        KEY_MAP.put(32, Key.D);
        KEY_MAP.put(33, Key.F);
        KEY_MAP.put(34, Key.G);
        KEY_MAP.put(35, Key.H);
        KEY_MAP.put(36, Key.J);
        KEY_MAP.put(37, Key.K);
        KEY_MAP.put(38, Key.L);
        KEY_MAP.put(39, Key.SEMICOLON);
        KEY_MAP.put(40, Key.APOSTROPHE);
        KEY_MAP.put(28, Key.ENTER);
        KEY_MAP.put(42, Key.SHIFT_LEFT);
        KEY_MAP.put(44, Key.Z);
        KEY_MAP.put(45, Key.X);
        KEY_MAP.put(46, Key.C);
        KEY_MAP.put(47, Key.V);
        KEY_MAP.put(48, Key.B);
        KEY_MAP.put(49, Key.N);
        KEY_MAP.put(50, Key.M);
        KEY_MAP.put(51, Key.COMMA);
        KEY_MAP.put(52, Key.PERIOD);
        KEY_MAP.put(53, Key.SLASH_FORWARD);
        KEY_MAP.put(54, Key.SHIFT_RIGHT);
        KEY_MAP.put(29, Key.CONTROL_LEFT);
        KEY_MAP.put(219, Key.META_LEFT);
        KEY_MAP.put(56, Key.ALT_LEFT);
        KEY_MAP.put(57, Key.SPACE);
        KEY_MAP.put(184, Key.ALT_RIGHT);
        KEY_MAP.put(220, Key.META_RIGHT);
        KEY_MAP.put(184, Key.MENU);
        KEY_MAP.put(157, Key.CONTROL_RIGHT);
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
