package com.sieprawski.infrastructure;

public class Properties {

    public static String appName = "backuper";
    public static String appDataDir = System.getenv("APPDATA") + "\\" + appName + "\\";

    public static String globalConfigFile = "config.dat";
    public static String usersFileWriteSuccess = "Users file written successfully";

    public static String usersFile = "users.dat";

    public static int bufferSize = 4096;
}
