

package org.sorus.oneeightnine.injectors;


import net.minecraft.client.gui.GuiIngame;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.event.Event;
import org.sorus.client.event.EventCancelable;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/gui/GuiIngame")
public class GuiIngameInjector extends Injector<GuiIngame> {

    public GuiIngameInjector(GuiIngame that) {
        super(that);
    }

    @Modify(name = "renderGameOverlay", desc = "(F)V")
    public static void modifyRenderGameOverlay(MethodNode methodNode) {
        String showCrosshairClass = ObfuscationManager.getClassName("net/minecraft/client/gui/GuiIngame");
        String showCrosshairMethod = ObfuscationManager.getMethodName("net/minecraft/client/gui/GuiIngame", "showCrosshair", "()Z");
        String drawTexturedModalRect = ObfuscationManager.getMethodName("net/minecraft/client/gui/Gui", "drawTexturedModalRect", "(IIIIII)V");
        LabelNode label = null;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(showCrosshairClass) && ((MethodInsnNode) node).name.equals(showCrosshairMethod) && ((MethodInsnNode) node).desc.equals("()Z")) {
                AbstractInsnNode insnNode = node.getNext().getNext();
                InsnList insnList = new InsnList();
                insnList.add(new TypeInsnNode(Opcodes.NEW, RenderObjectEvent.Crosshair.class.getName().replace(".", "/")));
                insnList.add(new InsnNode(Opcodes.DUP));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, RenderObjectEvent.Crosshair.class.getName().replace(".", "/"), "<init>", "()V", false));
                insnList.add(new VarInsnNode(Opcodes.ASTORE, 100));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Event.class.getName().replace(".", "/"), "post", "()V", false));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EventCancelable.class.getName().replace(".", "/"), "isCancelled", "()Z", false));
                label = new LabelNode();
                insnList.add(new JumpInsnNode(Opcodes.IFNE, label));
                methodNode.instructions.insert(insnNode, insnList);
            }
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(drawTexturedModalRect) && ((MethodInsnNode) node).desc.equals("(IIIIII)V")) {
                methodNode.instructions.insert(node, label);
            }
        }
    }

    @Modify(name = "renderScoreboard", desc = "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V")
    public static void modifyRenderScoreboard(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new TypeInsnNode(Opcodes.NEW, RenderObjectEvent.Sidebar.class.getName().replace(".", "/")));
        insnList.add(new InsnNode(Opcodes.DUP));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, RenderObjectEvent.Sidebar.class.getName().replace(".", "/"), "<init>", "()V", false));
        insnList.add(new VarInsnNode(Opcodes.ASTORE, 100));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Event.class.getName().replace(".", "/"), "post", "()V", false));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 100));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EventCancelable.class.getName().replace(".", "/"), "isCancelled", "()Z", false));
        LabelNode label = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new InsnNode(Opcodes.RETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}
