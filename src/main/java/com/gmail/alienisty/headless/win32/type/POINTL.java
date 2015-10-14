package com.gmail.alienisty.headless.win32.type;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sun.jna.Structure;

public class POINTL extends Structure {

  private static final List<String> fieldOrder = unmodifiableList(asList("x", "y"));

  public long x;

  public long y;

  @Override
  protected List<String> getFieldOrder() {
    return fieldOrder;
  }
}
