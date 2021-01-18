package org.sorus.launchwrapper;

import net.minecraft.launchwrapper.IClassTransformer;
import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.TransformerUtility;

import java.net.URL;

public class SorusTransformer extends TransformerUtility implements IClassTransformer {

    private final ClassLoader classLoader;

    public SorusTransformer(ClassLoader classLoader) {
        this.classLoader = classLoader;
        SorusStartup.addTransformerUtility(this, false);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        URL url = classLoader.getResource(name.replace(".", "/") + ".class");
        bytes = this.transformer.transform(transformedName.replace(".", "/"), bytes, classLoader, url == null ? "" : url.getPath());
        return bytes;
    }
}
