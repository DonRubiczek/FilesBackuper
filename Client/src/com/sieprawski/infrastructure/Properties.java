package com.sieprawski.infrastructure;

public class Properties {

    public static String appName			= "backuper-client";
    public static String appDataDir 		= System.getenv("APPDATA") + "\\" + appName + "\\";

    public static String userFile 			= "user.dat";
    public static String globalConfigFile	= "config.dat";

    public static int bufferSize			= 4096;

    public static String listOfFilesBeingBackuped = "filesTmp.dat";

    public static String userFilesList 			= "files.dat";
}
