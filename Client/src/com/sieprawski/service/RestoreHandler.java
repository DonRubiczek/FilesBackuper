package com.sieprawski.service;

import com.sieprawski.infrastructure.ClientMessage;
import com.sieprawski.infrastructure.Properties;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class RestoreHandler {

//    public class RestoreHandler implements Runnable{
//
//        private BufferedReader in;
//        private PrintWriter out;
//        private Socket socket;
//        private AppController controller;
//        private List<File> filesToRecover;
//
//
//
//        public RestoreHandler(Socket socket, AppController controller, List<File> files) {
//
//            this.socket = socket;
//            this.controller = controller;
//            this.filesToRecover = files;
//
//            try {
//                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
//                this.out = new PrintWriter(this.socket.getOutputStream(), true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//
//        @Override
//        public void run() {
//
//            try {
//
//                out.println(ClientMessage.RECOVER_FILES.name());
//
//
//                double filesAmount = filesToRecover.size();
//                double filesSent = 0;
//                for(File file : filesToRecover) {
//
//                    controller.setStateLabelRecoveringFile(file);
//
//                    out.println(ClientMessage.RECOVER_FILES.name());
//                    in.readLine(); // GIVE_ME_FILENAME
//                    out.println(file.getAbsolutePath());
//                    in.readLine(); // SENDING_FILESIZE_FROM_SERVER
//                    long fileSize = Long.parseLong(in.readLine()); // filesize
//                    in.readLine(); // SENDING FILE
//                    out.println("OK");
//
//                    int numberOfBytes = 0;
//                    byte[] buffer = new byte[Properties.bufferSize];
//
//
//                    float wholeFileSize = fileSize;
//                    float receivedFileSize = numberOfBytes;
//                    System.out.println(" << File transfer started >> ");
//
//                    DataInputStream dataIn = new DataInputStream(this.socket.getInputStream());
//                    FileOutputStream dataOut = new FileOutputStream(file);
//
////				while (fileSize > 0
////						&& (numberOfBytes = dataIn.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) > 0) {
////
////					System.out.println("Writing " + numberOfBytes + " bytes...");
////					dataOut.write(buffer, 0, numberOfBytes);
////					fileSize -= numberOfBytes;
////					receivedFileSize += numberOfBytes;
////					System.out.println("Done... " + (wholeFileSize - receivedFileSize) + " left");
////
////					controller.setProgressBarFileValue(receivedFileSize/wholeFileSize);
////
////					System.out.println(String.format("Client restored: %.2f", 100 * (receivedFileSize/wholeFileSize)) + "%");
////
////
////				}
//                    while (fileSize > 0) {
//                        try {
//                            this.socket.setSoTimeout(0);
//                            if ((numberOfBytes = dataIn.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) > 0) {
//
//                                System.out.println("Writing " + numberOfBytes + " bytes...");
//                                dataOut.write(buffer, 0, numberOfBytes);
//                                fileSize -= numberOfBytes;
//                                receivedFileSize += numberOfBytes;
//                                System.out.println("Done... " + (wholeFileSize - receivedFileSize) + " left");
//
//                                controller.setProgressBarFileValue(receivedFileSize/wholeFileSize);
//
//                                System.out.println(String.format("Client restored: %.2f", 100 * (receivedFileSize/wholeFileSize)) + "%");
//
//
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    this.socket.setSoTimeout(0);
//
//                    System.out.println(" << File transfer finished >> ");
//                    dataOut.close();
//
//                    filesSent++;
//                    controller.setProgressBarWholeValue(filesSent/filesAmount);
//
//
//                }
//
//                out.println(ClientMessage.RECOVER_FILES_FINISHED.name());
//                out.println(ClientMessage.EXIT.name());
//
//                controller.restartStateBox();
//                if(filesToRecover.size() == 1) {
//                    controller.showMessage("Restore finished", "", "Selected file restored succesfully");
//                } else if (filesToRecover.size() == 0) {
//                    controller.showMessage("Restore finished", "", "All files are up to date");
//                } else {
//                    controller.showMessage("Restore finished", "", "All files restored succesfully");
//                }
//
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//
//            }
//
//
//
//        }
//
//
//
//    }
}
