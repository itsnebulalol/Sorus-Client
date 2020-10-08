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

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.dev.DevClassLoader;
import org.sorus.client.startup.impl.ClassLoaderTransformerUtility;
import org.sorus.oneeightnine.Version;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * Used in a dev environment to run Sorus with a class loader transformer instead of java agents.
 */
public class Start {

    /**
     * Runs the minecraft main class and initializes Sorus along with it, for use in a dev environment.
     * @param args the program arguments
     */
    public static void main(String[] args) {
        ClassLoaderTransformerUtility utility = new ClassLoaderTransformerUtility();
        DevClassLoader loader = new DevClassLoader(((URLClassLoader) Start.class.getClassLoader()).getURLs(), utility);
        SorusStartup.start(Version.class, utility, loader, System.getProperty("sorus.args"),true);
        try {
            final Class<?> clazz = Class.forName("net.minecraft.client.main.Main", false, loader);
            final Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch(NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
