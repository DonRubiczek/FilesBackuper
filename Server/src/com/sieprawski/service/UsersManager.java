package com.sieprawski.service;

import com.sieprawski.infrastructure.AppConfigDynamic;
import com.sieprawski.infrastructure.AppData;
import com.sieprawski.infrastructure.Properties;
import com.sieprawski.models.User;
import com.sieprawski.models.Users;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsersManager {

    public static boolean initiateUsersDatabase() {

        Users users = new Users();

        ArrayList<User> usersDatabase = new ArrayList<User>();
        users.setUsers(usersDatabase);

        return saveUsersDatabase(users);

    }

    public static boolean saveUsersDatabase(Users users) {

        try {

            if(!AppData.AppDirExists()) {

                AppData.CreateAppDir();

            }

            String path = AppData.getUsersFileLocation();

            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(users);
            oos.close();
            fout.close();

            System.out.println(Properties.usersFileWriteSuccess);

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

        return true;
    }

    public static Users readUsersDatabase() {

        Users users = new Users();

        try {

            File file = new File(getUsersFilePath());
            FileInputStream testFin = new FileInputStream(getUsersFilePath());
            int z =testFin.read();
            if(file.exists() && !file.isDirectory() && z != -1) {

                // cool

            } else {

                initiateUsersDatabase();

            }

            FileInputStream fin = new FileInputStream(getUsersFilePath());
            ObjectInputStream ois = new ObjectInputStream(fin);
            users = (Users)ois.readObject();

            ois.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return users;
    }

    public static String getUsersFilePath() {

        return Properties.appDataDir + Properties.usersFile;

    }

    public static boolean checkIfUserExistsOrAdd(String login, String name) {

        boolean result = false;

        List<User> users = readUsersDatabase().getUsers();
        if(users.size() == 0) {
            User user = new User(login, name);
            addUserToDatabase(user);
            checkIfUserExistsOrAdd(login, name);

        } else {
            for (int currentUser = 0; currentUser < users.size(); currentUser++) {

                if(users.get(currentUser).getLogin().equals(login) && users.get(currentUser).getName().equals(name)) {
                    return true;
                }
            }
            if(!result) {
                User user = new User(login, name);
                addUserToDatabase(user);
                checkIfUserExistsOrAdd(login, name);
            }
        }
        return true;
    }

    public static boolean addUserToDatabase(User user) {

        System.out.println("Adding user: " + user.getLogin());
        Users users = readUsersDatabase();

        List<String> logins = getUsersLogins(users.getUsers());

        if (logins.contains(user.getLogin())) {

            System.out.println("User: " + user.getLogin() + " already exists!");
            return false;

        } else {

            users.getUsers().add(user);
            System.out.println("User: " + user.getLogin() + " added successfully!");
            saveUsersDatabase(users);
            createUserDirectory(user);
            return true;
        }
    }

    public static List<String> getUsersLogins(List<User> users) {

        List<String> logins = new ArrayList<>();

        for(User u : users) {

            logins.add(u.getLogin());

        }

        return logins;

    }

    public static void createUserDirectory(User user) {

        File f = new File(AppConfigDynamic
                .getInstance()
                .getAppConfig()
                .getBackupDirPath()
                + "\\" + user.getLogin() );
        System.out.println(f.getAbsolutePath());

        if (!f.exists()) {
            boolean result = false;
            result = f.mkdirs();
            System.out.println(result);
            System.out.println("User directory has just been created.");
        } else {
            //System.out.println("User directory already exists.");
        }

    }

    public static File getUserDirectory(User user) {
        File f = new File(AppConfigDynamic
                .getInstance()
                .getAppConfig()
                .getBackupDirPath()
                + "\\" + user.getLogin() );
        System.out.println(f.getAbsolutePath());

        if (!f.exists()) {
            f.mkdir();
            System.out.println("User directory has just been created.");
        } else {
            //System.out.println("User directory already exists.");
        }

        return f;
    }
}
