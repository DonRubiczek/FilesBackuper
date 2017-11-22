package com.sieprawski.service;

import com.sieprawski.application.ApplicationWindow;
import com.sieprawski.infrastructure.ClientMessage;
import com.sieprawski.models.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHandler {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User user;

    public ServerHandler(Socket socket, User user) {

        this.socket = socket;
        this.user = user;

        try {

            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    public void removeAllFilesFromServer() {
        out.println(ClientMessage.REMOVE_ALL_FILES.name());
    }

    public void removeSelectedFileFromServer(File file) {
        out.println(ClientMessage.REMOVE_SELECTED_FILE.name());
        out.println(file.getAbsolutePath());
    }

    public boolean checkIfServerIsReady() {

        try {

            in.readLine(); // GIVE_ME_USER_LOGIN
            out.println(this.user.getLogin());
            in.readLine(); // GIVE_ME_USER_PASSWORD
            out.println(this.user.getName());

            if(in.readLine().equals("WAITING_FOR_COMMANDS")) {

                return true;

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        return false;

    }

    public void disconnectUser() {
        try {
            out.println(ClientMessage.EXIT.name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    public void restore(List<File> files, AppController controller) {
//        RestoreHandler restoreHandler = new RestoreHandler(socket, controller, files);
//        Thread thread = new Thread(restoreHandler);
//        thread.start();
//    }
//
    public void backup(List<File> files, ApplicationWindow applicationWindow) {

        Map<File, String> filesOnServer = getServerFilesWithHashes();

        backup(files, filesOnServer, in, out, socket, applicationWindow);


    }

    private void backup(List<File> files, Map<File, String> filesOnServer, BufferedReader in, PrintWriter out, Socket socket, ApplicationWindow applicationWindow) {

        BackupHandler handler = new BackupHandler(files, filesOnServer, in, out, socket, applicationWindow);
        Thread thread = new Thread(handler);
        thread.start();

    }

    public Map<File, String> getServerFilesWithHashes() {

        Map<File, String> filesWithHashes = new HashMap<>();

        try {

            out.println(ClientMessage.LIST_MY_FILES.name());

            while(!in.readLine().equals("SENDING_FILELIST_FINISHED")) { // SENDING_FILELIST

                in.readLine(); // SENDING FILEPATH
                String filepath = in.readLine();
                in.readLine(); // SENDING FILEHASH
                String hash = in.readLine();

                File file = new File(filepath);

                filesWithHashes.put(file, hash);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return filesWithHashes;

    }

    public List<File> getServerFiles() {

        List<File> files = new ArrayList<>();

        try {

            out.println(ClientMessage.LIST_MY_FILES_WITHOUT_HASHES.name());

            while(!in.readLine().equals("SENDING_FILELIST_FINISHED")) { // SENDING_FILELIST

                in.readLine(); // SENDING FILEPATH
                String filepath = in.readLine();

                File file = new File(filepath);

                files.add(file);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return files;

    }

    public static boolean addOrGetUserFromBase(Socket socket, User user) {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            in.readLine(); // login
            out.println(user.getLogin());
            in.readLine(); // name
            out.println(user.getName());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
