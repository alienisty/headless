package com.gmail.alienisty.headless.win32;

import static com.gmail.alienisty.headless.win32.api.Desktop32.CreateDesktop;
import static com.gmail.alienisty.headless.win32.api.Kernel32.CreateProcess;
import static com.gmail.alienisty.headless.win32.api.Kernel32.IsRunning;
import static com.gmail.alienisty.headless.win32.api.Kernel32.TerminateProcessCleanly;
import java.io.IOException;
import java.util.Map;
import com.gmail.alienisty.headless.Headless;
import com.gmail.alienisty.headless.win32.type.STARTUPINFO;
import com.sun.jna.platform.win32.WTypes.LPSTR;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinNT.HANDLE;

/**
 * Windows implementation of the {@link Headless} interface. It uses the windows desktops feature to create a hidden
 * desktop on which to run the process.
 */
public class Win32Headless implements Headless {

  private final HANDLE offscreenDesktop;

  private final PROCESS_INFORMATION process;

  public Win32Headless(String commandLine, Map<String, String> environment) throws IOException {

    final String HIDDEN_DESKTOP = "hidden";

    offscreenDesktop = CreateDesktop(HIDDEN_DESKTOP);

    STARTUPINFO startupinfo = new STARTUPINFO();
    startupinfo.desktop = new LPSTR(HIDDEN_DESKTOP);

    process = CreateProcess(commandLine, environment, startupinfo);
  }

  @Override
  public boolean isRunning() {
    return IsRunning(process);
  }

  @Override
  public void stop() {
    TerminateProcessCleanly(offscreenDesktop, process, 500);
  }

  @Override
  protected void finalize() throws Throwable {
    stop();
  }
}
