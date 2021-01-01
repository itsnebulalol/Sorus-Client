

package org.sorus.onesixteenfour;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.settings.PointOfView;
import org.sorus.client.version.game.*;

import java.util.List;

public class Game implements IGame {

    private final ItemManager itemManager = new ItemManager();

    @Override
    public boolean isIngame() {
        return Minecraft.getInstance().currentScreen == null;
    }

    @Override
    public boolean shouldRenderHUDS() {
        return this.isIngame() || Minecraft.getInstance().currentScreen instanceof ChatScreen || Minecraft.getInstance().currentScreen instanceof BlankScreen;
    }

    @Override
    public void display(GUIType type) {
        Screen screen = null;
        switch(type) {
            case VIEW_WORLDS:
                screen = new WorldSelectionScreen(new MainMenuScreen());
                break;
            case VIEW_SERVERS:
                screen = new MultiplayerScreen(new MainMenuScreen());
                break;
            case LANGUAGES:
                screen = new LanguageScreen(new MainMenuScreen(), Minecraft.getInstance().gameSettings, Minecraft.getInstance().getLanguageManager());
                break;
            case SETTINGS:
                screen = new OptionsScreen(new MainMenuScreen(), Minecraft.getInstance().gameSettings);
                break;
            case BLANK:
                screen = new BlankScreen();
                break;
        }
        Minecraft.getInstance().displayGuiScreen(screen);
    }

    @Override
    public void removeBlankGUI() {
        if(Minecraft.getInstance().currentScreen instanceof BlankScreen) {
            Minecraft.getInstance().displayGuiScreen(null);
        }
    }

    @Override
    public int getFPS() {
        return Integer.parseInt(Minecraft.getInstance().debug.split(" ")[0]);
    }

    @Override
    public IPlayer getPlayer() {
        return new PlayerImpl(Minecraft.getInstance().player);
    }

    @Override
    public PerspectiveMode getPerspective() {
        switch(Minecraft.getInstance().gameSettings.func_243230_g()) {
            case FIRST_PERSON:
                return PerspectiveMode.FIRST_PERSON;
            case THIRD_PERSON_BACK:
                return PerspectiveMode.THIRD_PERSON_BACK;
            case THIRD_PERSON_FRONT:
                return PerspectiveMode.THIRD_PERSON_FRONT;
            default:
                return null;
        }
    }

    @Override
    public void setPerspective(PerspectiveMode perspective) {
        GameSettings gameSettings = Minecraft.getInstance().gameSettings;
        switch(perspective) {
            case FIRST_PERSON:
                gameSettings.func_243229_a(PointOfView.FIRST_PERSON);
                break;
            case THIRD_PERSON_BACK:
                gameSettings.func_243229_a(PointOfView.THIRD_PERSON_BACK);
                break;
            case THIRD_PERSON_FRONT:
                gameSettings.func_243229_a(PointOfView.THIRD_PERSON_FRONT);
                break;
        }
    }

    @Override
    public void sendChatMessage(String message) {
        Minecraft.getInstance().player.sendChatMessage(message);
    }

    @Override
    public int getPing() {
        return 0;
    }

    @Override
    public IItemManager getItemManager() {
        return this.itemManager;
    }

    @Override
    public void shutdown() {
        Minecraft.getInstance().shutdown();
    }

    @Override
    public IScoreboard getScoreboard() {
        return new ScoreboardImpl(Minecraft.getInstance().world.getScoreboard());
    }

    @Override
    public List<IPotionEffect> getDummyEffects() {
        return null;
    }

    @Override
    public List<IItemStack> getDummyArmor() {
        return null;
    }

    @Override
    public String getCurrentServerIP() {
        ServerData currentServerData = Minecraft.getInstance().getCurrentServerData();
        if(currentServerData == null) {
            return null;
        }
        return currentServerData.serverIP;
    }
}
