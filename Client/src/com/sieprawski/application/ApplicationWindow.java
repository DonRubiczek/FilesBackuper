package com.sieprawski.application;

import com.sieprawski.infrastructure.AppData;
import com.sieprawski.infrastructure.GlobalConfig;
import com.sieprawski.infrastructure.Properties;
import com.sieprawski.models.User;
import com.sieprawski.service.Connector;
import com.sieprawski.service.ServerHandler;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class ApplicationWindow {

    JFrame frame = new JFrame("Application Window");
    ApplicationWindow applicationWindow = this;
    private JPanel panel1;
    private JProgressBar wholeProgressBar;
    private JList serverFiles;
    private JList backupingFiles;
    private JButton removePositionButton;
    private JButton addFileButton;
    private JButton removeAllButton;
    private JButton addDirectoryButton;
    private JButton restoreAllButton;
    private JButton removeAllServerButton;
    private JButton restorePositionButton;
    private JButton removePositionServerButton;
    private JLabel backupingFilesListMethods;
    private JLabel serverFilesListMethods;
    private JButton BACKUPButton;
    private JComboBox localComboBox;
    private JComboBox serverComboBox;
    private DefaultListModel serverFilesListModel;
    private DefaultListModel backupingFilesListModel;



    public ApplicationWindow() {

        createAndShowFrame();
        serverFilesListModel = new DefaultListModel();
        backupingFilesListModel = new DefaultListModel();

        addFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileChooser fileChooser = new FileChooser();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        File file = fileChooser.showOpenDialog(null);
                        if (file != null) {
                            localComboBox.addItem(file);
                            backupingFilesListModel.addElement(file);
                            backupingFiles.setModel(backupingFilesListModel);
                        }

                        saveUserFileList();
                    }
                });

            }
        });

        addDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        File directory = directoryChooser.showDialog(null);
                        if (directory != null) {
                            localComboBox.addItem(directory);
                            backupingFilesListModel.addElement(directory);
                            backupingFiles.setModel(backupingFilesListModel);
                        }

                        saveUserFileList();
                    }
                });
            }
        });

        removePositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localComboBox.removeItemAt(localComboBox.getSelectedIndex());
                backupingFilesListModel.removeElementAt(backupingFiles.getSelectedIndex());
                backupingFiles.setModel(backupingFilesListModel);

                saveUserFileList();
            }
        });

        removeAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localComboBox.removeAllItems();
                backupingFilesListModel.removeAllElements();
                backupingFiles.setModel(backupingFilesListModel);

                saveUserFileList();
            }
        });

//        restoreAllButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                List<File> files = serverFiles.getModel();
//                files = restoreRemoveAllUpToDateFiles(files);
//
//                try {
//
//                    ServerHandler serverHandler = getServerHandler();
//                    serverHandler.authenticateUser();
//                    serverHandler.restore(files, this);
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//
//                }
//            }
//        });

//        restorePositionButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                File file = remoteList.getSelectionModel().getSelectedItem();
//                List<File> files = new ArrayList<>();
//                files.add(file);
//                files = restoreRemoveAllUpToDateFiles(files);
//
//                try {
//
//                    ServerHandler serverHandler = getServerHandler();
//                    serverHandler.authenticateUser();
//                    serverHandler.restore(files, this);
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//
//                }
//
//            }
//        });

        removeAllServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Remove all files from server");
                        alert.setHeaderText("Proceeding with this action will remove all backup files from the server");
                        alert.setContentText("Are you sure you want to do this?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            try {
                                ServerHandler serverHandler = getServerHandler();
                                serverHandler.checkIfServerIsReady();
                                serverHandler.removeAllFilesFromServer();
                                serverHandler.disconnectUser();

                            } catch (Exception en) {
                                en.printStackTrace();
                            }
                            serverComboBox.removeAllItems();
                            serverFilesListModel.removeAllElements();
                            serverFiles.setModel(serverFilesListModel);
                        } else {

                        }

                        restartStateBox();
                    }
                });
            }
        });

        removePositionServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Remove file from server");
                        alert.setHeaderText("Proceeding with this action will remove selected file from the server");
                        alert.setContentText("Are you sure you want to do this?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {

                            File file = (File)serverFilesListModel.getElementAt(backupingFiles.getSelectedIndex());
                            //setStateLabelRemovingFile(file);

                            if (file != null) {
                                try {

                                    ServerHandler serverHandler = getServerHandler();
                                    serverHandler.checkIfServerIsReady();
                                    serverHandler.removeSelectedFileFromServer(file);
                                    //serverHandler.disconnectUser();

                                } catch (Exception es) {
                                    es.printStackTrace();
                                }
                                populateRemoteFilesList();
                            }
                        }

                        restartStateBox();

                    }
                });
            }
        });

        BACKUPButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //setStateLabel("Backup started...");

                try {

                    List<File> files = new ArrayList<>();
                    for(int currentFile = 0; currentFile < backupingFilesListModel.getSize(); currentFile++) {

                        File file = (File) backupingFilesListModel.get(currentFile);
                        files.add(file);
                    }

                    ServerHandler serverHandler = getServerHandler();
                    serverHandler.checkIfServerIsReady();
                    serverHandler.backup(files, applicationWindow); // populating remote files list
                    //serverHandler.disconnectUser();

                } catch (Exception es) {

                    es.printStackTrace();

                }
            }
        });
    }




    public void populateRemoteFilesList() {

        Platform.runLater(() -> {

            serverComboBox.removeAllItems();;
            serverFilesListModel.removeAllElements();

            try {

                ServerHandler serverHandler = getServerHandler();
                serverHandler.checkIfServerIsReady();
                List<File> files = serverHandler.getServerFiles();
                for(File file : files) {
                    serverFilesListModel.addElement(file);
                }

                serverHandler.disconnectUser();

            } catch (Exception e) {

                e.printStackTrace();

            }

            serverFiles.setModel(serverFilesListModel);
        });

    }

    private ServerHandler getServerHandler() throws Exception {
        User user = User.readUser();
        GlobalConfig config = AppData.readGlobalConfig();
        Connector connector = new Connector(config.getHost(), config.getPort());
        ServerHandler serverHandler = new ServerHandler(connector.getSocket(), user);
        return serverHandler;
    }


