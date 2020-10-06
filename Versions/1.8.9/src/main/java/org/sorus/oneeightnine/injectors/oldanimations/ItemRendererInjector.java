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

package org.sorus.oneeightnine.injectors.oldanimations;

import net.minecraft.client.renderer.ItemRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.oldanimations.OldAnimations;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/ItemRenderer")
public class ItemRendererInjector extends Injector<ItemRenderer> {

    public ItemRendererInjector(ItemRenderer that) {
        super(that);
    }

    @Modify(name = "renderItemInFirstPerson", desc = "(F)V")
    public static void transform(MethodNode methodNode) {
        int i = 0;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode) {
                String methodName = ObfuscationManager.getMethodName("net/minecraft/client/renderer/ItemRenderer", "transformFirstPersonItem", "(FF)V");
                if(((MethodInsnNode) node).name.equals(methodName) && ((MethodInsnNode) node).desc.equals("(FF)V") && i < 4) {
                    methodNode.instructions.remove(node.getPrevious());
                    methodNode.instructions.insertBefore(node, new VarInsnNode(Opcodes.FLOAD, 4));
                    methodNode.instructions.insertBefore(node, new MethodInsnNode(Opcodes.INVOKESTATIC, ItemRendererInjector.class.getCanonicalName().replace(".", "/"), "getAnimation", "(F)F", false));
                    i++;
                }
            }
        }
    }

    public static float getAnimation(float animation) {
        return Sorus.getSorus().getModuleManager().getModule(OldAnimations.class).shouldShowAnimInteractBreak() ? animation : 0;
    }

}
