package org.sorus.oneeightnine.injectors.optimisations;

public class ClippingHelperImplHook {

    private static int i = 0;

    public static boolean skipUpdate() {
        i++;
        if(i == 3) {
            i = 0;
            return false;
        }
        return true;
    }

}
