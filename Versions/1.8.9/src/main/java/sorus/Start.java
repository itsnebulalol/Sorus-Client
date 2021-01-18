package sorus;

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.dev.DevClassLoader;
import org.sorus.client.startup.impl.ClassLoaderTransformerUtility;
import org.sorus.oneeightnine.Version;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

public class Start {

    public static void main(String[] args) {
        ClassLoaderTransformerUtility utility = new ClassLoaderTransformerUtility();
        DevClassLoader loader = new DevClassLoader(((URLClassLoader) Start.class.getClassLoader()).getURLs(), utility);
        SorusStartup.addTransformerUtility(utility, true);
        SorusStartup.start(Version.class, loader, SorusStartup.getArgsMap(System.getProperty("sorus.args")));
        try {
            final Class<?> clazz = Class.forName("net.minecraft.client.main.Main", false, loader);
            final Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch(NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
