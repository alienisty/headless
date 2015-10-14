package com.gmail.alienisty.headless.win32.api;

import static com.sun.jna.platform.win32.WinBase.CREATE_UNICODE_ENVIRONMENT;
import static com.sun.jna.platform.win32.WinUser.WM_CLOSE;
import java.io.IOException;
import java.util.Map;
import com.gmail.alienisty.headless.win32.type.STARTUPINFO;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.SECURITY_ATTRIBUTES;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Process management functions from kernel32.dll
 */
public interface Kernel32 extends StdCallLibrary {

  Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);

  boolean CreateProcess(String applicationName,
      String commandLine,
      SECURITY_ATTRIBUTES processAttributes,
      SECURITY_ATTRIBUTES threadAttributes,
      boolean inheritHandles,
      int creationFlags,
      Object lpEnvironment,
      String currentDirectory,
      com.sun.jna.Structure startupinfo,
      PROCESS_INFORMATION processInformation);

  int GetProcessId(HANDLE process);

  boolean TerminateProcess(
      HANDLE process,
      int exitCode);

  boolean GetExitCodeProcess(
      HANDLE process,
      IntByReference exitCode);

  int WaitForSingleObject(
      HANDLE handle,
      int timeoutMs);

  boolean CloseHandle(HANDLE handle);

  int GetLastError();

  static PROCESS_INFORMATION CreateProcess(
      String commandLine,
      Map<String, String> environment,
      STARTUPINFO startupinfo) throws IOException {

    PROCESS_INFORMATION process = new PROCESS_INFORMATION();
    if (!INSTANCE.CreateProcess(
        null,
        commandLine,
        null,
        null,
        true,
        CREATE_UNICODE_ENVIRONMENT,
        environment == null || environment.isEmpty() ? null : Advapi32Util.getEnvironmentBlock(environment),
        null,
        startupinfo,
        process)) {

      throw new IOException("Error " + INSTANCE.GetLastError() + ": could not create process: " + commandLine);
    }

    return process;
  }

  static boolean IsRunning(PROCESS_INFORMATION process) {
    IntByReference exitCode = new IntByReference();
    return INSTANCE.GetExitCodeProcess(process.hProcess, exitCode) && exitCode.getValue() == WinBase.STILL_ACTIVE;
  }

  /**
   * Tries to terminate the specified process as cleanly as possible
   *
   * @param hDesktop
   *          the handle to the desktop expected to contain the process windows.
   * @param hProc
   *          the handle to the process to terminate.
   * @param timeoutMs
   *          the timeout, in milliseconds, after which the process is forcibly killed.
   */
  static void TerminateProcessCleanly(HANDLE hDesktop, PROCESS_INFORMATION process, int timeoutMs) {
    User32 user32 = User32.INSTANCE;
    Desktop32.INSTANCE.EnumDesktopWindows(
        hDesktop,
        (hWnd, pId) -> {
          IntByReference lpdwProcessId = new IntByReference();
          user32.GetWindowThreadProcessId(hWnd, lpdwProcessId);
          if (pId.intValue() == lpdwProcessId.getValue()) {
            user32.PostMessage(hWnd, WM_CLOSE, new WPARAM(0), new LPARAM(0));
          }
          return true;
        } ,
        new LPARAM(INSTANCE.GetProcessId(process.hProcess)));

    // Wait on the handle. If it signals, great. If it times out,
    // then you kill it.
    if (INSTANCE.WaitForSingleObject(process.hProcess, timeoutMs) != WinBase.WAIT_OBJECT_0) {
      INSTANCE.TerminateProcess(process.hProcess, 0);
    }

    INSTANCE.CloseHandle(process.hThread);
    INSTANCE.CloseHandle(process.hProcess);
    INSTANCE.CloseHandle(hDesktop);
  }
}
