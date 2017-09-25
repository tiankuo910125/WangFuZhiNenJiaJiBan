package com.demo.smarthome.base.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;

public class TestbedUtils {
    private static final int ERROR = -1;
    private static final int DEFAULT_SIZE = 53000000;
    private static StringBuilder sFormatBuilder = new StringBuilder();
    private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
    private static final Object[] sTimeArgs = new Object[5];

    public static String makeTimeString(Context context, long secs) {
        if (secs < 0)
            return "unknown";
        String durationformat = "%1$02d:%3$02d:%5$02d";
        // secs < 3600 ? "%2$d:%5$02d" : "%1$d:%3$02d:%5$02d";

        /*
         * Provide multiple arguments so the format can be changed easily by
         * modifying the xml.
         */
        sFormatBuilder.setLength(0);

        final Object[] timeArgs = sTimeArgs;
        timeArgs[0] = secs / 3600;
        timeArgs[1] = secs / 60;
        timeArgs[2] = (secs / 60) % 60;
        timeArgs[3] = secs;
        timeArgs[4] = secs % 60;

        return sFormatter.format(durationformat, timeArgs).toString();
    }

    public static String makeDurationString(int min, int sec) {
        String durationformat = "%1$02d:%2$02d";
        sFormatBuilder.setLength(0);
        return sFormatter.format(durationformat, min, sec).toString();
    }

    public static String getRecordDirectory(final Context context) {
        final File recDir = new File(Environment.getExternalStorageDirectory(), "TestBed" + "/Video/");
        recDir.mkdirs();
        return recDir.getAbsolutePath();
    }

    static public boolean externalMemoryAvailable() {

        return Environment.getExternalStorageState()

        .equals(Environment.MEDIA_MOUNTED);

    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;

    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;

    }

    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return DEFAULT_SIZE;
        }
    }

    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    public static boolean IsThereEnoughSpace(long size) {
        if (externalMemoryAvailable())
            return getAvailableExternalMemorySize() > size;
        else
            return getAvailableInternalMemorySize() > size;
    }
}
