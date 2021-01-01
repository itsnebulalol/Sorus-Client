package org.sorus.client.module.impl.music;

import java.io.File;

public class SoundConverter {

  public static ISound fromMP4File(File file) {
    return new MP4Sound(file.getName().replace(".mp4", ""), file);
  }
}
