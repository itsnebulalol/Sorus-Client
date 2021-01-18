

package org.sorus.oneeightnine.injectors;

import net.minecraft.client.renderer.RenderGlobal;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.event.Event;
import org.sorus.client.event.EventCancelable;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/RenderGlobal")
public class RenderGlobalInjector extends Injector<RenderGlobal> {

    public RenderGlobalInjector(RenderGlobal that) {
        super(that);
    }

    @Modify(name = "drawSelectionBoundingBox", desc = "(Lnet/minecraft/util/AxisAlignedBB;)V")
    public static void modifyDrawSelectionBoundingBox(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new TypeInsnNode(Opcodes.NEW, RenderObjectEvent.BlockOverlay.class.getName().replace(".", "/")));
        insnList.add(new InsnNode(Opcodes.DUP));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, RenderObjectEvent.BlockOverlay.class.getName().replace(".", "/"), "<init>", "()V", false));
        insnList.add(new VarInsnNode(Opcodes.ASTORE, 100));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Event.class.getName().replace(".", "/"), "post", "()V", false));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EventCancelable.class.getName().replace(".", "/"), "isCancelled", "()Z", false));
        LabelNode label = new LabelNode();
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, new Object[0], 0, new Object[0]));
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new InsnNode(Opcodes.RETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}
