package com.pickyeaters.logic.utils;

public enum OS {
    UNKNOWN,
    LINUX,
    UNIX,
    WINDOWS,
    MAC;

    private static final String APP_DIRECTORY_NAME = "PickyEaters";
    private static final String APP_CONFIG_FILE = "PickyEaters.conf";

    public static String getConfigFilePath() {
        String configDir = getConfigDir();
        // just return empty string if cant find a config dir
        if (configDir.isEmpty()) {
            return "";
        }

        // else return config file path
        return configDir + getSeparator() + getAppConfigFile();
    }

    public static String getNameOS() {
        return System.getProperty("os.name");
    }

    public static String getHomeDir() {
        return System.getProperty("user.home");
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }
    public static String getSeparator() {
        return System.getProperty("file.separator");
    }
    public static String getConfigDatabaseHost() {
        return System.getenv("PICKY_DATABASE_HOST");
    }

    public static String getConfigDatabaseName() {
        return System.getenv("PICKY_DATABASE_NAME");
    }

    public static String getConfigDatabasePort() {
        return System.getenv("PICKY_DATABASE_PORT");
    }

    public static String getConfigDatabaseUser() {
        return System.getenv("PICKY_DATABASE_USER");
    }

    public static String getConfigDatabasePassword() {
        return System.getenv("PICKY_DATABASE_PASS");
    }

    public static String getConfigLocale() {
        return System.getenv("PICKY_DATABASE_LOCALE");
    }
    public static OS getCurrentOS() {
        String osname = getNameOS().toLowerCase();

        // If OS is windows
        if (osname.contains("win")) {
            return WINDOWS;
        }
        // If OS is mac
        if (osname.contains("mac")) {
            return MAC;
        }
        // If OS is unix
        if (osname.contains("nix")) {
            return UNIX;
        }
        // If OS is linux
        if (osname.contains("nux")) {
            return LINUX;
        }
        // Else if cant determine which OS
        return UNKNOWN;
    }

    public static String getAppConfigFile() {
        return APP_CONFIG_FILE;
    }

    public static String getAppDirectoryName() {
        return APP_DIRECTORY_NAME;
    }

    public static String getConfigDir() {

        OS currentOS = getCurrentOS();

        return switch (currentOS) {
            case LINUX, UNIX -> getConfigDirUnix();
            case WINDOWS -> getConfigDirWindows();
            case MAC -> getConfigDirMac();
            default ->
                // TODO: Should we throw exception?
                // return an empty string if cant figure out which os
                    "";
        };
    }

    private static String getConfigDirUnix() {
        // try to use xdg
        String xdg = System.getenv("XDG_CONFIG_HOME");
        if (xdg != null) {
            return xdg + "/." + getAppDirectoryName();
        }

        // if cant use xdg try fallback to home + ".config/"
        return getHomeDir() + "/.config/" + getAppDirectoryName();
    }

    private static String getConfigDirWindows() {
        // try to use APPDATA
        String appdata = System.getenv("APPDATA");
        if (appdata != null) {
            return appdata + "\\" + getAppDirectoryName();
        }

        String home = getHomeDir();
        // based on https://learn.microsoft.com/it-it/windows/win32/sysinfo/operating-system-version
        // check if system version is higher than XP
        final boolean IS_XP_HIGHER = (Float.parseFloat(System.getProperty("os.version")) > 5.2f);
        if (IS_XP_HIGHER) {
            return home + "\\AppData\\Roaming\\" + getAppDirectoryName();
        } else {
            return "C:\\Documents and Settings\\" + getUserName() + "\\Application Data\\" + getAppDirectoryName();
        }
    }

    private static String getConfigDirMac() {
        String home = getHomeDir();
        return home + "/Library/Application Support/" + getAppDirectoryName();
    }
}