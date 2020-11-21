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

package org.sorus.client.module;

import java.util.Arrays;

public abstract class VersionDecision {

  public abstract boolean allow(String version);

  public static class AllowCertain extends VersionDecision {

    private final String[] versions;

    public AllowCertain(String... versions) {
      this.versions = versions;
    }

    @Override
    public boolean allow(String version) {
      return Arrays.asList(versions).contains(version);
    }
  }

  public static class DenyCertain extends VersionDecision {

    private final String[] versions;

    public DenyCertain(String... versions) {
      this.versions = versions;
    }

    @Override
    public boolean allow(String version) {
      return !Arrays.asList(versions).contains(version);
    }
  }

  public static class Allow extends VersionDecision {

    @Override
    public boolean allow(String version) {
      return true;
    }
  }
}
