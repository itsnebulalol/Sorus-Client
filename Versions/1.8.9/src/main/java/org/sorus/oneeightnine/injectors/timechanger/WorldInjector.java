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

package org.sorus.oneeightnine.injectors.timechanger;

import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/world/World")
public class WorldInjector extends Injector<World> {

    public WorldInjector(World that) {
        super(that);
    }

    @Modify(name = "getCelestialAngle", desc = "(F)F")
    public static void transformGetCelestialAngle(MethodNode methodNode) {
        int thisCount = 0;
        boolean deleting = false;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof VarInsnNode && ((VarInsnNode) node).var == 0) {
                thisCount++;
            }
            if(thisCount == 2) {
                deleting = true;
                thisCount = 0;
            }
            AbstractInsnNode nextNode = node.getNext();
            if(deleting) {
                methodNode.instructions.remove(node);
            }
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).desc.equals("()J")) {
                deleting = false;
                methodNode.instructions.insertBefore(nextNode, new MethodInsnNode(Opcodes.INVOKESTATIC, WorldHooks.class.getCanonicalName().replace(".", "/"), "getAlteredTime", "()J", false));
            }
        }
    }

}
