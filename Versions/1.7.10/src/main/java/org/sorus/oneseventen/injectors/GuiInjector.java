

package org.sorus.oneseventen.injectors;

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

