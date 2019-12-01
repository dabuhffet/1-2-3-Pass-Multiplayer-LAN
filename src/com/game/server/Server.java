package com.game.server;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread{

    private ServerSocket serverSocket;
    private Hashtable <String, PrintWriter> senders;
    private Hashtable<String, ArrayList<String>> hands = new Hashtable<String, ArrayList<String>>();
    private Scanner receiver;
    private String cardArray[] = {"A","2","3","4","5","6","7","8","9","0","J","Q","K"};
    private String suitsArray[] = {"D","H","S","C"};
    private int numOfPlayers;

    public Server (int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.senders = new Hashtable<String, PrintWriter>();
    }

    public void broadcast (String message) {
        senders.forEach((key, value) -> {
            value.println(message);
        });
    }

    public void send (String clientId, String message) {
        senders.get(clientId).println(message);
    }

    public void run() {
        System.out.println("Waiting for clients on port " + serverSocket.getLocalPort() + "...");

        while (true) {
            try (Socket client = serverSocket.accept()) {
                System.out.println("Just connected to " + client.getRemoteSocketAddress());

                receiver = new Scanner(client.getInputStream());

                PrintWriter sender = new PrintWriter(client.getOutputStream(), true);

                // Generate ID of this client
                String id = String.format("%02d", (senders.size() + 1));

                // Add sender of client to the pool of sender with its id as the key.
                //      This is used to track the clients connected to the server.
                senders.put(id, sender);

                // Send generated id to the client
                send(id, "00:" + id + ":00");
                numOfPlayers += 1;
                while (receiver.hasNextLine()) {
                    String packet = receiver.nextLine();
                    String[] code = packet.split(":");

                    String packetType = code[0];
                    String playerId = code[1];
                    String cardCode = code[2];

                    System.out.println("CODE: " + packetType + "-" + playerId + "-" + cardCode);

                    switch (packetType) {
                        // READY PACKET
                        case "00" :
                            break;

                        // START/COUNT PACKET
                        case "01":
                            if (playerId.matches("01")) {
                               //Randomly choose which types of card will be given
                                ArrayList<String> randomCards = new ArrayList<String>();
                                for(int i = 0; randomCards.size() < numOfPlayers; i++){
                                    Random rand = new Random();
                                    int randomIndex = rand.nextInt(13);
                                    if(!(randomCards.contains(cardArray[randomIndex]))) randomCards.add(cardArray[randomIndex]);
                                }
                                //Create deck of cards with randomly chosen ranks and their 4 suits
                                ArrayList<String> deckOfCards = new ArrayList<String>();
                                for(int i = 0; i < randomCards.size(); i++){
                                    for(int j = 0; j < 4; j++){
                                        deckOfCards.add(randomCards.get(i) + suitsArray[j]);
                                    }
                                }
                                Collections.shuffle(deckOfCards);

                                //send shuffled cards to each player
                                senders.forEach((key, value) -> {
                                    ArrayList<String> listOfCards = new ArrayList<String>();
                                    for(int i = 0; i < 4; i++) {
                                        send(key,("00:" + key +":"+ deckOfCards.get(deckOfCards.size()-1)));
                                        listOfCards.add(deckOfCards.get(deckOfCards.size()-1));
                                        deckOfCards.remove(deckOfCards.size()-1);
                                    }hands.put(key, listOfCards);
                                });
                                System.out.println(hands);

                                this.broadcast("01:00:04");
                                System.out.println("Starting game...");

                                for (int i = 3; i > 0; i--) {
                                    this.broadcast("01:00:0" + i);
                                    System.out.println("Counting... " + i);
                                    Thread.sleep(1000);
                                }
                                this.broadcast("01:00:00");
                                System.out.println("Pass...");

                                // TODO: Insert here handling end game passing
                            }
                            break;

                        // PASS PACKET
                        case "02":

                            break;

                        // CARDS MATCHED PACKET
                        case "03":

                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Creates a Dialog Box to get the port to use
            JFrame frame = new JFrame();
            JPanel panel = new JPanel(new BorderLayout(5, 5));

            JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
            label.add(new JLabel("Server IP:", SwingConstants.RIGHT));
            panel.add(label, BorderLayout.WEST);

            JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
            JTextField serverIp = new JTextField();
            controls.add(serverIp);
            panel.add(controls, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(frame, panel, "Connection Information", JOptionPane.OK_CANCEL_OPTION);

            Thread t = new Server(Integer.parseInt(serverIp.getText()));
            t.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
