package com.gmail.alienisty.headless.win32.api;

import static com.sun.jna.platform.win32.WinUser.MONITORINFOF_PRIMARY;
import java.io.IOException;
import com.gmail.alienisty.headless.win32.type.AccessRights;
import com.gmail.alienisty.headless.win32.type.DEVMODE;
import com.gmail.alienisty.headless.win32.type.DISPLAY_DEVICE;
import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.MONITORINFO;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Desktops functions from user32.dll
 */
public interface Desktop32 extends StdCallLibrary {

  int CDS_TEST = 0x00000002;

  int CDS_FULLSCREEN = 0x00000004;

  long DM_PELSWIDTH = 0x00080000L;

  long DM_PELSHEIGHT = 0x00100000L;

  int EDD_GET_DEVICE_INTERFACE_NAME = 0x00000001;

  Desktop32 INSTANCE = (Desktop32) Native.loadLibrary("user32", Desktop32.class, W32APIOptions.DEFAULT_OPTIONS);

  HANDLE GetProcessWindowStation();

  boolean EnumDisplayDevices(
      String lpDevice,
      long iDevNum,
      DISPLAY_DEVICE lpDisplayDevice,
      long dwFlags);

  boolean EnumDisplaySettings(
      Pointer lpszDeviceName,
      long iModeNum,
      DEVMODE lpDevMode);

  int ChangeDisplaySettings(
      DEVMODE lpDevMode,
      long dwflags);

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
  static HANDLE CreateDesktop(String name, int minumumWidth, int minimumHeight) throws IOException {

    if (INSTANCE.GetProcessWindowStation() == null) {
      throw new IOException("Unable to create desktop " + name + ". Missing WindowStation for the process");
    }

    final User32 user32 = User32.INSTANCE;

    user32.EnumDisplayMonitors(null, null, (hMonitor, hdcMonitor, lprcMonitor, dwData) -> {
      MONITORINFO info = new MONITORINFO();
      user32.GetMonitorInfo(hMonitor, info);
      if ((info.dwFlags & MONITORINFOF_PRIMARY) == MONITORINFOF_PRIMARY) {
        int currentWidth = Math.abs(info.rcMonitor.right - info.rcMonitor.left);
        int currentHeigth = Math.abs(info.rcMonitor.bottom - info.rcMonitor.top);
        if (currentWidth < minumumWidth || currentHeigth < minimumHeight) {
          DEVMODE result = new DEVMODE();
          result.clear();
          INSTANCE.EnumDisplaySettings(null, 0, result);
          result.dmPelsWidth = Math.max(currentWidth, minumumWidth);
          result.dmPelsHeight = Math.max(currentHeigth, minimumHeight);
          result.dmFields = DM_PELSWIDTH | DM_PELSHEIGHT;
          switch (INSTANCE.ChangeDisplaySettings(result, CDS_FULLSCREEN)) {
          case 0: // DISP_CHANGE_SUCCESSFUL       0
            break;
          case 1: //DISP_CHANGE_RESTART          1
            throw new RuntimeException(
                "DISP_CHANGE_RESTART: The computer must be restarted for the graphics mode to work");
          case -1: // DISP_CHANGE_FAILED          -1
            throw new RuntimeException("DISP_CHANGE_FAILED: The display driver failed the specified graphics mode");
          case -2: // DISP_CHANGE_BADMODE         -2
            throw new RuntimeException("DISP_CHANGE_BADMODE: The graphics mode is not supported");
          case -3: // DISP_CHANGE_NOTUPDATED      -3
            throw new RuntimeException("DISP_CHANGE_NOTUPDATED: Unable to write settings to the registry");
          case -4: // DISP_CHANGE_BADFLAGS        -4
            throw new RuntimeException("DISP_CHANGE_BADFLAGS: An invalid set of flags was passed in");
          case -5: // DISP_CHANGE_BADPARAM        -5
            throw new RuntimeException(
                "DISP_CHANGE_BADPARAM: An invalid parameter was passed in. This can include an invalid flag or combination of flags");
          case -6: // DISP_CHANGE_BADDUALVIEW     -6
            throw new RuntimeException(
                "DISP_CHANGE_BADDUALVIEW: The settings change was unsuccessful because the system is DualView capable");
          }
        }
      }
      return 1;
    } , null);

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
