package com.gmail.alienisty.headless;

import java.io.IOException;
import java.util.Map;
import com.gmail.alienisty.headless.win32.Win32Headless;

/**
 * This interface represents a process that is run on a virtual Desktop so that it can run on headless systems.
 */
public interface Headless {

  /**
   * Tests whether the process is still running
   *
   * @return <code>true</code> if the process is running <code>false</code> otherwise.
   */
  boolean isRunning();

  /**
   * Allows to stop the process as cleanly as possible.
   *
   * Implementations are supposed to send a request to stop to the process first and then kill the process forcibly if
   * it didn't stop within a specified timeout interval.
   */
  void stop();

  /**
   * Factory method that creates and starts the specified process.
   *
   * @param commandLine
   *          the full command line including arguments.
   * @param environment
   *          an optional environment to use with the process. If not specified, the environment is inherited from the
   *          calling process.
   *
   * @return an instance of the running headless process.
   * @throws IOException
   */
  static Headless with(String commandLine, Map<String, String> environment)
      throws IOException {
    Headless process;
    String os = System.getProperty("os.name");
    if (os.contains("Windows")) {
      process = new Win32Headless(commandLine, environment);
    } else {
      throw new UnsupportedOperationException("Unsupported OS " + os);
    }
    if (!process.isRunning()) {
      throw new IOException("Process " + commandLine + " failed to start");
    }
    return process;
  }
}
