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

package org.sorus.oneseventen.shaders;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Iterator;

public class BlurShader extends ShaderGroup {

    public BlurShader(double amount) {
        super(Minecraft.theMinecraft.getTextureManager(), Minecraft.getMinecraft().getResourceManager(), Minecraft.getMinecraft().getFramebuffer(), new ResourceLocation("blur.json"));
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
            JsonArray var7;
            int var8;
            Iterator var9;
            JsonElement var10;
            JsonException var12;
            if (JsonUtils.jsonObjectFieldTypeIsArray(var22, "targets")) {
                var7 = var22.getAsJsonArray("targets");
                var8 = 0;

                for(var9 = var7.iterator(); var9.hasNext(); ++var8) {
                    var10 = (JsonElement)var9.next();

                    try {
                        super.initTarget(var10);
                    } catch (Exception var19) {
                        var12 = JsonException.func_151379_a(var19);
                        var12.func_151380_a("targets[" + var8 + "]");
                        throw var12;
                    }
                }
            }

            if (JsonUtils.jsonObjectFieldTypeIsArray(var22, "passes")) {
                var7 = var22.getAsJsonArray("passes");
                var8 = 0;

                for(var9 = var7.iterator(); var9.hasNext(); ++var8) {
                    var10 = (JsonElement)var9.next();

                    try {
                        super.parsePass(Minecraft.getMinecraft().getTextureManager(), var10);
                    } catch (Exception var18) {
                        var12 = JsonException.func_151379_a(var18);
                        var12.func_151380_a("passes[" + var8 + "]");
                        throw var12;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
