package com.gmail.alienisty.headless.win32.api;

import org.junit.Test;
import com.gmail.alienisty.headless.win32.type.DEVMODE;

public class Desktop32Test {

  private static final Desktop32 desktop = Desktop32.INSTANCE;

  @Test
  public void testEnumDisplaySettings() {
    DEVMODE result = new DEVMODE();
    result.clear();
    for (int i = 0; desktop.EnumDisplaySettings(null, i, result); i++) {
      System.out.println(result);
      result.clear();
    }
  }
}
