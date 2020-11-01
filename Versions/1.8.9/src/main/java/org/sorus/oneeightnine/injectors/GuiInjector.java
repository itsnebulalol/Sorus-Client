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

package org.sorus.oneeightnine.injectors;

import net.minecraft.client.gui.Gui;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.event.Event;
import org.sorus.client.event.EventCancelable;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/gui/Gui")
public class GuiInjector extends Injector<Gui> {

    public GuiInjector(Gui that) {
        super(that);
    }

    @Modify(name = "drawGradientRect", desc = "(IIIIII)V")
    public static void modifyDrawGradientRect(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new TypeInsnNode(Opcodes.NEW, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/")));
        insnList.add(new InsnNode(Opcodes.DUP));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 2));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 3));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 5));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 6));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/"), "<init>", "(IIIIII)V", false));
        insnList.add(new VarInsnNode(Opcodes.ASTORE, 100));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Event.class.getName().replace(".", "/"), "post", "()V", false));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/"), "getLeft", "()I", false));
        insnList.add(new VarInsnNode(Opcodes.ISTORE, 1));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/"), "getTop", "()I", false));
        insnList.add(new VarInsnNode(Opcodes.ISTORE, 2));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/"), "getRight", "()I", false));
        insnList.add(new VarInsnNode(Opcodes.ISTORE, 3));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/"), "getBottom", "()I", false));
        insnList.add(new VarInsnNode(Opcodes.ISTORE, 4));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/"), "getColor1", "()I", false));
        insnList.add(new VarInsnNode(Opcodes.ISTORE, 5));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RenderObjectEvent.GradientRectangle.class.getName().replace(".", "/"), "getColor2", "()I", false));
        insnList.add(new VarInsnNode(Opcodes.ISTORE, 6));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EventCancelable.class.getName().replace(".", "/"), "isCancelled", "()Z", false));
        LabelNode label = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new InsnNode(Opcodes.RETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}

