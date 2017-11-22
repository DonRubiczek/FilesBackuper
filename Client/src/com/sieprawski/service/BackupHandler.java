package com.sieprawski.service;

import com.sieprawski.application.ApplicationWindow;
import com.sieprawski.infrastructure.ClientMessage;
import com.sieprawski.infrastructure.Properties;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackupHandler implements Runnable {

        private List<File> files;
        private Map<File, String> filesOnServer;
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;
        private ApplicationWindow applicationWindow;

        public BackupHandler(List<File> files, Map<File, String> filesOnServer, BufferedReader in, PrintWriter out, Socket socket,
                             ApplicationWindow applicationWindow) {
            this.files = files;
            this.filesOnServer = filesOnServer;
            this.in = in;
            this.out = out;
            this.socket = socket;
            this.applicationWindow = applicationWindow;
        }

        public void run() {

		    // PRINTS ALL FILES ON SERVER WITH HASHES
		    for (Map.Entry<File, String> entry : this.filesOnServer.entrySet()) {

			System.out.println(entry.getKey() + " : " + entry.getValue());

		    }

            // GET ONLY FILES OUT OF THE LIST OF FILES AND(!) DIRECTORIES
            List<File> tmpFiles = new ArrayList<>();

            for (File file : files) {

                if (file.isDirectory()) {
                    tmpFiles.addAll(getFiles(file));
                } else {
                    tmpFiles.add(file);
                }

            }

            // CHECK WHICH OF THE FILES TO BACKUP DO NOT HAVE UP-TO-DATE COPIES ON SERVER
            List<File> filesToBackup = new ArrayList<>();
            try {
                for(File file : tmpFiles) {

                    if (filesOnServer.containsKey(file)) {
                        if(md5Hash(file).equals(filesOnServer.get(file))) {
                            continue;
                        }
                    }

                    filesToBackup.add(file);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            out.println(ClientMessage.BACKUP_FILES.name());

            double filesAmount = filesToBackup.size();
            double filesSent = 0;
            for (File file : filesToBackup) {
                try {

                    out.println(ClientMessage.BACKUP_FILES.name());

                    in.readLine(); // SEND_ME_FILEPATH
                    String filepath = file.getAbsolutePath();
                    out.println(filepath); // PATH

                    in.readLine(); // SEND_ME_FILESIZE
                    long fileSize = file.length();
                    out.println(fileSize); // SIZE

                    in.readLine(); // SEND_ME_FILE

                    int numberOfBytes = 0;
                    byte[] buffer = new byte[Properties.bufferSize];
                    DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
                    FileInputStream fis = new FileInputStream(file);

                    double wholeFileSize = fileSize;
                    double alreadySentSize = 0;
                    while (fileSize > 0 && (numberOfBytes = fis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {

                        dataOut.write(buffer, 0, numberOfBytes);
                        fileSize -= numberOfBytes;
                        alreadySentSize += numberOfBytes;
                        //controller.setProgressBarFileValue(alreadySentSize/wholeFileSize);

                    }

                    System.out.println(in.readLine());

                    fis.close();
                    fis = new FileInputStream(file);
                    //controller.setStateLabelCalculatingHashClient();
                    // think about that: http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
                    String hash = DigestUtils.md5Hex(fis);
                    out.println(hash);

                    fis.close();


                    applicationWindow.getServerComboBox().addItem(file);
                    DefaultListModel defaultListModel = applicationWindow.getServerFilesListModel();
                    defaultListModel.addElement(file);
                    applicationWindow.getServerFiles().setModel(defaultListModel);


                } catch (Exception e) {

                    e.printStackTrace();

                }

                filesSent++;
                //controller.setProgressBarWholeValue(filesSent/filesAmount);

            }

            out.println(ClientMessage.BACKUP_FINISHED.name());
            out.println(ClientMessage.EXIT.name());

            //controller.showMessage("Backup finished", "", "Backup finished succesfully");
            //controller.restartStateBox();
            applicationWindow.populateRemoteFilesList();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime time = LocalDateTime.now();
            //LocalDateTime.of(LocalDate.now(), LocalTime.now());
            String formattedDateTime = time.format(formatter);
            //controller.setStateLabel("Last backup: " + formattedDateTime);

        }

        private List<File> getFiles(File file) {

            List<File> files = new ArrayList<>();

            return getFiles(file, files);

        }

        private List<File> getFiles(File folder, List<File> files) {

            File[] listOfFiles = folder.listFiles();

            for (File f : listOfFiles) {

                if (f.isFile()) {

                    files.add(f);

                } else if (f.isDirectory()) {

                    getFiles(f, files);

                }

            }

            return files;

        }

        private String md5Hash(File file) throws Exception {

            FileInputStream fis = new FileInputStream(file);
            String hash = DigestUtils.md5Hex(fis);
            fis.close();
            return hash;


        }

//	private boolean saveListOfFilesBeingBackuped(List<File> list) {
//		try {
//
//			File file = new File(Properties.appDataDir + Properties.listOfFilesBeingBackuped);
//			FileOutputStream fos = new FileOutputStream(file);
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//			oos.writeObject(list);
//			oos.close();
//
//			return true;
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			return false;
//
//		}
//
//
//	}


}
