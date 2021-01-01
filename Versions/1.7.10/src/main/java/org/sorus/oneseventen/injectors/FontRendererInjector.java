

package org.sorus.oneseventen.injectors;

import net.minecraft.client.gui.FontRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.event.Event;
import org.sorus.client.event.EventCancelable;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

import java.util.List;

@Hook("net/minecraft/client/gui/FontRenderer")
public class FontRendererInjector extends Injector<FontRenderer> {

    public FontRendererInjector(FontRenderer that) {
        super(that);
    }

    @Modify(name = "drawString", desc = "(Ljava/lang/String;FFIZ)I")
    public static void modifyDrawString(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new TypeInsnNode(Opcodes.NEW, RenderObjectEvent.Text.class.getName().replace(".", "/")));
        insnList.add(new InsnNode(Opcodes.DUP));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, RenderObjectEvent.Text.class.getName().replace(".", "/"), "<init>", "(Ljava/lang/String;)V", false));
        insnList.add(new VarInsnNode(Opcodes.ASTORE, 100));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Event.class.getName().replace(".", "/"), "post", "()V", false));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RenderObjectEvent.Text.class.getName().replace(".", "/"), "getText", "()Ljava/lang/String;", false));
        insnList.add(new VarInsnNode(Opcodes.ASTORE, 1));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EventCancelable.class.getName().replace(".", "/"), "isCancelled", "()Z", false));
        LabelNode label = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new InsnNode(Opcodes.ICONST_0));
        insnList.add(new InsnNode(Opcodes.IRETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}
