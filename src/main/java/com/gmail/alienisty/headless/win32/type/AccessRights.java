package com.gmail.alienisty.headless.win32.type;

/**
 * Access rights constants used to create desktops
 */
public interface AccessRights {

  long DELETE = 0x00010000; //  Required to delete the object.

  long READ_CONTROL = 0x00020000; //  Required to read information in the security descriptor for the object, not including the information in the SACL. To read or write the SACL, you must request the ACCESS_SYSTEM_SECURITY access right. For more information, see SACL Access Right.

  long SYNCHRONIZE = 0x00100000; // Not supported for desktop objects.

  long WRITE_DAC = 0x00040000; // Required to modify the DACL in the security descriptor for the object.

  long WRITE_OWNER = 0x00080000; //Required to change the owner in the security descriptor for the object.

  long DESKTOP_CREATEMENU = 0x0004; //  Required to create a menu on the desktop.

  long DESKTOP_CREATEWINDOW = 0x0002; //  Required to create a window on the desktop.

  long DESKTOP_ENUMERATE = 0x0040; // Required for the desktop to be enumerated.

  long DESKTOP_HOOKCONTROL = 0x0008; // Required to establish any of the window hooks.

  long DESKTOP_JOURNALPLAYBACK = 0x0020; // Required to perform journal playback on a desktop.

  long DESKTOP_JOURNALRECORD = 0x0010; // Required to perform journal recording on a desktop.

  long DESKTOP_READOBJECTS = 0x0001; // Required to read objects on the desktop.

  long DESKTOP_SWITCHDESKTOP = 0x0100; // Required to activate the desktop using the SwitchDesktop function.

  long DESKTOP_WRITEOBJECTS = 0x0080; // Required to write objects on the desktop.

  long STANDARD_RIGHTS_REQUIRED = DELETE | READ_CONTROL | WRITE_DAC | WRITE_OWNER;

  long GENERIC_ALL = DESKTOP_CREATEMENU | DESKTOP_CREATEWINDOW | DESKTOP_ENUMERATE | DESKTOP_HOOKCONTROL
      | DESKTOP_JOURNALPLAYBACK | DESKTOP_JOURNALRECORD | DESKTOP_READOBJECTS | DESKTOP_SWITCHDESKTOP
      | DESKTOP_WRITEOBJECTS | STANDARD_RIGHTS_REQUIRED;
}
