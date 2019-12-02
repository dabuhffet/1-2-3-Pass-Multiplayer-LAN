package com.game.client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {
    private boolean isConnected = false;
    private int queuedCard = 1;
    private ArrayList<String> cards = new ArrayList<String>();
    private String playerId = "";
    private PrintWriter sender;
    private Scanner receiver;

    public Client() {
        initComponents();
    }

    public void initComponents () {
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("1-2-3 Pass!");
        frame.setBackground(new Color(10, 10, 10));
        frame.setPreferredSize(new Dimension(800,490));
        frame.setResizable(false);

        jLayeredPane1.setBackground(new Color(30, 40, 47));
        jLayeredPane1.setForeground(new Color(30, 40, 47));

        gamePanel.setBackground(new Color(10, 10, 10));

        leaderboardPanel.setBorder(new LineBorder(new Color(255,255,255), 2));
        leaderboardPanel.setBackground(new java.awt.Color(10, 10, 10));
        leaderboardPanel.setBorder(new LineBorder(new Color(255,255,255), 2));

        jLabel4.setText("LEADERBOARD");
        jLabel4.setFont(new Font("Consolas", 0, 20));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));

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
        passPanel.setBackground(new Color(10, 10, 10));
        passPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255),3));
        passingCardText.setBackground(new Color(255, 255, 255));
        passingCardText.setFont(new Font("Consolas", 0, 36));
        passingCardText.setForeground(new Color(255, 255, 255));
        passingCardText.setText("        ...        ");

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

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
                card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 101, Short.MAX_VALUE)
                        .addComponent(card1Label)
        );
        card1Layout.setVerticalGroup(
                card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
                        .addComponent(card1Label)
        );

        card3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
                card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 101, Short.MAX_VALUE)
                        .addComponent(card3Label)
        );
        card3Layout.setVerticalGroup(
                card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
                        .addComponent(card3Label)
        );

        card4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
                card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 101, Short.MAX_VALUE)
                        .addComponent(card4Label)
        );
        card4Layout.setVerticalGroup(
                card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
                        .addComponent(card4Label)
        );

        card2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
                card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 101, Short.MAX_VALUE)
                        .addComponent(card2Label)
        );
        card2Layout.setVerticalGroup(
                card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 155, Short.MAX_VALUE)
                        .addComponent(card2Label)
        );

        playerNameText.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        playerNameText.setForeground(new java.awt.Color(255, 255, 255));
        playerNameText.setText("Connecting to server...");

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
                                        .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void removeCardBorder () {
        card1.setBorder(null);
        card2.setBorder(null);
        card3.setBorder(null);
        card4.setBorder(null);
    }
    private void setBorder (JPanel card) {
        Border insideBorder = BorderFactory.createMatteBorder(0,3,0,3, new Color(0,0,0));
        Border outsideBorder = BorderFactory.createMatteBorder(0,3,0,3, new Color(255,255,255));
        card.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
    }

    private void card4Clicked(MouseEvent evt) {
        this.queuedCard = 4;
        this.removeCardBorder();
        this.setBorder(card4);
    }

    private void card3Clicked(MouseEvent evt) {
        this.queuedCard = 3;
        this.removeCardBorder();
        this.setBorder(card3);
    }

    private void card2Clicked(MouseEvent evt) {
        this.queuedCard = 2;
        this.removeCardBorder();
        this.setBorder(card2);
    }

    private void card1Clicked(MouseEvent evt) {
        this.queuedCard = 1;
        this.removeCardBorder();
        this.setBorder(card1);
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

        try (Socket socket = new Socket(ip, port)) {

            this.isConnected = true;
            connectPanel.setVisible(false);
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());

            this.receiver = new Scanner(socket.getInputStream());
            this.sender = new PrintWriter(socket.getOutputStream(), true);

            while(receiver.hasNextLine()) {
                String packet = receiver.nextLine();
                String[] code = packet.split(":");

                String packetType = code[0];
                String playerId  = code[1];
                String cardCode = code[2];

                //System.out.println("CODE: " + packetType + "-" + playerId + "-" + cardCode);

                switch(packetType) {
                    // READY PACKET
                    case "00" :
                        // If playerId has been set, packets are about the cards
                        if (this.playerId.length() > 0) {
                            System.out.println("Card received... [" + cardCode + "]");
                            cards.add(cardCode);
                            //Set card labels
                            if(cards.size() == 1){
                                card1Label.setText(cardCode);
                                card1Label.setFont(new Font("Consolas", 0, 60));
                                if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card1Label.setForeground(new java.awt.Color(255, 0, 0));
                            }else if(cards.size() == 2){
                                card2Label.setText(cardCode);
                                card2Label.setFont(new Font("Consolas", 0, 60));
                                if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card2Label.setForeground(new java.awt.Color(255, 0, 0));
                            }else if(cards.size() == 3){
                                card3Label.setText(cardCode);
                                card3Label.setFont(new Font("Consolas", 0, 60));
                                if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card3Label.setForeground(new java.awt.Color(255, 0, 0));
                            }else if(cards.size() == 4){
                                card4Label.setText(cardCode);
                                card4Label.setFont(new Font("Consolas", 0, 60));
                                if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card4Label.setForeground(new java.awt.Color(255, 0, 0));
                            }

                        }
                        else {
                            // Initialize player information
                            this.playerId = playerId;
                            this.playerNameText.setText("PLAYER " + playerId + " | Connected on " + socket.getRemoteSocketAddress());
                            this.passingCardText.setText("  WAITING TO START  ");

                            // Set initial picked card
                            this.queuedCard = 1;
                            this.setBorder(card1);

                            if (this.playerId.matches("01")) {
                                JOptionPane.showMessageDialog(frame, "Would you like to start the game now?");

                                // Send start game signal packet.
                                System.out.println("Sending start signal packet...");
                                this.sender.println("01:01:00");

                                this.passingCardText.setText("        START       ");
                            }
                        }
                        break;

                    // START/COUNT PACKET
                    case "01" :
                        if (cardCode.matches("00")) {
                            this.passingCardText.setText("        PASS!       ");
                            // TODO: Insert pass code here.
                            // sender.println("02:" + this.playerId + ":" + cards[queuedCard]);
                            this.sender.println("02:"+this.playerId+":"+cards.get(queuedCard - 1));
                        }
                        else if (cardCode.matches("01")) {
                            this.passingCardText.setText("          1         ");
                        }
                        else if (cardCode.matches("02")) {
                            this.passingCardText.setText("          2         ");
                        }
                        else if (cardCode.matches("03")) {
                            this.passingCardText.setText("          3         ");
                        }
                        else if (cardCode.matches("04"))  {
                            this.passingCardText.setText("        START       ");
                        }
                        else {
                            this.passingCardText.setText("        ERROR       ");
                        }
                        break;

                    // PASS PACKET
                    case "02" :
                        //replace chosen queuedcard with new card
                        if(queuedCard == 1){
                            cards.set(0, cardCode);
                            card1Label.setText(cardCode);
                            card1Label.setFont(new Font("Consolas", 0, 60));
                            if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card1Label.setForeground(new java.awt.Color(255, 0, 0));
                            else card1Label.setForeground(new java.awt.Color(0, 0, 0));
                        }else if(queuedCard == 2){
                            cards.set(1, cardCode);
                            card2Label.setText(cardCode);
                            card2Label.setFont(new Font("Consolas", 0, 60));
                            if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card2Label.setForeground(new java.awt.Color(255, 0, 0));
                            else card2Label.setForeground(new java.awt.Color(0, 0, 0));
                        }else if (queuedCard == 3){
                            cards.set(2, cardCode);
                            card3Label.setText(cardCode);
                            card3Label.setFont(new Font("Consolas", 0, 60));
                            if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card3Label.setForeground(new java.awt.Color(255, 0, 0));
                            else card3Label.setForeground(new java.awt.Color(0, 0, 0));
                        }else if(queuedCard == 4){
                            cards.set(3, cardCode);
                            card4Label.setText(cardCode);
                            card4Label.setFont(new Font("Consolas", 0, 60));
                            if(cardCode.substring(1).equals("H") || cardCode.substring(1).equals("D"))card4Label.setForeground(new java.awt.Color(255, 0, 0));
                            else card4Label.setForeground(new java.awt.Color(0, 0, 0));
                        }
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
    private JLabel card1Label = new JLabel();
    private JLabel card2Label = new JLabel();
    private JLabel card3Label = new JLabel();
    private JLabel card4Label = new JLabel();
}
