package org.sorus.launchwrapper;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.sorus.client.startup.SorusStartup;
import org.sorus.client.version.IVersion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;

public class SorusLaunch {

    public static String[] args;

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        //OptionSpec<String> realMain = parser.accepts("realMain").withRequiredArg();
        OptionSpec<String> sorusArgs = parser.accepts("sorusArgs").withRequiredArg();
        OptionSet options = parser.parse(args);
        String[] newArgs = new String[args.length + 2];
        System.arraycopy(args, 0, newArgs, 0, args.length);
        newArgs[newArgs.length - 2] = "--tweakClass";
        newArgs[newArgs.length - 1] = "org.sorus.launchwrapper.SorusTweaker";
        SorusLaunch.args = newArgs;
        Class<?> versionMain = Class.forName("sorus.VersionMain");
        Class<?> version = (Class<?>) versionMain.getMethod("getVersion").invoke(null);
        SorusStartup.start((Class<? extends IVersion>) version, null, SorusStartup.getArgsMap(options.valueOf(sorusArgs)));
        //String realMainClassName = options.valueOf(realMain);
        //Class<?> realMainClass = Class.forName(realMainClassName);
        new Thread(() -> {
            while(Launch.classLoader == null) {
                System.out.print("");
            }
            try {
                Field field = LaunchClassLoader.class.getDeclaredField("transformers");
                field.setAccessible(true);
                CustomArrayList<IClassTransformer> arrayList = new CustomArrayList<>(Collections.singletonList(new SorusTransformer(Launch.classLoader)));
                field.set(Launch.classLoader, arrayList);
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }).start();
        Launch.main(newArgs);
    }

}
