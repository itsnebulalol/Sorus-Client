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

package org.sorus.client.module.impl.music.screen;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.parser.DefaultParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class YoutubeSearchProvider implements ISearchProvider {

  private final Map<String, String> videos = new HashMap<>();

  @Override
  public List<String> search(String term) {
    videos.clear();
    try {
      URL url =
          new URL(
              "https://www.youtube.com/results?search_query="
                  + term.replace(" ", "+").toLowerCase());
      InputStream stream = url.openStream();
      Scanner scanner = new Scanner(stream);
      StringBuilder builder = new StringBuilder();
      while (scanner.hasNextLine()) {
        builder.append(scanner.nextLine());
      }
      String string = builder.toString();
      int index = 0;
      while (index != 28) {
        index = string.indexOf("]},\"title\":{\"runs\":[{\"text\":\"", index) + 29;
        int videoIdIndex = string.indexOf("\"watchEndpoint\":{\"videoId\":\"", index) + 28;
        videos.put(
            string.substring(index, string.indexOf("\"", index)),
            string.substring(videoIdIndex, string.indexOf("\"", videoIdIndex)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>(videos.keySet());
  }

  @Override
  public void get(String value) {
    new Thread(
            () -> {
              try {
                String videoID = this.videos.get(value);
                YoutubeDownloader downloader = new YoutubeDownloader(new DefaultParser());
                downloader.addCipherFunctionPattern(
                    2,
                    "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
                downloader.setParserRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
                downloader.setParserRetryOnFailure(1);
                YoutubeVideo video = downloader.getVideo(videoID);
                File outputDir = new File("sorus/music");
                outputDir.mkdirs();
                Format format1 = video.findFormatByItag(18);
                video.download(format1, outputDir);
              } catch (YoutubeException | IOException e) {
                e.printStackTrace();
              }
            })
        .start();
  }
}