//    private void initialize() {
//
//        restartStateBox();
//
//        for (File file : readUserFileList()) {
//            localListItems.add(file);
//        }
//        localList.setItems(localListItems);
//        remoteList.setItems(remoteListItems);
//        populateRemoteFilesList();
//
//        timer = new Timer();
//        setBackupSchedule();
//
//    }


    public JProgressBar getWholeProgressBar() {
        return wholeProgressBar;
    }

    public JComboBox getServerComboBox() {
        return serverComboBox;
    }

    public DefaultListModel getServerFilesListModel() {
        return serverFilesListModel;
    }

    public JList getServerFiles() {
        return serverFiles;
    }

    private void handleAbout() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Author");
        alert.setHeaderText(null);
        alert.setContentText("Created in 2015 as OPA project by Piotr Koca.");
        alert.showAndWait();
    }

    private List<File> readUserFileList() {

        List<File> list = new ArrayList<>();

        try {

            File fileList = new File(Properties.appDataDir + Properties.userFilesList);

            if (!fileList.exists()) {
                fileList.createNewFile();
            }

            FileInputStream fis = new FileInputStream(fileList);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (List<File>) ois.readObject();
            ois.close();

        } catch (Exception e) {

        }
        return list;

    }

    public void restartStateBox() {

        //setStateLabelIdle();
        //setProgressBarFileValue(0);
        //setwholeProgressBarValue(0);

    }

//    public void setwholeProgressBarValue(double percentage) {
//        Platform.runLater(() -> {
//            this.wholeProgressBar.setP(percentage);
//            ;
//        });
//
//    }

    private void saveUserFileList() {
        File fileList = new File(Properties.appDataDir + Properties.userFilesList);

        try {

            FileOutputStream fos = new FileOutputStream(fileList);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            List<File> list = new ArrayList<>();

            for(int currentFile = 0; currentFile < backupingFilesListModel.getSize(); currentFile++) {

                File file = (File)backupingFilesListModel.get(currentFile);
                list.add(file);
            }

            oos.writeObject(list);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private List<File> restoreRemoveAllUpToDateFiles(List<File> filesToRecover) {
//
//        List<File> list = new ArrayList<>();
//
//        try {
//
//            ServerHandler serverHandler = getServerHandler();
//            serverHandler.authenticateUser();
//            Map<File, String> serverFilesWithHashes = serverHandler.getServerFilesWithHashes();
//            serverHandler.disconnectUser();
//
//            for (File file : filesToRecover) {
//
//                boolean shouldBeRecovered = false;
//
//                if (!file.exists()) {
//
//                    shouldBeRecovered = true;
//                    file.getParentFile().mkdirs();
//                    file.createNewFile();
//
//                } else {
//
//                    FileInputStream fis = new FileInputStream(file);
//                    String localHash = DigestUtils.md5Hex(fis);
//                    String remoteHash = serverFilesWithHashes.get(file);
//
//                    fis.close();
//
//                    if (!localHash.equals(remoteHash)) {
//                        shouldBeRecovered = true;
//                    }
//
//                }
//
//                if (shouldBeRecovered) {
//                    list.add(file);
//                }
//
//            }
//
//        } catch (Exception e) {
//
//        }
//
//        return list;
//
//    }

    public void showMessage(String title, String header, String message) {

        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();

        });

    }

    private void createAndShowFrame() {
        frame.setContentPane(panel1);
        final JFXPanel fxPanel = new JFXPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(850, 400);
        frame.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

        private static void initFX(JFXPanel fxPanel) {
            // This method is invoked on the JavaFX thread
            Scene scene = createScene();
            fxPanel.setScene(scene);
        }

        private static Scene createScene() {
            Group root  =  new  Group();
            Scene  scene  =  new  Scene(root, Color.ALICEBLUE);
            Text text  =  new  Text();

            text.setX(40);
            text.setY(100);
            text.setFont(new Font(25));
            text.setText("Welcome JavaFX!");

            root.getChildren().add(text);

            return (scene);
        }
}
