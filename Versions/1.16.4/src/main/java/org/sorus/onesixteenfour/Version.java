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

package org.sorus.onesixteenfour;

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.impl.InstrumentationTransformerUtility;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.IVersion;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.render.IRenderer;

import java.lang.instrument.Instrumentation;

/**
 * Implementation of the {@link IVersion} for 1.16.4.
 */
public class Version implements IVersion {

    public static void premain(String args, Instrumentation inst) {
        SorusStartup.start(Version.class, new InstrumentationTransformerUtility(inst), null, args, false);
    }

    private final IRenderer renderer = new Renderer(this);
    private final IGLHelper glHelper = new GLHelper();
    private final IScreen screen = new Screen();
    private final IGame game = new Game();
    private final IInput input = new Input(this);

    /**
     * Gets the {@link IRenderer} which handles drawing objects to the screen.
     * @return the renderer
     */
    @Override
    public IRenderer getRenderer() {
        return renderer;
    }

    /**
     * Gets the {@link IGLHelper} which helps with performing opengl functions.
     * @return the gl helper
     */
    @Override
    public IGLHelper getGLHelper() {
        return glHelper;
    }

    /**
     * Gets the {@link IScreen} which helps get screen width, and scaled width.
     * @return the screen
     */
    @Override
    public IScreen getScreen() {
        return screen;
    }

    /**
     * Gets the {@link IGame} which helps get details about the current game.
     * @return the game
     */
    @Override
    public IGame getGame() {
        return game;
    }


    /**
     * Gets the {@link IInput} which helps receive input from the user.
     * @return the input
     */
    @Override
    public IInput getInput() {
        return input;
    }

}
