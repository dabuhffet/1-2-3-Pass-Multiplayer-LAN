package com.game.server;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Server extends Thread{

    private ServerSocket serverSocket;
    private static Hashtable <String, PrintWriter> senders;
    private static Hashtable<String, ArrayList<String>> hands = new Hashtable<String, ArrayList<String>>();
    private Scanner receiver;
    private static String cardArray[] = {"A","2","3","4","5","6","7","8","9","0","J","Q","K"};
    private static String suitsArray[] = {"D","H","S","C"};
    private static int numOfPlayers;


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

    public static void handle (String packet) throws InterruptedException {
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
                        }
                        hands.put(key, listOfCards);
                    });
                    System.out.println(hands);

                    broadcast("01:00:04");
                    System.out.println("Starting game...");

                    for (int i = 1; i <= 3; i++) {
                        broadcast("01:00:0" + i);
                        System.out.println("Counting... " + i);
                        Thread.sleep(1000);
                    }
                    broadcast("01:00:00");
                    System.out.println("Pass...");
                    Thread.sleep(2000);

                    // TODO: Insert here handling end game passing
                }
                break;

            // PASS PACKET
            case "02":
                int playerPosition = parseInt(playerId);
                //System.out.println(playerPosition);
                String passToId = "";
                //find person to your right
                if(playerPosition < numOfPlayers){
                    if(playerPosition < 10) passToId = "0"+String.valueOf(playerPosition+1);
                    else passToId = String.valueOf(playerPosition+1);
                }else{
                    passToId = "01";
                }
               send(passToId, ("02:"+playerId+":"+cardCode));
                //System.out.print("02:"+playerId+":"+cardCode);
                //passing card to person to the right will make one of your slots empty
                ArrayList<String> newHand = hands.get(playerId);
                for(int i = 0; i < 4; i++){
                    if(newHand.get(i).equals(cardCode)) newHand.set(i,"00");
                }
                hands.replace(playerId, newHand);
                //edit the hand of the person to the right
                ArrayList<String> newPassHand = hands.get(passToId);
                for(int i = 0; i < 4; i++){
                    if((newPassHand.get(i)).equals("00")) newPassHand.set(i,cardCode);
                }
                hands.replace(passToId, newPassHand);

                System.out.println(hands);

                //will check if the cards in in the playerId's hands are matching, every match +1 to the flag
                ArrayList<String> listOfCards = hands.get(playerId);
                Integer matchCount = 0;
                for(int i = 0; i < 4; i++) {
                    if(listOfCards.get(i).startsWith(cardCode.substring(0,1))) matchCount++;
                }
                //if flag = 4 will send to player that all 4 matched
                if (matchCount== 4) send(playerId,"03:"+playerId+":00");

                for (int i = 1; i <= 3; i++) {
                    broadcast("01:00:0" + i);
                  //  System.out.println("Counting... " + i);
                    Thread.sleep(1500);
                }
                broadcast("01:00:00");
                //System.out.println("Pass...");
                Thread.sleep(2000);

                break;

            // CARDS MATCHED PACKET
            case "03":

                break;
        }
    }

    public void run() {
        System.out.println("Waiting for clients on port " + serverSocket.getLocalPort() + "...");

        while (true) {
            try {
                Socket client = serverSocket.accept();

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

                new Thread(new Handler(client)).start();


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
