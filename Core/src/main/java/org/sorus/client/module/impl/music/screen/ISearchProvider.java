package org.sorus.client.module.impl.music.screen;

import java.util.List;

public interface ISearchProvider {

  List<String> search(String term);

  void get(String value);
}
