package org.sorus.client.module.impl.scoreboard;

import java.awt.*;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.game.IScore;
import org.sorus.client.version.game.IScoreObjective;
import org.sorus.client.version.game.IScoreboard;

public class ScoreboardComponent extends Component {

  private final Setting<Boolean> showRedNumbers;
  private final Setting<Color> backgroundColor;

  private double width;
  private double height;

  public ScoreboardComponent() {
    super("Scoreboard");
    this.register(showRedNumbers = new Setting<>("showRedNumbers", true));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
  }

  @Override
  public void onAdd() {
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void render(double x, double y, boolean dummy) {
    Sorus.getSorus()
        .getGUIManager()
        .getRenderer()
        .drawRect(x, y, width, height, backgroundColor.getValue());
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer();
    IScoreboard scoreboard = Sorus.getSorus().getVersion().getData(IGame.class).getScoreboard();
    IScoreObjective scoreObjective = scoreboard.getObjectiveInSlot(1);
    String scoreObjectiveName;
    List<IScore> scores;
    if (!scoreObjective.exists() && dummy) {
      scoreObjective =
          Sorus.getSorus().getVersion().getData(IGame.class).getScoreboard().getDummyObjective();
    } else if (!scoreObjective.exists() && !dummy) {
      width = 0;
      height = 0;
      return;
    }
    scoreObjectiveName = scoreObjective.getName();
    scores = scoreObjective.getScores();
    width = fontRenderer.getStringWidth(scoreObjectiveName) + 4;
    for (IScore score : scores) {
      String s =
          score.getPlayerName() + ((showRedNumbers.getValue()) ? ": " + score.getPoints() : "");
      width = Math.max(width, fontRenderer.getStringWidth(s) + 4);
    }
    fontRenderer.drawString(
        scoreObjectiveName,
        x + width / 2 - fontRenderer.getStringWidth(scoreObjectiveName) / 2,
        y + 1,
        1,
        1,
        false,
        Color.WHITE);
    int i = 1;
    for (IScore score : scores) {
      fontRenderer.drawString(
          score.getPlayerName(),
          x + 2,
          y + i * (fontRenderer.getFontHeight() + 2),
          1,
          1,
          false,
          Color.WHITE);
      if (showRedNumbers.getValue()) {
        fontRenderer.drawString(
            String.valueOf(score.getPoints()),
            x + width - 2 - fontRenderer.getStringWidth(String.valueOf(score.getPoints())),
            y + i * (fontRenderer.getFontHeight() + 2),
            1,
            1,
            false,
            new Color(255, 85, 85));
      }
      i++;
    }
    height = i * (fontRenderer.getFontHeight() + 2);
  }

  @Override
  public String getDescription() {
    return "Customizable scoreboard.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(showRedNumbers, "Show Red Numbers"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }

  @EventInvoked
  public void onRenderSidebar(RenderObjectEvent.Sidebar e) {
    e.setCancelled(true);
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public double getHeight() {
    return height;
  }
}
