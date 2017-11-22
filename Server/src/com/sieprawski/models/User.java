package com.sieprawski.models;

import com.sieprawski.service.UsersManager;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = 1362649688772597672L;
    private String login;
    private String name;

    public User(String login, String name){
        this.login = login;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }


    public List<File> getFiles() {

        List<File> files = new ArrayList<>();

        return getFiles(UsersManager.getUserDirectory(this), files);

    }

    private List<File> getFiles(File folder, List<File> files) {

        File[] listOfFiles = folder.listFiles();

        for(File f : listOfFiles) {

            if (f.isFile()) {

                files.add(f);

            } else if (f.isDirectory()) {

                getFiles(f, files);

            }

        }

        return files;

    }

    public List<FilesModel> getServerFiles() {

        List<FilesModel> list = new ArrayList<>();

        String userDir = UsersManager.getUserDirectory(this).getAbsolutePath();

        for(File f : this.getFiles()) {

            String pathOnServer = f.getAbsolutePath();

            String pathOnClient = new File(userDir).toURI().relativize(new File(pathOnServer).toURI()).getPath();
            pathOnClient = pathOnClient.substring(0, 1) + ":" + pathOnClient.substring(1, pathOnClient.length());
            pathOnClient = pathOnClient.replace("/", "\\");

            String hash = "";
            try {
                FileInputStream fis = new FileInputStream(f);
                hash = DigestUtils.md5Hex(fis);
                fis.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            list.add(new FilesModel(pathOnServer, pathOnClient, hash));

        }


        return list;

    }


}
