import org.sorus.client.startup.SorusStartup;
import org.sorus.client.version.IVersion;

import java.lang.instrument.Instrumentation;
import java.util.Map;

public class AgentMain {

    public static void premain(String args, Instrumentation inst) {
        Map<String, String> launchArgs = SorusStartup.getArgsMap(args);
        try {
            Class<?> versionMain = Class.forName(launchArgs.get("versionMain"));
            SorusStartup.start((Class<? extends IVersion>) versionMain, new InstrumentationTransformerUtility(inst), null, launchArgs, false);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
