package com.gmail.alienisty.headless.win32.type;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sun.jna.Structure;

public class DISPLAY_DEVICE extends Structure {

  public static class ByReference extends DISPLAY_DEVICE implements Structure.ByReference {
  }

  private static final List<String> fieldOrder = unmodifiableList(
      asList("cb", "DeviceName", "DeviceString", "StateFlags", "DeviceID", "DeviceKey"));

  public long cb;

  public char DeviceName[] = new char[32];

  public char DeviceString[] = new char[128];

  public long StateFlags;

  public char DeviceID[] = new char[128];

  public char DeviceKey[] = new char[128];

  @Override
  public void clear() {
    super.clear();
    cb = size();
  }

  @Override
  public String toString() {
    String string = new String(DeviceString);
    return super.toString() + string.substring(0, string.indexOf(0));
  }

  @Override
  protected List<String> getFieldOrder() {
    return fieldOrder;
  }
}
