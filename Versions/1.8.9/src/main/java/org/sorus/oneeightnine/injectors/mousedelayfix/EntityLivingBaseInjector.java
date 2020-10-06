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

package org.sorus.oneeightnine.injectors.mousedelayfix;

import net.minecraft.entity.EntityLivingBase;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/entity/EntityLivingBase")
public class EntityLivingBaseInjector extends Injector<EntityLivingBase> {

    public EntityLivingBaseInjector(EntityLivingBase that) {
        super(that);
    }

    @Modify(name = "getLook", desc = "(F)Lnet/minecraft/util/Vec3;")
    public static void transformGetLook(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new TypeInsnNode(Opcodes.INSTANCEOF, ObfuscationManager.getClassName("net/minecraft/client/entity/EntityPlayerSP")));
        LabelNode label = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new VarInsnNode(Opcodes.FLOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, ObfuscationManager.getClassName("net/minecraft/entity/Entity"), methodNode.name, methodNode.desc, false));
        insnList.add(new InsnNode(Opcodes.ARETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}
