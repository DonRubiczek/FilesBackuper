package com.sieprawski.application;

import com.sieprawski.infrastructure.AppData;
import com.sieprawski.service.Connector;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartingWindow {

    private JPanel startPanel;
    private JButton startButton;
    private JButton exitButton;
    private JFrame frame = new JFrame("StartingWindow");

    public StartingWindow() {

        createAndShowFrame();

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AppData.initialize();

                    Thread connector = new Thread(new Connector());
                    connector.start();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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


}
