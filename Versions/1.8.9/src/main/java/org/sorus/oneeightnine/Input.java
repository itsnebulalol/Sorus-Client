

package org.sorus.oneeightnine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.sourceforge.jaad.aac.tools.IS;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.*;
import org.sorus.client.version.IVersion;
import org.sorus.client.version.input.KeybindType;
import org.sorus.oneeightnine.util.input.InputMap;

public class Input implements IInput {

    private final IVersion version;

    public Input(IVersion version) {
        this.version = version;
    }

    @Override
    public double getMouseX() {
        return Mouse.getX() / version.getData(IScreen.class).getDisplayWidth() * version.getData(IScreen.class).getScaledWidth();
    }

    @Override
    public double getMouseY() {
        return version.getData(IScreen.class).getScaledHeight() - (Mouse.getY() / version.getData(IScreen.class).getDisplayHeight() * version.getData(IScreen.class).getScaledHeight());
    }

    @Override
    public boolean isButtonDown(Button button) {
        return Mouse.isButtonDown(InputMap.getButtonCode(button));
    }

    @Override
    public boolean isKeyDown(Key key) {
        return Keyboard.isKeyDown(InputMap.getKeyCode(key));
    }

    @Override
    public double getScroll() {
        return Mouse.getDWheel();
    }

    @Override
    public IKeybind getKeybind(KeybindType keybind) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        switch(keybind) {
            case ATTACK:
                return new Keybind(gameSettings.keyBindAttack);
            case INTERACT:
                return new Keybind(gameSettings.keyBindUseItem);
            case FORWARD:
                return new Keybind(gameSettings.keyBindForward);
            case LEFT:
                return new Keybind(gameSettings.keyBindLeft);
            case RIGHT:
                return new Keybind(gameSettings.keyBindRight);
            case BACK:
                return new Keybind(gameSettings.keyBindBack);
            case SPRINT:
                return new Keybind(gameSettings.keyBindSprint);
            case JUMP:
                return new Keybind(gameSettings.keyBindJump);
            default:
                return null;
        }
    }

}
