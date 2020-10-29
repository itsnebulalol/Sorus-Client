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

package org.sorus.client.gui.theme.defaultTheme.positionscreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.hud.positonscreen.SelectedHUDState;
import org.sorus.client.gui.hud.positonscreen.Snap;
import org.sorus.client.gui.screen.MenuScreen;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.util.Axis;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.Key;

public class DefaultHUDPositionScreen extends ThemeBase<HUDPositionScreen> {

  private final Panel main = new Panel();
  private final List<HUD> huds;

  private HUD selectedHUD;
  private SelectedHUDState selectedHUDState;

  private final List<Double> xSnaps = new ArrayList<>();
  private final List<Double> ySnaps = new ArrayList<>();

  public DefaultHUDPositionScreen(HUDManager hudManager) {
    Sorus.getSorus().getVersion().getRenderer().enableBlur();
    this.huds = hudManager.getHUDs();
    main.add(new PositionScreenButton(this::onButtonClick, "S", 50, 50).position(935, 515));
  }

  @Override
  public void render() {
    for (HUD hud : this.huds) {
      hud.render();
      this.renderHUDOverlay(hud);
    }
    this.updateMods();
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    this.main.onRender();
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getRenderer().disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    this.main.onRemove();
  }

  /** Renders the overlay for a hud taking in account selectedness, and hoveredness. */
  private void renderHUDOverlay(HUD hud) {
    Color backgroundColor = new Color(255, 255, 255, 45);
    Color borderColor = new Color(255, 255, 255, 90);
    Color resizeBoxesColor = new Color(255, 255, 255, 90);
    double hudLeft = hud.getLeft();
    double hudTop = hud.getTop();
    if (hud.equals(selectedHUD)) {
      switch (selectedHUDState.getInteractType()) {
        case DRAG:
          backgroundColor = new Color(255, 255, 255, 75);
          borderColor = new Color(255, 255, 255, 150);
          break;
        case RESIZE_LEFT:
        case RESIZE_RIGHT:
          resizeBoxesColor = new Color(255, 255, 255, 150);
          break;
      }
    } else {
      double mouseX = Sorus.getSorus().getVersion().getInput().getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getInput().getMouseY();
      double hudRight = hud.getRight();
      double hudBottom = hud.getBottom();
      if (this.distance(mouseX, mouseY, hudRight, hudTop) < 3
              || this.distance(mouseX, mouseY, hudLeft, hudTop) < 3
              || this.distance(mouseX, mouseY, hudLeft, hudBottom) < 3
              || this.distance(mouseX, mouseY, hudRight, hudBottom) < 3) {
        resizeBoxesColor = new Color(255, 255, 255, 120);
      } else if (mouseX > hudLeft && mouseX < hudRight && mouseY > hudTop && mouseY < hudBottom) {
        backgroundColor = new Color(255, 255, 255, 60);
        borderColor = new Color(255, 255, 255, 120);
      }
    }
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    renderer.drawRect(
            hudLeft, hudTop, hud.getScaledWidth(), hud.getScaledHeight(), backgroundColor);
    renderer.drawHollowRect(
            hudLeft, hudTop, hud.getScaledWidth(), hud.getScaledHeight(), 0.5, borderColor);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        renderer.drawRect(
                hudLeft + i * hud.getScaledWidth() - 1.5,
                hudTop + j * hud.getScaledHeight() - 1.5,
                3,
                3,
                resizeBoxesColor);
      }
    }
  }

  /** Handles dragging / resizing. Updates the mod's position based on user interaction. */
  public void updateMods() {
    boolean mouseDown = Button.ONE.isDown();
    double mouseX = Sorus.getSorus().getVersion().getInput().getMouseX();
    double mouseY = Sorus.getSorus().getVersion().getInput().getMouseY();
    if (mouseDown && selectedHUD != null) {
      List<Double> xSnaps = this.getXSnaps();
      List<Double> ySnaps = this.getYSnaps();
      switch (selectedHUDState.getInteractType()) {
        case DRAG:
          {
            double targetX =
                mouseX - selectedHUDState.getInitialMouseX() + selectedHUDState.getInitialX();
            double targetY =
                mouseY - selectedHUDState.getInitialMouseY() + selectedHUDState.getInitialY();
            this.applyCorrectionsAndMove(selectedHUD, xSnaps, ySnaps, targetX, targetY);
            break;
          }
        case RESIZE_LEFT:
          {
            this.applyCorrectionsAndResize(
                selectedHUD,
                xSnaps,
                ySnaps,
                (selectedHUDState.getInitialMouseX() - mouseX) / selectedHUD.getWidth() * 2
                    + selectedHUDState.getInitialScale());
            break;
          }
        case RESIZE_RIGHT:
          {
            this.applyCorrectionsAndResize(
                selectedHUD,
                xSnaps,
                ySnaps,
                (mouseX - selectedHUDState.getInitialMouseX()) / selectedHUD.getWidth() * 2
                    + selectedHUDState.getInitialScale());
            break;
          }
      }
    }
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    for (double dub : xSnaps) {
      renderer.drawRect(
          dub - 0.25,
          0,
          0.5,
          Sorus.getSorus().getVersion().getScreen().getScaledHeight(),
          new Color(255, 255, 255, 150));
    }
    for (double dub : ySnaps) {
      renderer.drawRect(
          0,
          dub - 0.25,
          Sorus.getSorus().getVersion().getScreen().getScaledWidth(),
          0.5,
          new Color(255, 255, 255, 150));
    }
    xSnaps.clear();
    ySnaps.clear();
  }
  /**
   * Checks if the hud should snap to any of the possible snapping points and then sets the new
   * position correctly.
   *
   * @param hud the current selected hud
   * @param xSnaps the available x positions to snap to
   * @param ySnaps the available y position to snap to
   * @param wantedX the raw x, without taking in account snapping
   * @param wantedY the raw y, without taking in account snapping
   */
  public void applyCorrectionsAndMove(
      HUD hud, List<Double> xSnaps, List<Double> ySnaps, double wantedX, double wantedY) {
    double scaledWidth = Sorus.getSorus().getVersion().getScreen().getScaledWidth();
    double scaledHeight = Sorus.getSorus().getVersion().getScreen().getScaledHeight();
    Snap[] snaps = {
      this.getSnap(
          xSnaps, wantedX - hud.getScaledWidth() / 2, -selectedHUD.getScaledWidth() / 2, Axis.X),
      this.getSnap(
          ySnaps, wantedY - hud.getScaledHeight() / 2, -selectedHUD.getScaledHeight() / 2, Axis.Y),
      this.getSnap(xSnaps, wantedX, 0, Axis.X),
      this.getSnap(ySnaps, wantedY, 0, Axis.Y),
      this.getSnap(
          xSnaps, wantedX + hud.getScaledWidth() / 2, selectedHUD.getScaledWidth() / 2, Axis.X),
      this.getSnap(
          ySnaps, wantedY + hud.getScaledHeight() / 2, selectedHUD.getScaledHeight() / 2, Axis.Y)
    };
    List<Double> actualXSnaps = new ArrayList<>();
    List<Double> actualYSnaps = new ArrayList<>();
    Snap minXSnap = new Snap(0, Double.MAX_VALUE, 0, false, Axis.X);
    Snap minYSnap = new Snap(0, Double.MAX_VALUE, 0, false, Axis.Y);
    for (Snap snap : snaps) {
      if (snap.getAxis() == Axis.X) {
        if (snap.getDistance() <= minXSnap.getDistance()) {
          if (snap.getDistance() < minXSnap.getDistance()) {
            actualXSnaps.clear();
          }
          if (snap.isSnapped()) {
            actualXSnaps.add(snap.getValue());
          }
          minXSnap = snap;
        }
      } else {
        if (snap.getDistance() <= minYSnap.getDistance()) {
          if (snap.getDistance() < minYSnap.getDistance()) {
            actualYSnaps.clear();
          }
          if (snap.isSnapped()) {
            actualYSnaps.add(snap.getValue());
          }
          minYSnap = snap;
        }
      }
    }
    double snappedX = minXSnap.getValue() - minXSnap.getOffset();
    double snappedY = minYSnap.getValue() - minYSnap.getOffset();
    double constrainedX =
        MathUtil.clamp(snappedX, hud.getScaledWidth() / 2, scaledWidth - hud.getScaledWidth() / 2);
    double constrainedY =
        MathUtil.clamp(
            snappedY, hud.getScaledHeight() / 2, scaledHeight - hud.getScaledHeight() / 2);
    if (snappedX != constrainedX) {
      actualXSnaps.clear();
    }
    if (snappedY != constrainedY) {
      actualYSnaps.clear();
    }
    this.xSnaps.addAll(actualXSnaps);
    this.ySnaps.addAll(actualYSnaps);
    hud.setX(constrainedX);
    hud.setY(constrainedY);
  }

  /**
   * Checks if the hud should snap to any of the possible snapping points and then sets the new
   * scale correctly.
   *
   * @param hud the current selected hud
   * @param xSnaps the available x positions to snap to
   * @param ySnaps the available y position to snap to
   * @param wantedScale the raw scale, without taking in account snapping
   */
  public void applyCorrectionsAndResize(
      HUD hud, List<Double> xSnaps, List<Double> ySnaps, double wantedScale) {
    double scaledWidth = Sorus.getSorus().getVersion().getScreen().getScaledWidth();
    double scaledHeight = Sorus.getSorus().getVersion().getScreen().getScaledHeight();
    double x = hud.getX();
    double y = hud.getY();
    Snap[] snaps = {
      this.getSnap(
          xSnaps, x - hud.getWidth() * wantedScale / 2, -hud.getWidth() * wantedScale / 2, Axis.X),
      this.getSnap(
          ySnaps,
          y - hud.getHeight() * wantedScale / 2,
          -hud.getHeight() * wantedScale / 2,
          Axis.Y),
      this.getSnap(
          xSnaps, x + hud.getWidth() * wantedScale / 2, hud.getWidth() * wantedScale / 2, Axis.X),
      this.getSnap(
          ySnaps, y + hud.getHeight() * wantedScale / 2, hud.getHeight() * wantedScale / 2, Axis.Y)
    };
    List<Double> actualXSnaps = new ArrayList<>();
    List<Double> actualYSnaps = new ArrayList<>();
    Snap minSnap = new Snap(0, Double.MAX_VALUE, 0, false, Axis.X);
    for (Snap snap : snaps) {
      if (snap.getDistance() <= minSnap.getDistance()) {
        if (snap.getDistance() < minSnap.getDistance()) {
          actualXSnaps.clear();
          actualYSnaps.clear();
        }
        if (snap.isSnapped()) {
          if (snap.getAxis() == Axis.X) {
            actualXSnaps.add(snap.getValue());
          } else {
            actualYSnaps.add(snap.getValue());
          }
        }
        minSnap = snap;
      }
    }
    double newScale;
    if (minSnap.getAxis() == Axis.X) {
      newScale = Math.abs(minSnap.getValue() - x) / hud.getWidth() * 2;
      this.ySnaps.clear();
    } else {
      newScale = Math.abs(minSnap.getValue() - y) / hud.getHeight() * 2;
      this.xSnaps.clear();
    }
    double constrainedScale = MathUtil.clamp(newScale, 0.5, 2);
    if (x - constrainedScale * hud.getWidth() / 2 < 0) {
      constrainedScale = -(-x * 2 / hud.getWidth());
    }
    if (y - constrainedScale * hud.getHeight() / 2 < 0) {
      constrainedScale = -(-y * 2 / hud.getHeight());
    }
    if (x + constrainedScale * hud.getWidth() / 2 > scaledWidth) {
      constrainedScale = (scaledWidth - x) * 2 / hud.getWidth();
    }
    if (y + constrainedScale * hud.getHeight() / 2 > scaledHeight) {
      constrainedScale = (scaledHeight - y) * 2 / hud.getHeight();
    }
    if (constrainedScale != newScale) {
      actualXSnaps.clear();
      actualYSnaps.clear();
    }
    this.xSnaps.addAll(actualXSnaps);
    this.ySnaps.addAll(actualYSnaps);
    hud.setX(selectedHUDState.getInitialX());
    hud.setY(selectedHUDState.getInitialY());
    hud.setScale(constrainedScale);
  }

  /**
   * Gets a snap object, which is used to make the hud "snap" to guidelines.
   *
   * @param snaps the possible locations to snap to
   * @param value the raw, unsnapped value
   * @param offset the offset of the snap, ranging from -hudWidth / 2 (left side) to hudWidth / 2
   *     (right side), used to snap the correct edge
   * @param axis the axis for the snap ({@link Axis#X} or {@link Axis#Y})
   * @return the snapped value
   */
  public Snap getSnap(List<Double> snaps, double value, double offset, Axis axis) {
    for (double dub : snaps) {
      if (Math.abs(dub - value) < 3.5) {
        return new Snap(dub, Math.abs(dub - value), offset, true, axis);
      }
    }
    return new Snap(value, Double.MAX_VALUE, offset, false, axis);
  }

  /** @return all the possible x positions to snap to in the hud */
  private List<Double> getXSnaps() {
    List<Double> xSnaps = new ArrayList<>();
    for (HUD hud : this.huds) {
      if (!hud.equals(selectedHUD)) {
        xSnaps.add(hud.getLeft());
        xSnaps.add(MathUtil.average(hud.getLeft(), hud.getRight()));
        xSnaps.add(hud.getRight());
      }
    }
    double scaledWidth = Sorus.getSorus().getVersion().getScreen().getScaledWidth();
    xSnaps.add(scaledWidth / 2);
    return xSnaps;
  }

  /** @return all the possible y positions to snap to in the hud */
  private List<Double> getYSnaps() {
    List<Double> ySnaps = new ArrayList<>();
    for (HUD hud : this.huds) {
      if (!hud.equals(selectedHUD)) {
        ySnaps.add(hud.getTop());
        ySnaps.add(MathUtil.average(hud.getTop(), hud.getBottom()));
        ySnaps.add(hud.getBottom());
      }
    }
    double scaledHeight = Sorus.getSorus().getVersion().getScreen().getScaledHeight();
    ySnaps.add(scaledHeight / 2);
    return ySnaps;
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  @Override
  public void mouseClicked(Button button, double x, double y) {
    for (HUD hud : this.huds) {
      double left = hud.getLeft();
      double right = hud.getRight();
      double top = hud.getTop();
      double bottom = hud.getBottom();
      SelectedHUDState.InteractType interactType = null;
      if (this.distance(x, y, right, top) < 3) {
        interactType = SelectedHUDState.InteractType.RESIZE_RIGHT;
      } else if (this.distance(x, y, left, top) < 3) {
        interactType = SelectedHUDState.InteractType.RESIZE_LEFT;
      } else if (this.distance(x, y, left, bottom) < 3) {
        interactType = SelectedHUDState.InteractType.RESIZE_LEFT;
      } else if (this.distance(x, y, right, bottom) < 3) {
        interactType = SelectedHUDState.InteractType.RESIZE_RIGHT;
      } else if (x > left && x < right && y > top && y < bottom) {
        interactType = SelectedHUDState.InteractType.DRAG;
      }
      if (interactType != null) {
        this.setSelectedHUD(hud, interactType, x, y);
      }
    }
  }

  /**
   * Sets the selected hud and updates the selected hud state.
   *
   * @param selectedHUD the hud to be selected
   * @param interactType the type of interaction (ex: drag, resize)
   * @param mouseX the mouse x where the interaction first occurred
   * @param mouseY the mouse y where the interaction first occurred
   */
  private void setSelectedHUD(
      HUD selectedHUD, SelectedHUDState.InteractType interactType, double mouseX, double mouseY) {
    this.selectedHUD = selectedHUD;
    this.selectedHUDState =
        new SelectedHUDState(
            interactType,
            selectedHUD.getX(),
            selectedHUD.getY(),
            selectedHUD.getScale(),
            mouseX,
            mouseY);
  }

  @Override
  public void mouseReleased(Button button) {
    if (selectedHUD != null) {
      this.selectedHUD = null;
    }
  }

  public double distance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  }

  private void onButtonClick() {
    Sorus.getSorus().getGUIManager().close(DefaultHUDPositionScreen.this.screen);
    Sorus.getSorus().getGUIManager().open(new MenuScreen());
  }
}
