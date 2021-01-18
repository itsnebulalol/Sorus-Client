package org.sorus.javaagent;

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.version.IVersion;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class AgentMain {

    public static void premain(String args, Instrumentation inst) {
        Map<String, String> launchArgs = SorusStartup.getArgsMap(args);
        try {
            Class<?> versionMain = Class.forName("sorus.VersionMain");
            Class<?> version = (Class<?>) versionMain.getMethod("getVersion").invoke(null);
            SorusStartup.start((Class<? extends IVersion>) version, null, launchArgs);
            SorusStartup.addTransformerUtility(new InstrumentationTransformerUtility(inst), false);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
