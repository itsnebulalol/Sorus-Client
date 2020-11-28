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

package org.sorus.oneseventen;

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.impl.InstrumentationTransformerUtility;
import org.sorus.client.version.*;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link IVersion} for 1.8.9.
 */
public class Version implements IVersion {

    public static void premain(String args, Instrumentation inst) {
        SorusStartup.start(Version.class, new InstrumentationTransformerUtility(inst), null, args, false);
    }

    private final List<Object> datas = new ArrayList<>();

    public Version() {
        this.datas.add(new Renderer(this));
        this.datas.add(new GLHelper());
        this.datas.add(new Screen());
        this.datas.add(new Game());
        this.datas.add(new Input(this));
    }

    @Override
    public <T> T getData(Class<T> clazz) {
        for(Object object : this.datas) {
            if(clazz.isAssignableFrom(object.getClass())) {
                return (T) object;
            }
        }
        return null;
    }
}
