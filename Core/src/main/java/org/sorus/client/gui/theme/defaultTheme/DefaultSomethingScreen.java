package org.sorus.client.gui.theme.defaultTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.hud.positonscreen.SelectedHUDState;
import org.sorus.client.gui.hud.positonscreen.Snap;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.screen.modulelist.ModuleListScreen;
import org.sorus.client.gui.screen.profilelist.ProfileListScreen;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.gui.screen.theme.ThemeListScreen;
import org.sorus.client.util.Axis;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;

public class DefaultSomethingScreen<T extends Screen> extends DefaultThemeBase<T> {

  private static int prevIndex = 0;
  private static int staticIndex = 0;

  private final List<DefaultSomethingComponent> components = new ArrayList<>();

  private final boolean flyIn;

  private final List<HUD> huds;
  private final int index;

  private HUD selectedHUD;
  private SelectedHUDState selectedHUDState;

  private final List<Double> xSnaps = new ArrayList<>();
  private final List<Double> ySnaps = new ArrayList<>();

  private Panel main;
  private Collection bar;
  private Rectangle selector;

  private double width;

  private long initTime;

  public DefaultSomethingScreen(DefaultTheme theme, boolean flyIn, int index) {
    super(theme);
    this.flyIn = flyIn;
    prevIndex = staticIndex;
    staticIndex = index;
    this.index = index;
    this.huds = Sorus.getSorus().getHUDManager().getHUDs();
  }

  @Override
  public void init() {
    this.initTime = System.currentTimeMillis();
    main = new Panel();
    this.components.clear();
    this.components.add(
        new DefaultSomethingComponent(
            this,
            "sorus/hudposition.png",
            () -> Sorus.getSorus().getGUIManager().open(new HUDPositionScreen(false))));
    this.components.add(
        new DefaultSomethingComponent(
            this,
            "sorus/hudlist.png",
            () -> Sorus.getSorus().getGUIManager().open(new ModuleListScreen())));
    this.components.add(
        new DefaultSomethingComponent(
            this,
            "sorus/hudlist.png",
            () -> Sorus.getSorus().getGUIManager().open(new HUDListScreen())));
    this.components.add(
        new DefaultSomethingComponent(
            this,
            "sorus/settings.png",
            () ->
                Sorus.getSorus()
                    .getGUIManager()
                    .open(
                        new SettingsScreen(
                            this.getParent(), null, Sorus.getSorus().getSettingsManager()))));
    this.components.add(
        new DefaultSomethingComponent(
            this,
            "sorus/themelist.png",
            () -> Sorus.getSorus().getGUIManager().open(new ThemeListScreen())));
    this.components.add(
        new DefaultSomethingComponent(
            this,
            "sorus/profilelist.png",
            () -> Sorus.getSorus().getGUIManager().open(new ProfileListScreen())));
    width = 80 + components.size() * 70;
    this.bar = new Collection();
    bar.add(new Rectangle().size(width, 70).smooth(10).color(defaultTheme.getBackgroundColorNew()));
    bar.add(new Image().resource("sorus/logo.png").size(70, 70));
    int i = 0;
    for (DefaultSomethingComponent component : components) {
      bar.add(component.position(85 + i * 70, 12.5));
      i++;
    }
    bar.add(selector = new Rectangle().size(65, 70).color(new Color(255, 255, 255, 30)));
    main.add(bar);
  }

  @Override
  public void render() {
    bar.position(
        960 - width / 2,
        flyIn ? 1080 - 130 * Math.min(1, (System.currentTimeMillis() - initTime) / 100.0) : 950);
    selector.size(65 * ((staticIndex >= 0) ? 1 : 0), 70);
    double realLocation =
        prevIndex
            + (staticIndex - prevIndex)
                * Math.min(1, (System.currentTimeMillis() - initTime) / 100.0);
    selector.position(75 + Math.max(0, realLocation) * 70, 0);
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender();
    for (HUD hud : this.getHUDs()) {
      hud.render();
      this.renderHUDOverlay(hud);
    }
    this.updateHUDs();
  }

