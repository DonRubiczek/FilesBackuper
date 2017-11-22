package com.sieprawski.infrastructure;

import javax.swing.*;
import java.io.*;

public class AppData {

    public static boolean AppDirExists() {

        File f = new File(Properties.appDataDir);

        boolean result = f.exists();
        System.out.println("App directory exists: " + result);
        return result;

    }

    public static boolean CreateAppDir() {

        File f = new File(Properties.appDataDir);

        boolean result = f.mkdir();
        System.out.println("App directory creation result: " + result);
        return result;

    }

    public static void initialize() {

        if (!AppDirExists()) {

            CreateAppDir();

        }

        AppConfigDynamic.getInstance().setAppConfig(readGlobalConfigFile());
    }

    public static AppConfig readGlobalConfigFile() {

        AppConfig appConfig = new AppConfig();

        File f = new File(Properties.appDataDir + Properties.globalConfigFile);
        if (!f.exists()) {

            // Swing system style
            try {

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            } catch (Exception e) {

                e.printStackTrace();

            }

            JOptionPane.showMessageDialog(null, "Please select backup archive location.");

            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setDialogTitle("backup archive location");
            fc.showOpenDialog(null);


            String backupDir = "";
            if (fc.getSelectedFile() != null) {

                backupDir = fc.getSelectedFile().getAbsolutePath();
                System.out.println(backupDir.toString());

            } else {

                System.out.println("No directory was selected, closing the app.");
                System.exit(0);

            }

            appConfig.setBackupDirPath(backupDir);

            JOptionPane.showMessageDialog(null, "Please select port number.");

            boolean check;
            int port = 0;
            do {

                check = true;

                try {

                    port = Integer.parseInt(JOptionPane.showInputDialog("Port number: ", 9999));

                } catch (Exception e) {

                    e.printStackTrace();
                    check = false;

                }

            } while(!check);

            appConfig.setPortNumber(port);

            try {

                FileOutputStream fout = new FileOutputStream(Properties.appDataDir + Properties.globalConfigFile);
                ObjectOutputStream os = new ObjectOutputStream(fout);
                os.writeObject(appConfig);
                os.close();
                fout.close();
                System.out.println("Global config file location: " + Properties.appDataDir + Properties.globalConfigFile);

            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }

        } else {

            try {

                FileInputStream fin = new FileInputStream(Properties.appDataDir + Properties.globalConfigFile);
                ObjectInputStream is = new ObjectInputStream(fin);
                appConfig = (AppConfig)is.readObject();
                fin.close();
                is.close();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        return appConfig;
    }

    public static String getUsersFileLocation() {

        System.out.println("Users file location: " + Properties.appDataDir + Properties.usersFile);
        return Properties.appDataDir + Properties.usersFile;

    }
}
