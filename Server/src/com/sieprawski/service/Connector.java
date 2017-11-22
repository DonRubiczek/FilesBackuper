package com.sieprawski.service;

import com.sieprawski.infrastructure.AppConfigDynamic;

import java.net.ServerSocket;
import java.net.Socket;

public class Connector implements Runnable {

    @Override
    public void run() {

        try {

            int port = AppConfigDynamic
                    .getInstance()
                    .getAppConfig()
                    .getPortNumber();

            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Main is listening on port: " + port);

            while(true) {

                Socket socket = serverSocket.accept();
                System.out.println("Connection with client estabilished");

                if (socket.isConnected()){
                    ClientHandler client = new ClientHandler(socket);

                    if(client.isAuthenticated()) {

                        Thread thread = new Thread(client);
                        System.out.println("twoja stara");
                        thread.start();

                        System.out.println("User has just been authenticated");

                    } else {

                        socket.close();

                    }
                }



            }


        } catch(Exception e) {

            e.printStackTrace();

        }
    }

}
