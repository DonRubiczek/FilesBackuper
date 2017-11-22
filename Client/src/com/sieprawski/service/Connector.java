package com.sieprawski.service;

import com.sieprawski.infrastructure.AppData;
import com.sieprawski.infrastructure.GlobalConfig;
import com.sieprawski.models.User;

import javax.swing.*;
import java.net.Socket;

public class Connector {

    private Socket socket;
    private boolean isConnected = false;

    public Connector(String host, int port) {

        try {

            this.socket = new Socket(host, port);
            this.isConnected = socket.isConnected();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(

                    null,
                    "Please start the server first and then relaunch the application.",
                    "Server is not running",
                    JOptionPane.WARNING_MESSAGE);

            System.exit(0);

        }

    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public static boolean isServerOnline(String login, String name, int serverPort, String serverHost) {
        try {
            GlobalConfig config = AppData.readGlobalConfig();
            if(config.getHost().equals(serverHost) && config.getPort() == serverPort ) {

                Connector connector = new Connector(config.getHost(), config.getPort());
                boolean result = connector.isConnected;
                if(result) {

                    User user = new User(login, name);
                    user.saveUser();
                    boolean authenticateResult = ServerHandler.addOrGetUserFromBase(connector.getSocket(), user);
                }
                return result;
            } else {
                JOptionPane.showMessageDialog(

                        null,
                        "Please start the server first and then relaunch the application.",
                        "Server is not running",
                        JOptionPane.WARNING_MESSAGE);

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
