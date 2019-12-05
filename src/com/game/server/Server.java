package com.game.server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Integer.parseInt;

public class Server extends Thread{

    private ServerSocket serverSocket;
    private static Hashtable <String, PrintWriter> senders;
    private static Hashtable <String, Boolean> sync = new Hashtable<String, Boolean>();
    private static Hashtable <String, ArrayList<String>> hands = new Hashtable<String, ArrayList<String>>();
    private static String cardArray[] = {"A","2","3","4","5","6","7","8","9","0","J","Q","K"};
    private static String suitsArray[] = {"D","H","S","C"};
    private static int numOfPlayers;
    private static Hashtable<String, String> cardFromLeft = new Hashtable<>();
    private static boolean cardFromLeftComplete;
    private static int finishedCounter;


    public Server (int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.senders = new Hashtable<String, PrintWriter>();
    }

    public static void broadcast (String message) {
        senders.forEach((key, value) -> {
            value.println(message);
        });
    }

    public static void send (String clientId, String message) {
        senders.get(clientId).println(message);
    }

    public static void count () throws InterruptedException {
        resetSync();
        for (int i = 1; i <= 3; i++) {
            broadcast("01:00:0" + i);
            System.out.println("Counting... " + i);
            Thread.sleep(1000);
        }
        broadcast("01:00:00");
        System.out.println("Pass...");
        Thread.sleep(1000);

        return;
    }

    public static void resetSync() {
        // Reset sync variables

        sync.forEach((key, value) -> {
            sync.put(key, false);
        });

        return;
    }

    public static Boolean checkSync() {
        AtomicBoolean allHasPassed = new AtomicBoolean(true);

        sync.forEach((key, value) -> {
            if (value == false) {
                allHasPassed.set(false);
            }
        });

        return allHasPassed.get();
    }

    public static String checkCards () {
        AtomicReference<String> playerId = new AtomicReference<>("");

        hands.forEach((id, hand) -> {
            AtomicBoolean matched = new AtomicBoolean(true);
            String suit = hand.get(0).substring(0,1);

            hand.forEach((card) -> {
                if (!card.contains(suit)) {
                    matched.set(false);
                }
            });

            if (matched.get()) {
                playerId.set(id);
            }
        });

        return playerId.get();
    }

