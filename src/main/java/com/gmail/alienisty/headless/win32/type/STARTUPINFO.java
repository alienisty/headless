package com.gmail.alienisty.headless.win32.type;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WTypes.LPSTR;
import com.sun.jna.platform.win32.WinNT.HANDLE;

/**
 * For some reason we can't use the JNA platform defined structure that uses Strings instead of LPSTR. I need to
 * investigate why, but for the minute redefining it with LPSTR instead of Strings solves the problem.
 */
public class STARTUPINFO extends Structure {

  private static final List<String> fieldOrder = unmodifiableList(asList(
      "size", "reserved", "desktop", "title", "x", "y", "xSize", "ySize", "xCountChars", "yCountChars", "fillAttribute",
      "flags", "showWindow", "reserved2Size", "reserved2", "stdInput", "stdOutput", "stdError"));

  public int size = size();

  public LPSTR reserved;

  public LPSTR desktop;

  public LPSTR title;

  public int x;

  public int y;

  public int xSize;

  public int ySize;

  public int xCountChars;

  public int yCountChars;

  public int fillAttribute;

  public int flags;

  public short showWindow;

  public short reserved2Size;

  public Pointer reserved2;

  public HANDLE stdInput;

  public HANDLE stdOutput;

  public HANDLE stdError;

  @Override
  protected List<String> getFieldOrder() {
    return fieldOrder;
  }
}