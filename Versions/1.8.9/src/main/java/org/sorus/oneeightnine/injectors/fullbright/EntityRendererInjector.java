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

package org.sorus.oneeightnine.injectors.fullbright;

import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererInjector extends Injector<EntityRenderer> {

    public EntityRendererInjector(EntityRenderer that) {
        super(that);
    }

    @Modify(name = "updateLightmap", desc = "(F)V")
    public static void modifyUpdateLightmap(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            String fieldName = ObfuscationManager.getFieldName("net/minecraft/client/settings/GameSettings", "gammaSetting");
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).name.equals(fieldName)) {
                System.out.println(node.getNext().getOpcode() + " " + ((VarInsnNode) node.getNext()).var);
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.FLOAD, 17));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getName().replace(".", "/"), "getBrightness", "(F)F", false));
                insnList.add(new VarInsnNode(Opcodes.FSTORE, 17));
                methodNode.instructions.insert(node.getNext(), insnList);
            }
        }
    }

}
