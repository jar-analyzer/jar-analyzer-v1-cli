package me.n1ar4.util;

public class OSUtil {
    public static boolean isWindows() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().contains("windows");
    }
}
