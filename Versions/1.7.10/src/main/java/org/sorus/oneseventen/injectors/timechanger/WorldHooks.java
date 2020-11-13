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

package org.sorus.oneseventen.injectors.timechanger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.timechanger.TimeChanger;
import sun.reflect.Reflection;

public class WorldHooks {

    public static long getAlteredTime() {
        TimeChanger timeChanger = Sorus.getSorus().getModuleManager().getModule(TimeChanger.class);
        String className = new Exception().getStackTrace()[3].getClassName();
        if(timeChanger.isEnabled() && (className.equals(EntityRenderer.class.getName()) || className.equals(RenderGlobal.class.getName()))) {
            return timeChanger.getTime();
        }
        if(Minecraft.getMinecraft().theWorld == null) {
            return 0;
        }
        return Minecraft.getMinecraft().theWorld.getWorldTime();
    }

}
