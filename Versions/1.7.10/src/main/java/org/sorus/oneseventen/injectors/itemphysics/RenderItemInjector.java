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

package org.sorus.oneseventen.injectors.itemphysics;

import net.minecraft.client.renderer.entity.RenderItem;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

import java.util.List;

@Hook("net/minecraft/client/renderer/entity/RenderItem")
public class RenderItemInjector extends Injector<RenderItem> {

    private static boolean foundRotate;

    public RenderItemInjector(RenderItem that) {
        super(that);
    }

    @Modify(name = "doRender", desc = "(Lnet/minecraft/entity/item/EntityItem;DDDFF)V")
    public static void transformDoRender(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals("glTranslatef")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.FLOAD, 11));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/sorus/oneseventen/injectors/itemphysics/RenderItemHook", "performTranslations", "(Ljava/lang/Object;F)V", false));
                methodNode.instructions.insert(node, insnList);
            }
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals("glRotatef") && !foundRotate) {
                foundRotate = true;
                AbstractInsnNode node1 = node.getPrevious().getPrevious().getPrevious().getPrevious();
                methodNode.instructions.insert(node1, new MethodInsnNode(Opcodes.INVOKESTATIC, "org/sorus/oneseventen/injectors/itemphysics/RenderItemHook", "getRotate", "(F)F", false));
            }
        }
    }

    private static boolean checking;

    @Modify(name = "renderDroppedItem", desc = "(Lnet/minecraft/entity/item/EntityItem;Lnet/minecraft/util/IIcon;IFFFF)V")
    public static void modifyRenderDroppedItem(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof LineNumberNode && node.getNext() instanceof FrameNode && node.getNext().getNext() instanceof VarInsnNode && node.getNext().getNext().getNext() instanceof FieldInsnNode && node.getNext().getNext().getNext().getNext() instanceof InsnNode) {
                checking = true;
            }
            if(checking) {
                if(node instanceof LdcInsnNode && ((LdcInsnNode) node).cst.equals(57.295776f)) {
                    methodNode.instructions.insert(node, new MethodInsnNode(Opcodes.INVOKESTATIC, "org/sorus/oneseventen/injectors/itemphysics/RenderItemHook", "getRotate", "(F)F", false));
                }
            }
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals("glRotatef")) {
                checking = false;
            }
        }
    }

}
