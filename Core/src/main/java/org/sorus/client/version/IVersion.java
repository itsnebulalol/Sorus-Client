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

package org.sorus.client.version;

import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.render.IRenderer;

/**
 * Base version interface, each version will implement this so Core can render and get data from the
 * game.
 */
public interface IVersion {

  /**
   * Gets the {@link IRenderer} which handles drawing objects to the screen.
   *
   * @return the renderer
   */
  IRenderer getRenderer();

  /**
   * Gets the {@link IGLHelper} which helps with performing opengl functions.
   *
   * @return the gl helper
   */
  IGLHelper getGLHelper();

  /**
   * Gets the {@link IScreen} which helps get screen width, and scaled width.
   *
   * @return the screen
   */
  IScreen getScreen();

  /**
   * Gets the {@link IGame} which helps get details about the current game.
   *
   * @return the game
   */
  IGame getGame();

  /**
   * Gets the {@link IInput} which helps receive input from the user.
   *
   * @return the input
   */
  IInput getInput();
}
