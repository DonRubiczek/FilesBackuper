package com.sieprawski.service;

import com.sieprawski.infrastructure.ClientMessage;
import com.sieprawski.infrastructure.Properties;
import com.sieprawski.infrastructure.ServerMessage;
import com.sieprawski.models.FilesModel;
import com.sieprawski.models.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class ClientHandler implements Runnable {

    private static int clientsCounter = 0;

    private Socket socket;

    private User user;
    private ClientMessage clientMessage;
    private BufferedReader in;
    private PrintWriter out;
    private boolean authenticated = false;

    public ClientHandler(Socket socket) {

        this.socket = socket;

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {

            e.printStackTrace();

        }

        this.authenticated = authenticateUser();

    }

    private boolean authenticateUser() {

        boolean result = false;

        try {

            sendMessage(ServerMessage.SEND_ME_USER_LOGIN.name());
            String login = receiveMessage();
            sendMessage(ServerMessage.SEND_ME_USER_NAME.name());
            String name = receiveMessage();

            result = UsersManager.checkIfUserExistsOrAdd(login, name);
            if (result) {

                this.user = new User(login, name);
                clientsCounter++;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }

    public boolean isAuthenticated() {

        return authenticated;

    }

    @Override
    public void run() {

        clientMessage = ClientMessage.CONNECTED;
        setServerIdle();

        while (!clientMessage.equals(ClientMessage.EXIT)) {

            clientMessage = getClientMessage();

            switch (clientMessage) {

                case LIST_MY_FILES:

                    sendServerFilesListWithHashes();
                    break;

                case LIST_MY_FILES_WITHOUT_HASHES:

                    sendServerFilesListWithoutHashes();
                    break;

                case BACKUP_FILES:

                    backupFiles();
                    break;

                case RECOVER_FILES:

                    recoverFiles();
                    break;

                case REMOVE_ALL_FILES:

                    for (File file : this.user.getFiles()) {

                        removeFile(file);

                    }

                    break;

                case REMOVE_SELECTED_FILE:

                    try {

                        String clientPath = receiveMessage();
                        clientPath = clientPath.replace(":", "");
                        String serverPath = UsersManager.getUserDirectory(this.user).getAbsolutePath() + "\\" + clientPath;
                        File file = new File(serverPath);

                        removeFile(file);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    break;

                case NOT_RECOGNIZED:

                    System.out.println(" << Command not recognized. >> ");
                    clientMessage = ClientMessage.EXIT;
                    break;

                default:

                    break;

            }

            setServerIdle();

        }



        this.authenticated = false;
        this.clientMessage = null;
        this.user = null;
        try {

            this.in.close();
            this.out.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
        clientsCounter--;
        System.out.println(" << Clients connected: " + clientsCounter + " >> ");

    }

    private void setServerIdle() {

        try {

            sendMessage(ServerMessage.WAITING_FOR_COMMANDS.name());

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private ClientMessage getClientMessage() {

        ClientMessage clientMessage = ClientMessage.NOT_RECOGNIZED;

        try {

            String message = receiveMessage();

            if (ClientMessage.contains(message)) {

                clientMessage = ClientMessage.valueOf(message);

            } else {

                // not recognized

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return clientMessage;
    }

    private String receiveMessage() throws Exception {

        String message = this.in.readLine();
        System.out.println("client > " + message);

        return message;

    }

    private void sendServerFilesListWithHashes() {

        List<FilesModel> serverFiles = this.user.getServerFiles();
        for (FilesModel sf : serverFiles) {

            try {

                sendMessage(ServerMessage.SENDING_FILELIST.name());
                sendMessage(ServerMessage.SENDING_FILEPATH.name());
                sendMessage(sf.getClientPath());
                sendMessage(ServerMessage.SENDING_FILE_HASH.name());
                sendMessage(sf.getHash());

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        sendMessage(ServerMessage.SENDING_FILELIST_FINISHED.name());

    }

    private void sendServerFilesListWithoutHashes() {

        List<FilesModel> serverFiles = this.user.getServerFiles();
        for (FilesModel sf : serverFiles) {

            try {

                sendMessage(ServerMessage.SENDING_FILELIST.name());
                sendMessage(ServerMessage.SENDING_FILEPATH.name());
                sendMessage(sf.getClientPath());

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        sendMessage(ServerMessage.SENDING_FILELIST_FINISHED.name());

    }

    private void backupFiles() {

        String clientPath = "";
        String serverPath = "";
        String hashOnServer = "";
        String hashOnClient = "";

        try {

            while (!receiveMessage().equals(ClientMessage.BACKUP_FINISHED.name())) {

                sendMessage(ServerMessage.SEND_ME_FILEPATH.name());
                clientPath = receiveMessage();
                clientPath = clientPath.replace(":", "");
                serverPath = UsersManager.getUserDirectory(this.user).getAbsolutePath() + "\\" + clientPath;
                File file = new File(serverPath);
                if (file.exists()) {

                    file.delete();
                    file.getParentFile().mkdirs(); // creates whole directory
                    // structure
                    file.createNewFile(); // creates the file

                } else {

                    file.getParentFile().mkdirs();
                    file.createNewFile();

                }

                sendMessage(ServerMessage.SEND_ME_FILESIZE.name());
                long fileSize = Long.parseLong(receiveMessage()); // file size
                // in bytes

                sendMessage(ServerMessage.SEND_ME_FILE.name());
                int numberOfBytes = 0;
                byte[] buffer = new byte[Properties.bufferSize];

                // http://stackoverflow.com/questions/10367698/java-multiple-file-transfer-over-socket
                DataInputStream dataIn = new DataInputStream(this.socket.getInputStream());
                FileOutputStream dataOut = new FileOutputStream(file);

                long wholeFileSize = fileSize;
                long receivedFileSize = numberOfBytes;
                System.out.println(" << File transfer started >> ");

                while (fileSize > 0
                        && (numberOfBytes = dataIn.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) > 0) {

                    dataOut.write(buffer, 0, numberOfBytes);
                    fileSize -= numberOfBytes;

                    receivedFileSize += numberOfBytes;
                    System.out.println(
                            (String.format("Main backuped: %.2f", 100 * ((float) receivedFileSize / (float) wholeFileSize))) + "%");

                }

                System.out.println(" << File transfer finished >> ");
                dataOut.close();

                sendMessage(ServerMessage.SEND_ME_HASH.name());
                hashOnClient = receiveMessage();
                FileInputStream fis = new FileInputStream(file);
                hashOnServer = DigestUtils.md5Hex(fis);
                System.out.println("server > " + hashOnServer);

                if (hashOnServer.equals(hashOnClient)) {

                    System.out.println(" << File sent succesfully >> ");

                } else {

                    System.out.println(" << File was corrupted during transfer >> ");

                }

                fis.close();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void recoverFiles() {

        try {

            while (!receiveMessage().equals("RECOVER_FILES_FINISHED")) {

                sendMessage(ServerMessage.SEND_ME_FILEPATH.name());
                String path = receiveMessage();
                path = path.replace(":", "");
                String serverPath = UsersManager.getUserDirectory(this.user).getAbsolutePath() + "\\" + path;
                System.out.println(serverPath);
                File file = new File(serverPath);
                long fileSize = file.length();
                sendMessage("SENDING_REQUESTED_FILE_SIZE");
                sendMessage("" + fileSize);
                sendMessage("SENDING_FILE");
                receiveMessage(); // OK

                int numberOfBytes = 0;
                byte[] buffer = new byte[Properties.bufferSize];
                FileInputStream fis = new FileInputStream(file);
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

                double wholeFileSize = fileSize;
                double alreadySentSize = 0;
                while (fileSize > 0
                        && (numberOfBytes = fis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {

                    System.out.println("Writing " + numberOfBytes + " bytes...");
                    dataOut.write(buffer, 0, numberOfBytes);
                    System.out.println("Done...");
                    fileSize -= numberOfBytes;

                    alreadySentSize += numberOfBytes;
                    System.out.println(
                            (String.format("Main restored: %.2f", 100 * ((float) alreadySentSize / (float) wholeFileSize))) + "%");


                }

                fis.close();

                System.out.println("Main restored finished");


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void removeFile(File file) {
        if (file.exists()) {
            file.delete();

            File dir = file.getParentFile();
            if (dir.isDirectory() && dir.listFiles().length == 0) {
                dir.delete();
            }
        }
    }

    public static int getClientsCounter() {
        return clientsCounter;
    }

    private void sendMessage(String message) {

        this.out.println(message);
        System.out.println("server > " + message);

    }


}
