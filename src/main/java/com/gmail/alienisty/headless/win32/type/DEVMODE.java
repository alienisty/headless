package com.gmail.alienisty.headless.win32.type;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sun.jna.Structure;
import com.sun.jna.Union;

public class DEVMODE extends Structure {

  public static class ByReference extends DEVMODE implements Structure.ByReference {
  };

  private static final int CCHDEVICENAME = 32;

  private static final int CCHFORMNAME = 32;

  private static final List<String> fieldOrder = unmodifiableList(asList("dmDeviceName", "dmSpecVersion",
      "dmDriverVersion", "dmSize", "dmDriverExtra", "dmFields", "specs", "dmColor", "dmDuplex", "dmYResolution",
      "dmTTOption", "dmCollate", "dmFromName", "dmLogPixels", "dmBitsPerPel", "dmPelsWidth", "dmPelsHeight", "flags",
      "dmDisplayFrequency", "dmICMMethod", "dmICMIntent", "dmMediaType", "dmDitherType", "dmReserved1", "dmReserved2",
      "dmPanningWidth", "dmPanningHeight"));

  public char[] dmDeviceName = new char[CCHDEVICENAME];

  public int dmSpecVersion;

  public int dmDriverVersion;

  public int dmSize;

  public int dmDriverExtra;

  public long dmFields;

  public UNION specs;

  public short dmColor;

  public short dmDuplex;

  public short dmYResolution;

  public short dmTTOption;

  public short dmCollate;

  public char[] dmFromName = new char[CCHFORMNAME];

  public int dmLogPixels;

  public long dmBitsPerPel;

  public long dmPelsWidth;

  public long dmPelsHeight;

  public Flags flags;

  public long dmDisplayFrequency;

  public long dmICMMethod;

  public long dmICMIntent;

  public long dmMediaType;

  public long dmDitherType;

  public long dmReserved1;

  public long dmReserved2;

  public long dmPanningWidth;

  public long dmPanningHeight;

  @Override
  public void clear() {
    super.clear();
    dmSize = size();
  }

  @Override
  protected List<String> getFieldOrder() {
    return fieldOrder;
  }

  public static class UNION extends Union {

    public Structure1 s1;

    public Structure1 s2;

    public static class Structure1 extends Structure {

      private static final List<String> fieldOrder = unmodifiableList(asList("dmOrientation", "dmPaperSize",
          "dmPaperLength", "dmPaperWidth", "dmScale", "dmCopies", "dmDefaultSource", "dmPrintQuality"));

      public short dmOrientation;

      public short dmPaperSize;

      public short dmPaperLength;

      public short dmPaperWidth;

      public short dmScale;

      public short dmCopies;

      public short dmDefaultSource;

      public short dmPrintQuality;

      @Override
      protected List<String> getFieldOrder() {
        return fieldOrder;
      }
    }

    public static class Structure2 extends Structure {

      private static final List<String> fieldOrder = unmodifiableList(asList("dmPosition", "dmDisplayOrientation",
          "dmDisplayFixedOutput"));

      public POINTL dmPosition;

      public long dmDisplayOrientation;

      public long dmDisplayFixedOutput;

      @Override
      protected List<String> getFieldOrder() {
        return fieldOrder;
      }
    }
  }

  public static class Flags extends Union {

    public long dmDisplayFlags;

    public long dmNup;
  }
}
