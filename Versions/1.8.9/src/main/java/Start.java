

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.dev.DevClassLoader;
import org.sorus.client.startup.impl.ClassLoaderTransformerUtility;
import org.sorus.oneeightnine.Version;

import net.minecraft.client.Minecraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

import com.google.common.collect.Queues;


public class Start {


    public static void main(String[] args) {
        ClassLoaderTransformerUtility utility = new ClassLoaderTransformerUtility();
        DevClassLoader loader = new DevClassLoader(((URLClassLoader) Start.class.getClassLoader()).getURLs(), utility);
        SorusStartup.start(Version.class, utility, loader, SorusStartup.getLaunchArgs(System.getProperty("sorus.args")), true);
        try {
            final Class<?> clazz = Class.forName("net.minecraft.client.main.Main", false, loader);
            final Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch(NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
