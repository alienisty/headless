package com.gmail.alienisty.headless.win32.api;

import java.io.IOException;
import com.gmail.alienisty.headless.win32.type.AccessRights;
import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Desktops functions from user32.dll
 */
public interface Desktop32 extends StdCallLibrary {

  int CDS_TEST = 0x00000002;

  int CDS_FULLSCREEN = 0x00000004;

  int DM_PELSWIDTH = 0x00080000;

  int DM_PELSHEIGHT = 0x00100000;

  int EDD_GET_DEVICE_INTERFACE_NAME = 0x00000001;

  long ENUM_CURRENT_SETTINGS = -1;

  Desktop32 INSTANCE = (Desktop32) Native.loadLibrary("user32", Desktop32.class, W32APIOptions.DEFAULT_OPTIONS);

  HANDLE GetProcessWindowStation();

  HANDLE CreateDesktop(
      String desktop,
      String device,
      Pointer devmode,
      long flags,
      long desiredAccess,
      Structure securityAttributes);

  boolean EnumDesktopWindows(
      HANDLE hDesktop,
      WNDENUMPROC lpfn,
      LPARAM lParam);

  /**
   * Creates a desktop with the specified name that is not visible by default
   *
   * @param name
   * @return
   * @throws IOException
   */
  static HANDLE CreateDesktop(String name) throws IOException {

    if (INSTANCE.GetProcessWindowStation() == null) {
      throw new IOException("Unable to create desktop " + name + ". Missing WindowStation for the process");
    }

    HANDLE desktop = INSTANCE.CreateDesktop(
        name,
        null,
        null,
        0,
        AccessRights.GENERIC_ALL,
        null);

    if (desktop == null) {
      throw new IOException("Unable to create desktop " + name);
    }

    return desktop;
  }

  /**
   * Callback for windows enumeration
   */
  interface WNDENUMPROC extends Callback {

    boolean callback(HWND hwnd, LPARAM lParam);
  }
}