  @Override
  public void exit() {
    this.main.onRemove();
  }

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
        case RESIZE_LEFT_TOP:
        case RESIZE_LEFT_BOTTOM:
        case RESIZE_RIGHT_TOP:
        case RESIZE_RIGHT_BOTTOM:
          resizeBoxesColor = new Color(255, 255, 255, 150);
          break;
      }
    } else {
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
      double hudRight = hud.getRight();
      double hudBottom = hud.getBottom();
      if (this.distance(mouseX, mouseY, hudRight, hudTop) < 2.5
          || this.distance(mouseX, mouseY, hudLeft, hudTop) < 2.5
          || this.distance(mouseX, mouseY, hudLeft, hudBottom) < 2.5
          || this.distance(mouseX, mouseY, hudRight, hudBottom) < 2.5) {
        resizeBoxesColor = new Color(255, 255, 255, 120);
      } else if (mouseX > hudLeft && mouseX < hudRight && mouseY > hudTop && mouseY < hudBottom) {
        backgroundColor = new Color(255, 255, 255, 60);
        borderColor = new Color(255, 255, 255, 120);
      }
    }
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    double scaledWidth = hud.getScaledWidth();
    double scaledHeight = hud.getScaledHeight();
    renderer.drawRect(hudLeft, hudTop, scaledWidth, scaledHeight, backgroundColor);
    renderer.drawHollowRect(hudLeft, hudTop, scaledWidth, scaledHeight, 0.5, borderColor);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        renderer.drawRect(
            hudLeft + i * scaledWidth - 1.25,
            hudTop + j * scaledHeight - 1.25,
            2.5,
            2.5,
            resizeBoxesColor);
      }
    }
  }

  public void updateHUDs() {
    boolean mouseDown = Button.ONE.isDown();
    double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
    double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
    if (mouseDown && selectedHUD != null) {
      List<Double> xSnaps = this.getXSnaps();
      List<Double> ySnaps = this.getYSnaps();
      boolean centerScaling = Sorus.getSorus().getSettingsManager().isCenterScaling();
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
        case RESIZE_LEFT_TOP:
          {
            if (centerScaling) {
              this.applyCorrectionsAndResize(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (selectedHUDState.getInitialMouseX() - mouseX) / selectedHUD.getWidth() * 2
                      + selectedHUDState.getInitialScale());
            } else {
              this.applyCorrectionsAndResize2(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (selectedHUDState.getInitialMouseX() - mouseX) / selectedHUD.getWidth()
                      + selectedHUDState.getInitialScale(),
                  0.5,
                  -0.5);
            }
            break;
          }
        case RESIZE_LEFT_BOTTOM:
          {
            if (centerScaling) {
              this.applyCorrectionsAndResize(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (selectedHUDState.getInitialMouseX() - mouseX) / selectedHUD.getWidth() * 2
                      + selectedHUDState.getInitialScale());
            } else {
              this.applyCorrectionsAndResize2(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (selectedHUDState.getInitialMouseX() - mouseX) / selectedHUD.getWidth()
                      + selectedHUDState.getInitialScale(),
                  0.5,
                  0.5);
            }
            break;
          }
        case RESIZE_RIGHT_TOP:
          {
            if (centerScaling) {
              this.applyCorrectionsAndResize(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (mouseX - selectedHUDState.getInitialMouseX()) / selectedHUD.getWidth() * 2
                      + selectedHUDState.getInitialScale());
            } else {
              this.applyCorrectionsAndResize2(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (mouseX - selectedHUDState.getInitialMouseX()) / selectedHUD.getWidth()
                      + selectedHUDState.getInitialScale(),
                  -0.5,
                  -0.5);
            }
            break;
          }
        case RESIZE_RIGHT_BOTTOM:
          {
            if (centerScaling) {
              this.applyCorrectionsAndResize(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (mouseX - selectedHUDState.getInitialMouseX()) / selectedHUD.getWidth() * 2
                      + selectedHUDState.getInitialScale());
            } else {
              this.applyCorrectionsAndResize2(
                  selectedHUD,
                  xSnaps,
                  ySnaps,
                  (mouseX - selectedHUDState.getInitialMouseX()) / selectedHUD.getWidth()
                      + selectedHUDState.getInitialScale(),
                  -0.5,
                  0.5);
            }
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
          Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight(),
          new Color(255, 255, 255, 150));
    }
    for (double dub : ySnaps) {
      renderer.drawRect(
          0,
          dub - 0.25,
          Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth(),
          0.5,
          new Color(255, 255, 255, 150));
    }
    xSnaps.clear();
    ySnaps.clear();
  }

  public void applyCorrectionsAndMove(
      HUD hud, List<Double> xSnaps, List<Double> ySnaps, double wantedX, double wantedY) {
    double scaledWidth = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth();
    double scaledHeight = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight();
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

  public void applyCorrectionsAndResize(
      HUD hud, List<Double> xSnaps, List<Double> ySnaps, double wantedScale) {
    double scaledWidth = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth();
    double scaledHeight = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight();
    double x = selectedHUDState.getInitialX();
    double y = selectedHUDState.getInitialY();
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
    hud.setScale(constrainedScale);
    hud.setX(selectedHUDState.getInitialX());
    hud.setY(selectedHUDState.getInitialY());
  }

  public void applyCorrectionsAndResize2(
      HUD hud,
      List<Double> xSnaps,
      List<Double> ySnaps,
      double wantedScale,
      double offsetX,
      double offsetY) {
    double scaledWidth = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth();
    double scaledHeight = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight();
    double x = selectedHUDState.getInitialX();
    double y = selectedHUDState.getInitialY();
    double x2 =
        x - ((wantedScale - selectedHUDState.getInitialScale()) * selectedHUD.getWidth()) * offsetX;
    double y2 =
        y
            + ((wantedScale - selectedHUDState.getInitialScale()) * selectedHUD.getHeight())
                * offsetY;
    Snap[] snaps = {
      this.getSnap(xSnaps, hud.getX() - wantedScale * hud.getWidth() * offsetX, 0, Axis.X),
      this.getSnap(ySnaps, y2 + wantedScale * hud.getHeight() * offsetY, 0, Axis.Y)
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
    double newScale = wantedScale;
    if (minSnap.getAxis() == Axis.X) {
      newScale = -(minSnap.getValue() - hud.getX()) / (offsetX * hud.getWidth());
      this.ySnaps.clear();
    } else {
      // newScale = (minSnap.getValue() - hud.getY()) / offsetY;
      // newScale = Math.abs(minSnap.getValue() - y) / hud.getHeight() * 2;
      this.xSnaps.clear();
    }
    double constrainedScale = newScale; // MathUtil.clamp(newScale, 0.5, 2);
    /*if (x - constrainedScale * hud.getWidth() / 2 < 0) {
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
    }*/
    if (constrainedScale != newScale) {
      actualXSnaps.clear();
      actualYSnaps.clear();
    }
    this.xSnaps.addAll(actualXSnaps);
    this.ySnaps.addAll(actualYSnaps);
    hud.setScale(constrainedScale);
    // hud.setX(x - ((constrainedScale - selectedHUDState.getInitialScale()) *
    // selectedHUD.getWidth()) * offsetX);
    // hud.setY(y + ((constrainedScale - selectedHUDState.getInitialScale()) *
    // selectedHUD.getHeight()) * offsetY);
  }

  public Snap getSnap(List<Double> snaps, double value, double offset, Axis axis) {
    for (double dub : snaps) {
      if (Math.abs(dub - value) < Sorus.getSorus().getSettingsManager().getSnappingSensitivity()) {
        return new Snap(dub, Math.abs(dub - value), offset, true, axis);
      }
    }
    return new Snap(value, Double.MAX_VALUE, offset, false, axis);
  }

  private List<Double> getXSnaps() {
    List<Double> xSnaps = new ArrayList<>();
    for (HUD hud : this.getHUDs()) {
      if (!hud.equals(selectedHUD)) {
        xSnaps.add(hud.getLeft());
        xSnaps.add(MathUtil.average(hud.getLeft(), hud.getRight()));
        xSnaps.add(hud.getRight());
      }
    }
    double scaledWidth = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth();
    xSnaps.add(scaledWidth / 2);
    return xSnaps;
  }

  private List<Double> getYSnaps() {
    List<Double> ySnaps = new ArrayList<>();
    for (HUD hud : this.getHUDs()) {
      if (!hud.equals(selectedHUD)) {
        ySnaps.add(hud.getTop());
        ySnaps.add(MathUtil.average(hud.getTop(), hud.getBottom()));
        ySnaps.add(hud.getBottom());
      }
    }
    double scaledHeight = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight();
    ySnaps.add(scaledHeight / 2);
    return ySnaps;
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      this.close();
    }
  }

  protected void close() {
    Sorus.getSorus().getGUIManager().close(this.getParent());
  }

  @Override
  public void onMouseClick(Button button, double x, double y) {
    if (!this.shouldInitiateDrag(x, y)) {
      return;
    }
    for (HUD hud : this.getHUDs()) {
      double left = hud.getLeft();
      double right = hud.getRight();
      double top = hud.getTop();
      double bottom = hud.getBottom();
      SelectedHUDState.InteractType interactType = null;
      if (this.distance(x, y, right, top) < 2.5) {
        interactType = SelectedHUDState.InteractType.RESIZE_RIGHT_TOP;
      } else if (this.distance(x, y, left, top) < 2.5) {
        interactType = SelectedHUDState.InteractType.RESIZE_LEFT_TOP;
      } else if (this.distance(x, y, left, bottom) < 2.5) {
        interactType = SelectedHUDState.InteractType.RESIZE_LEFT_BOTTOM;
      } else if (this.distance(x, y, right, bottom) < 2.5) {
        interactType = SelectedHUDState.InteractType.RESIZE_RIGHT_BOTTOM;
      } else if (x > left && x < right && y > top && y < bottom) {
        interactType = SelectedHUDState.InteractType.DRAG;
      }
      if (interactType != null) {
        this.setSelectedHUD(hud, interactType, x, y);
      }
    }
  }

  protected boolean shouldInitiateDrag(double mouseX, double mouseY) {
    return true;
  }

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
  public void onMouseRelease(Button button) {
    if (selectedHUD != null) {
      this.selectedHUD = null;
    }
  }

  public double distance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  }

  public int getIndex() {
    return index;
  }

  public List<HUD> getHUDs() {
    return huds;
  }
}
