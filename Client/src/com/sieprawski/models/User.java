package com.sieprawski.models;

import com.sieprawski.infrastructure.AppData;
import com.sieprawski.infrastructure.Properties;

import java.io.*;

public class User implements Serializable{

    private static final long serialVersionUID = -2437875523288736761L;
    private String login;
    private String name;

    public User(String login, String name) {

        this.login = login;
        this.name = name;

    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public boolean saveUser() {

        if(!AppData.appDirExists()) {

            AppData.createAppDir();

        }

        String userFilePath = Properties.appDataDir + Properties.userFile;
        try {

            FileOutputStream fout = new FileOutputStream(userFilePath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
            fout.close();

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }


        return true;

    }

    public static User readUser() throws Exception {

        String userFilePath = Properties.appDataDir + Properties.userFile;

        FileInputStream fout = new FileInputStream(userFilePath);
        ObjectInputStream oos = new ObjectInputStream(fout);
        User user = (User)oos.readObject();
        oos.close();
        fout.close();

        return user;


    }

    public static boolean isUserRegistered() {

        String path = Properties.appDataDir + Properties.userFile;
        //System.out.println(path);
        File file = new File(path);
        if (file.exists()) 	return true;
        else 				return false;

    }

}
