package com.github.worldmaomao.disk;

/**
 * 硬盘扫描工厂类
 *
 */
public class DiskScannerFactory {
    private static String OS = System.getProperty("os.name").toLowerCase();


    public static DiskScanner create() {
        if (isMac()) {
            return new OsxDiskScanner();
        }
        if (isWindows()) {
            return new WindowsDiskScanner();
        }
        if (isUnix()) {
            return new LinuxDiskScanner();
        }
        return null;
    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0
                || OS.indexOf("nux") >= 0
                || OS.indexOf("aix") > 0);
    }
}
