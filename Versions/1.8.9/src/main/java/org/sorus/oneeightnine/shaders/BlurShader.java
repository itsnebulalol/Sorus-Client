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

package org.sorus.oneeightnine.shaders;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.sorus.client.obfuscation.ObfuscationManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

public class BlurShader extends ShaderGroup {

    private static Method initTarget;
    private static Method parsePass;

    static {
        try {
            String initTargetName = ObfuscationManager.getMethodName("net/minecraft/client/shader/ShaderGroup", "initTarget", "(Lcom/google/gson/JsonElement;)V");
            initTarget = ShaderGroup.class.getDeclaredMethod(initTargetName, JsonElement.class);
            initTarget.setAccessible(true);
            String parsePassName = ObfuscationManager.getMethodName("net/minecraft/client/shader/ShaderGroup", "parsePass", "(Lnet/minecraft/client/renderer/texture/TextureManager;Lcom/google/gson/JsonElement;)V");
            parsePass = ShaderGroup.class.getDeclaredMethod(parsePassName, TextureManager.class, JsonElement.class);
            parsePass.setAccessible(true);
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public BlurShader(double amount) throws IOException {
        super(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getResourceManager(), Minecraft.getMinecraft().getFramebuffer(), new ResourceLocation("blur.json"));
        this.load(amount);
    }

    @Override
    public void parseGroup(TextureManager textureManager, ResourceLocation resourceLocation) throws JsonSyntaxException {

    }

    private void load(double amount) {
        JsonParser var3 = new JsonParser();
        try {
            String string = "{ \"targets\": [ \"swap\" ], \"passes\": [ { \"name\": \"blur\", \"intarget\": \"minecraft:main\", \"outtarget\": \"swap\", \"uniforms\": [ { \"name\": \"BlurDir\", \"values\": [ 1.0, 0.0 ] }, { \"name\": \"Radius\", \"values\": [ " + amount + " ] } ] }, { \"name\": \"blur\", \"intarget\": \"swap\", \"outtarget\": \"minecraft:main\", \"uniforms\": [ { \"name\": \"BlurDir\", \"values\": [ 0.0, 1.0 ] }, { \"name\": \"Radius\", \"values\": [ " + amount + " ] } ] } ] }";
            JsonObject var22 = var3.parse(string).getAsJsonObject();
            Iterator<JsonElement> var9;
            int counter;
            JsonArray jsonArray;
            JsonElement var10;
            JsonException var12;
            if (JsonUtils.isJsonArray(var22, "targets")) {
                jsonArray = var22.getAsJsonArray("targets");
                counter = 0;
                for(var9 = jsonArray.iterator(); var9.hasNext(); ++counter) {
                    var10 = var9.next();
                    try {
                        initTarget.invoke(this, var10);
                    } catch (Exception var19) {
                        var12 = JsonException.func_151379_a(var19);
                        var12.func_151380_a("targets[" + counter + "]");
                        throw var12;
                    }
                }
            }

            if (JsonUtils.isJsonArray(var22, "passes")) {
                jsonArray = var22.getAsJsonArray("passes");
                counter = 0;

                for(var9 = jsonArray.iterator(); var9.hasNext(); ++counter) {
                    var10 = var9.next();

                    try {
                        parsePass.invoke(this, Minecraft.getMinecraft().getTextureManager(), var10);
                    } catch (Exception var18) {
                        var12 = JsonException.func_151379_a(var18);
                        var12.func_151380_a("passes[" + counter + "]");
                        throw var12;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
