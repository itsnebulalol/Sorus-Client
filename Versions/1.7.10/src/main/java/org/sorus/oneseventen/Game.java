

package org.sorus.oneseventen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.version.game.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game implements IGame {

    private final ItemManager itemManager = new ItemManager();

    @Override
    public boolean isIngame() {
        return Minecraft.getMinecraft().currentScreen == null;
    }

    @Override
    public boolean shouldRenderHUDS() {
        return this.isIngame() || Minecraft.getMinecraft().currentScreen instanceof GuiChat || Minecraft.getMinecraft().currentScreen instanceof GuiBlank;
    }

    @Override
    public void display(GUIType type) {
        GuiScreen screen = null;
        switch(type) {
            case VIEW_WORLDS:
                screen = new GuiSelectWorld(new GuiMainMenu());
                break;
            case VIEW_SERVERS:
                screen = new GuiMultiplayer(new GuiMainMenu());
                break;
            case LANGUAGES:
                screen = new GuiLanguage(new GuiMainMenu(), Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().getLanguageManager());
                break;
            case SETTINGS:
                screen = new GuiOptions(new GuiMainMenu(), Minecraft.getMinecraft().gameSettings);
                break;
            case BLANK:
                screen = new GuiBlank();
                break;
        }
        Minecraft.getMinecraft().displayGuiScreen(screen);
    }

    @Override
    public void removeBlankGUI() {
        if(Minecraft.getMinecraft().currentScreen instanceof GuiBlank) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    public int getFPS() {
        try {
            String fieldName = ObfuscationManager.getFieldName("net/minecraft/client/Minecraft", "debugFPS");
            Field field = Minecraft.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(null);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public IPlayer getPlayer() {
        return new PlayerImpl(Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public PerspectiveMode getPerspective() {
        switch(Minecraft.getMinecraft().gameSettings.thirdPersonView) {
            case 0:
                return PerspectiveMode.FIRST_PERSON;
            case 1:
                return PerspectiveMode.THIRD_PERSON_BACK;
            case 2:
                return PerspectiveMode.THIRD_PERSON_FRONT;
            default:
                return null;
        }
    }

    @Override
    public void setPerspective(PerspectiveMode perspective) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        switch(perspective) {
            case FIRST_PERSON:
                gameSettings.thirdPersonView = 0;
                break;
            case THIRD_PERSON_BACK:
                gameSettings.thirdPersonView = 1;
                break;
            case THIRD_PERSON_FRONT:
                gameSettings.thirdPersonView = 2;
                break;
        }
    }

    @Override
    public void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
    }

    @Override
    public int getPing() {
        try {
            Field field = NetHandlerPlayClient.class.getDeclaredField("playerInfoMap");
            field.setAccessible(true);
            return ((GuiPlayerInfo) ((Map<?, ?>) field.get(Minecraft.getMinecraft().getNetHandler())).get(Minecraft.getMinecraft().getSession().getUsername())).responseTime;
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public IItemManager getItemManager() {
        return this.itemManager;
    }

    @Override
    public void shutdown() {
        Minecraft.getMinecraft().shutdown();
    }

    @Override
    public IScoreboard getScoreboard() {
        return new ScoreboardImpl(Minecraft.getMinecraft().theWorld.getScoreboard());
    }

    @Override
    public List<IPotionEffect> getDummyEffects() {
        List<IPotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffectImpl(new PotionEffect(1, 35, 1)));
        effects.add(new PotionEffectImpl(new PotionEffect(5, 1089, 1)));
        effects.add(new PotionEffectImpl(new PotionEffect(11, 2046, 1)));
        return effects;
    }

    @Override
    public List<IItemStack> getDummyArmor() {
        List<IItemStack> armor = new ArrayList<>();
        armor.add(new ItemStackImpl(new ItemStack(Item.getItemById(313))));
        armor.add(new ItemStackImpl(new ItemStack(Item.getItemById(311))));
        return armor;
    }

    @Override
    public String getCurrentServerIP() {
        ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
        if(currentServerData == null) {
            return null;
        }
        return currentServerData.serverIP;

    }
}
