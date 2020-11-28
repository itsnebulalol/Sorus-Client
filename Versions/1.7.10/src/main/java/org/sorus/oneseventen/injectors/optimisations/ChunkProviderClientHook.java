package org.sorus.oneseventen.injectors.optimisations;

import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkProviderClientHook {

    public static List<Chunk> newList(List<Chunk> list) {
        return new ArrayList<>(list);
    }

}
