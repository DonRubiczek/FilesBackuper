package com.sieprawski.infrastructure;

import javafx.application.Application;

import java.io.*;

public class AppData {

    public static boolean appDirExists() {

        File f = new File(Properties.appDataDir);

        boolean result = f.exists();
        System.out.println("App directory exists: " + result);
        return result;

    }

    public static boolean createAppDir() {

        File f = new File(Properties.appDataDir);

        boolean result = f.mkdir();
        System.out.println("App directory creation result: " + result);
        return result;

    }

    public static boolean userFileExists() {

        File f = new File(Properties.appDataDir + Properties.userFile);

        boolean result = f.exists();

        if (result) {
            System.out.println("User file exists");
        } else {
            System.out.println("User file does not exist");
        }

        return result;

    }

    public static boolean globalConfigFileExists() {

        File f = new File(Properties.appDataDir + Properties.globalConfigFile);

        boolean result = f.exists();

        if (result) {
            System.out.println("Global config file exists");
        } else {
            System.out.println("Global config file does not exist");
        }

        return result;
    }

    public static boolean isConfigured() {

        if(!userFileExists() || !globalConfigFileExists()) {

            //Application.launch(Login.class, "initiateApp");

        }

        if (userFileExists() && globalConfigFileExists()) {
            return true;
        } else {
            return false;
        }

    }

    public static GlobalConfig readGlobalConfig() throws Exception {

        File file = new File(Properties.appDataDir + Properties.globalConfigFile);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        GlobalConfig config = (GlobalConfig)ois.readObject();
        ois.close();

        return config;

    }

    public static GlobalConfig saveGlobalConfig(GlobalConfig config) {

        File file = new File(Properties.appDataDir + Properties.globalConfigFile);

        if(file.exists()) {
            file.delete();
        }

        try {

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(config);
            oos.close();

        } catch (Exception e) {

            e.printStackTrace();

        }



        return null;

    }


}