    public static void handle (String packet) throws InterruptedException {
        String[] code = packet.split(":");

        String packetType = code[0];
        String playerId = code[1];
        String cardCode = code[2];

//        System.out.println("CODE: " + packetType + "-" + playerId + "-" + cardCode);

        switch (packetType) {
            // READY PACKET
            case "00":
                break;

            // START/COUNT PACKET
            case "01":
                if (playerId.matches("01")) {
                    //Randomly choose which types of card will be given
                    ArrayList<String> randomCards = new ArrayList<String>();
                    for (int i = 0; randomCards.size() < numOfPlayers; i++) {
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(13);
                        if (!(randomCards.contains(cardArray[randomIndex]))) randomCards.add(cardArray[randomIndex]);
                    }
                    //Create deck of cards with randomly chosen ranks and their 4 suits
                    ArrayList<String> deckOfCards = new ArrayList<String>();
                    for (int i = 0; i < randomCards.size(); i++) {
                        for (int j = 0; j < 4; j++) {
                            deckOfCards.add(randomCards.get(i) + suitsArray[j]);
                        }
                    }
                    Collections.shuffle(deckOfCards);

                    //send shuffled cards to each player
                    senders.forEach((key, value) -> {
                        ArrayList<String> listOfCards = new ArrayList<String>();
                        for (int i = 0; i < 4; i++) {
                            send(key, ("00:" + key + ":" + deckOfCards.get(deckOfCards.size() - 1)));
                            listOfCards.add(deckOfCards.get(deckOfCards.size() - 1));
                            deckOfCards.remove(deckOfCards.size() - 1);
                        }
                        hands.put(key, listOfCards);
                    });
                    System.out.println("Initial hands: ");
                    System.out.println(hands);

                    broadcast("01:00:04");
                    System.out.println("Starting game...");

                    count();
                }
                break;

            // PASS PACKET
            case "02":

                // Set sync variable of playerId to true, signifying that the player has passed.
                sync.put(playerId, true);

                int playerPosition = parseInt(playerId);

                String passToId = "";
                //find person to your right
                if (playerPosition < numOfPlayers) {

                    if (playerPosition < 10) {
                        String wew = String.valueOf((playerPosition+1));
                        passToId = "0" + String.valueOf(wew);
                    }
                    else passToId = String.valueOf(playerPosition + 1);
                } else {
                    passToId = "01";
                }
                //save passed card
                cardFromLeft.replace(passToId, cardCode);
                //set flag if all cards have been obtained by server
                if (cardFromLeft.containsValue("")) cardFromLeftComplete = false;
                else cardFromLeftComplete = true;
                //update hands if and only if server has collected all cards to be passed
                if (cardFromLeftComplete) {
                    hands.forEach((key, value) -> {
                        ArrayList<String> newHand = hands.get(key);
                        int position = Integer.parseInt(key);
                        for (int i = 0; i < 4; i++) {
                            String personToRight = "";
                            if(position < numOfPlayers){
                                if (position < 10) {
                                    String wew = String.valueOf((position+1));
                                    personToRight = "0" + wew;
                                }
                                else personToRight = String.valueOf(i + 1);
                            }else{
                                personToRight = "01";
                            }
                            if (newHand.get(i).equals(cardFromLeft.get(personToRight))) newHand.set(i, cardFromLeft.get(key));
                        }
                        hands.replace(key, newHand);


                    });
                    senders.forEach((key, value) -> {
                        value.println("02:"+key+":"+cardFromLeft.get(key));
                        cardFromLeft.replace(key,"");
                    });
                    cardFromLeftComplete = false;
                    System.out.println("New hands:");
                    System.out.println(hands);
                }

                //will check if the cards in the playerId's hands are matching, every match +1 to the flag
                ArrayList<String> listOfCards = hands.get(playerId);
                Integer matchCount = 0;
                for(int i = 0; i < 4; i++) {
                    if(listOfCards.get(i).startsWith(cardCode.substring(0,1))) matchCount++;
                }

                //if flag = 4 will send to player that all 4 matched
                if (matchCount== 4) send(playerId,"03:"+playerId+":00");

                // Check if there is a winning player
                String winningPlayer = checkCards();

                // If all sync flags are all true and there is no winning player
                //      start counting again
                if (checkSync() && winningPlayer.length() == 0) {
                    count();
                }

                // If there is a winning player, notify clients.
                if (winningPlayer.length() > 0) {
                   broadcast("03:" + winningPlayer + ":00");
                }

                break;

            // CARDS MATCHED PACKET
            case "03":

                finishedCounter += 1;
                String pt = "";
                if(finishedCounter < 10) {
                    pt = "03:" + playerId +":0"+ String.valueOf(finishedCounter);
                }else {
                    pt = "03:"+playerId + ":" + String.valueOf(finishedCounter);
                }
                send(playerId,pt);
                if(finishedCounter + 1 == numOfPlayers) System.exit(0);
                break;
        }
    }

    public void run() {
        System.out.println("Waiting for clients on port " + serverSocket.getLocalPort() + "...");

        while (true) {
            try {
                Socket client = serverSocket.accept();

                System.out.println("Just connected to " + client.getRemoteSocketAddress());

                PrintWriter sender = new PrintWriter(client.getOutputStream(), true);

                // Generate ID of this client
                String id = String.format("%02d", (senders.size() + 1));

                // Add sender of client to the pool of sender with its id as the key.
                //      This is used to track the clients connected to the server.
                senders.put(id, sender);
                cardFromLeft.put(id, "");
                // Initialize sync variable for thread syncing.
                sync.put(id, false);
                this.cardFromLeftComplete = false;
                // Send generated id to the client
                send(id, "00:" + id + ":00");
                numOfPlayers += 1;

                new Handler(client).start();

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

            Thread t = new Server(parseInt(serverIp.getText()));
            t.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
