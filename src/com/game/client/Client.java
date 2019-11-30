package com.game.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private boolean isConnected = false;
    private int queuedCard = 1;
    private String playerId = "";
    private PrintWriter sender;
    private Scanner receiver;

    public Client() {
        initComponents();
    }

    public void initComponents () {
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("1-2-3 Pass!");
        frame.setBackground(new java.awt.Color(37, 40, 47));

        jLayeredPane1.setBackground(new java.awt.Color(30, 40, 47));
        jLayeredPane1.setForeground(new java.awt.Color(30, 40, 47));

        gamePanel.setBackground(new java.awt.Color(255, 236, 177));

        leaderboardPanel.setBackground(new java.awt.Color(37, 40, 47));

        jLabel4.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(254, 254, 254));
        jLabel4.setText("LEADERBOARD");

        javax.swing.GroupLayout leaderboardPanelLayout = new javax.swing.GroupLayout(leaderboardPanel);
        leaderboardPanel.setLayout(leaderboardPanelLayout);
        leaderboardPanelLayout.setHorizontalGroup(
                leaderboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(leaderboardPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        leaderboardPanelLayout.setVerticalGroup(
                leaderboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(leaderboardPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        passPanel.setBackground(new java.awt.Color(10, 26, 81));
        passingCardText.setBackground(new java.awt.Color(255, 255, 255));
        passingCardText.setFont(new java.awt.Font("Ubuntu", 0, 48)); // NOI18N
        passingCardText.setForeground(new java.awt.Color(255, 255, 255));
        passingCardText.setText("                ...");

        javax.swing.GroupLayout passPanelLayout = new javax.swing.GroupLayout(passPanel);
        passPanel.setLayout(passPanelLayout);
        passPanelLayout.setHorizontalGroup(
                passPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(passPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(passingCardText, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                                .addContainerGap())
        );
        passPanelLayout.setVerticalGroup(
                passPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(passPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(passingCardText, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                                .addContainerGap())
        );

        card1.setBackground(new java.awt.Color(255, 255, 255));
        card1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
                card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 109, Short.MAX_VALUE)
        );
        card1Layout.setVerticalGroup(
                card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
        );

        card3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
                card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 109, Short.MAX_VALUE)
        );
        card3Layout.setVerticalGroup(
                card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
        );

        card4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
                card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 109, Short.MAX_VALUE)
        );
        card4Layout.setVerticalGroup(
                card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
        );

        card2.setBackground(new java.awt.Color(255, 255, 255));


        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
                card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 109, Short.MAX_VALUE)
        );
        card2Layout.setVerticalGroup(
                card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
        );

        playerNameText.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        playerNameText.setForeground(new java.awt.Color(10, 26, 81));
        playerNameText.setText("PLAYER");

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
                gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePanelLayout.createSequentialGroup()
                                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(gamePanelLayout.createSequentialGroup()
                                                .addGap(42, 42, 42)
                                                .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(37, 37, 37)
                                                .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(39, 39, 39)
                                                .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(39, 39, 39)
                                                .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(gamePanelLayout.createSequentialGroup()
                                                .addGap(107, 107, 107)
                                                .addComponent(passPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(gamePanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(playerNameText)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                .addComponent(leaderboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        gamePanelLayout.setVerticalGroup(
                gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(leaderboardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(gamePanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(passPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(playerNameText)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(gamePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
                jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(connectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(196, Short.MAX_VALUE))
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
                jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(connectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(115, Short.MAX_VALUE))
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                        .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane1)
        );

        card1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card1Clicked(evt);
            }
        });
        card2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card2Clicked(evt);
            }
        });
        card3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card3Clicked(evt);
            }
        });
        card4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card4Clicked(evt);
            }
        });
        frame.setVisible(true);
        frame.pack();
    }

    private void card4Clicked(MouseEvent evt) {
    }

    private void card3Clicked(MouseEvent evt) {
    }

    private void card2Clicked(MouseEvent evt) {
    }

    private void card1Clicked(MouseEvent evt) {
        System.out.println("Clicked!");

        sender.println("01:02:23");
    }

    public void run() {
        System.out.println("Server IP: " + serverIp.getText() + " Port #:" + portNumber.getText());

        // Creates a Dialog Box to get the server ip and port
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
        label.add(new JLabel("Server IP:", SwingConstants.RIGHT));
        label.add(new JLabel("Server Port", SwingConstants.RIGHT));
        panel.add(label, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField serverIp = new JTextField();
        controls.add(serverIp);
        JTextField serverPort = new JTextField();
        controls.add(serverPort);
        panel.add(controls, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(frame, panel, "Connection Information", JOptionPane.OK_CANCEL_OPTION);

        String ip = serverIp.getText();
        int port = Integer.parseInt(serverPort.getText());

        System.out.println("Connecting to " + ip + " on port " + port);

        try (Socket socket = new Socket(ip, port)) {

            this.isConnected = true;
            connectPanel.setVisible(false);
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());

            receiver = new Scanner(socket.getInputStream());
            sender = new PrintWriter(socket.getOutputStream(), true);

            while(receiver.hasNextLine()) {
                String packet = receiver.nextLine();
                String[] code = packet.split(":");

                String packetType = code[0];
                String playerId  = code[1];
                String cardCode = code[2];

                System.out.println("CODE: " + packetType + "-" + playerId + "-" + cardCode);

                switch(packetType) {
                    // READY PACKET
                    case "00" :

                        break;

                    // START/COUNT PACKET
                    case "01" :

                        break;

                    // PASS PACKET
                    case "02" :

                        break;

                    // CARDS MATCHED PACKET
                    case "03" :

                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main (String [] args) {
        try {
            Thread t = new Client();
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JFrame frame = new JFrame();
    private JPanel card1 = new JPanel();
    private JPanel card2 = new JPanel();
    private JPanel card3 = new JPanel();
    private JPanel card4 = new JPanel();
    private JPanel connectPanel = new JPanel();
    private JPanel gamePanel = new JPanel();
    private JLabel jLabel4 = new JLabel();
    private JLayeredPane jLayeredPane1 = new JLayeredPane();
    private JPanel leaderboardPanel = new JPanel();
    private JPanel passPanel = new JPanel();
    private JLabel passingCardText = new JLabel();
    private JLabel playerNameText = new JLabel();
    private JTextField portNumber = new JTextField();
    private JTextField serverIp = new JTextField();
}
