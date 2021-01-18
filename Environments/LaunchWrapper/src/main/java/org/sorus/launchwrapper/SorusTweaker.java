package org.sorus.launchwrapper;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SorusTweaker implements ITweaker {

    @Override
    public void acceptOptions(List<String> list, File file, File file1, String s) {

    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {

    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        System.out.println(Launch.blackboard);
        System.out.println(Launch.blackboard.get("Tweaks") + " " + this);
        int tweaks = Collections.frequency(Arrays.asList(SorusLaunch.args), "--tweakClass");
        if(tweaks > 1) {
            return new String[0];
        } else {
            return SorusLaunch.args;
        }
    }

}
