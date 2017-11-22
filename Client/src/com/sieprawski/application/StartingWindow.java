package com.sieprawski.application;

import com.sieprawski.infrastructure.AppData;
import com.sieprawski.models.User;
import com.sieprawski.service.Connector;
import com.sieprawski.service.ServerHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartingWindow {

    JFrame frame = new JFrame("StartingWindow");
    private JPanel startPanel;
    private JPanel southButtonPanel;
    private JPanel westLabelPanel;
    private JPanel eastTextFieldPanel;
    private JLabel loginLabel;
    private JLabel serverPortLabel;
    private JLabel nameLabel;
    private JLabel serverHostLabel;
    private JTextField loginTextField;
    private JTextField serverPortTextField;
    private JTextField nameTextField;
    private JTextField serverHostTextField;
    private JButton loginButton;
    private JButton nameButton;
    private JButton serverHostButton;
    private JButton serverPortButton;
    private JButton connectButton;
    private JButton clearDataButton;
    private JButton instructionButton;
    private JButton exitButton;

    private String login, name, serverHost;
    private int serverPort;

    public StartingWindow() {

        createAndShowFrame();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String localLogin = loginTextField.getText();
                    if(localLogin.equals("")) {
                        throw new Exception();
                    }
                    login = localLogin;
                    JOptionPane.showMessageDialog(createMessageDialog(), "You sucessfully added login." );
                }catch(Exception exception) {
                    JOptionPane.showMessageDialog(createMessageDialog(), "Field with login cannot be empty." );
                }
            }
        });

        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String localName = nameTextField.getText();
                    if(localName.equals("")) {
                        throw new Exception();
                    }
                    name = localName;
                    JOptionPane.showMessageDialog(createMessageDialog(), "You sucessfully added name." );
                }catch(Exception exception) {
                    JOptionPane.showMessageDialog(createMessageDialog(), "Field with name cannot be empty." );
                }
            }
        });

        serverHostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String serverHostLocal = serverHostTextField.getText();
                    if(serverHostLocal.equals("")) {
                        throw new Exception();
                    }
                    serverHost = serverHostLocal;
                    JOptionPane.showMessageDialog(createMessageDialog(), "You sucessfully added server Host." );
                }catch(Exception exception) {
                    JOptionPane.showMessageDialog(createMessageDialog(), "Field with server Host cannot be empty." );
                }

            }
        });

        serverPortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int parsedPortLocal = 0;
                    parsedPortLocal = Integer.parseInt(serverPortTextField.getText());
                    if(parsedPortLocal == 0) {
                        throw new Exception();
                    }
                    serverPort = parsedPortLocal;
                    JOptionPane.showMessageDialog(createMessageDialog(), "You sucessfully added server Port." );
                }catch(Exception exception) {
                    JOptionPane.showMessageDialog(createMessageDialog(), "Field with server Port cannot be empty." );
                }
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(login.equals(null) || name.equals(null) || serverPort == 0 || serverHost.equals(null)) {
                        throw new NullPointerException();
                    }else {
                        if(AppData.isConfigured()){

                            if (Connector.isServerOnline(login, name, serverPort, serverHost)) {

                                frame.dispose();
                                ApplicationWindow applicationWindow = new ApplicationWindow();


                            } else {
                                JOptionPane.showMessageDialog(

                                        null,
                                        "Please start the server first and then relaunch the application or add correct port and host.",
                                        "Uncorrectly added data.",
                                        JOptionPane.WARNING_MESSAGE);

                            }
                        }

                    }
                }catch(NullPointerException exception) {
                    JOptionPane.showMessageDialog(createMessageDialog(), "Data wasn't added correctly, please try again." );
                }
            }
        });

        clearDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginTextField.setText(null);
                nameTextField.setText(null);
                serverHostTextField.setText(null);
                serverPortTextField.setText(null);
                login = null;
                name = null;
                serverHost = null;
                serverPort = 0;
            }
        });

        instructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void createAndShowFrame() {
        frame.setContentPane(startPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,300);
        frame.setVisible(true);
    }

    private Frame createMessageDialog() {
        JFrame messageDialog = new JFrame("MessageFrame");
        messageDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return messageDialog;
    }

}
