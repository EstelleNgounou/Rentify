/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: out/host/linux-x86/bin/aidl --lang=java --structured --version 2 --hash f8d74c149f04e76b6d622db2bd8e465dae24b08c --stability vintf --min_sdk_version current -pout/soong/.intermediates/hardware/interfaces/common/aidl/android.hardware.common_interface/2/preprocessed.aidl -pout/soong/.intermediates/hardware/interfaces/common/fmq/aidl/android.hardware.common.fmq_interface/1/preprocessed.aidl --ninja -d out/soong/.intermediates/hardware/interfaces/tv/tuner/aidl/android.hardware.tv.tuner-V2-java-source/gen/android/hardware/tv/tuner/FrontendScanMessageType.java.d -o out/soong/.intermediates/hardware/interfaces/tv/tuner/aidl/android.hardware.tv.tuner-V2-java-source/gen -Nhardware/interfaces/tv/tuner/aidl/aidl_api/android.hardware.tv.tuner/2 hardware/interfaces/tv/tuner/aidl/aidl_api/android.hardware.tv.tuner/2/android/hardware/tv/tuner/FrontendScanMessageType.aidl
 */
package android.hardware.tv.tuner;
/** @hide */
public @interface FrontendScanMessageType {
  public static final int LOCKED = 0;
  public static final int END = 1;
  public static final int PROGRESS_PERCENT = 2;
  public static final int FREQUENCY = 3;
  public static final int SYMBOL_RATE = 4;
  public static final int HIERARCHY = 5;
  public static final int ANALOG_TYPE = 6;
  public static final int PLP_IDS = 7;
  public static final int GROUP_IDS = 8;
  public static final int INPUT_STREAM_IDS = 9;
  public static final int STANDARD = 10;
  public static final int ATSC3_PLP_INFO = 11;
  public static final int MODULATION = 12;
  public static final int DVBC_ANNEX = 13;
  public static final int HIGH_PRIORITY = 14;
  public static final int DVBT_CELL_IDS = 15;
}
