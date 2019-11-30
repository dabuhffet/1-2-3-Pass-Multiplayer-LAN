package com.game.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread{

    private ServerSocket serverSocket;
    private PrintWriter sender;
    private Scanner receiver;

    public Server (int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void run() {
        System.out.println("Waiting for clients on port " + serverSocket.getLocalPort() + "...");

        while (true) {
            try (Socket client = serverSocket.accept()) {
                System.out.println("Just connected to " + client.getRemoteSocketAddress());

                receiver = new Scanner(client.getInputStream());
                sender = new PrintWriter(client.getOutputStream(), true);

                while (receiver.hasNextLine()) {
                    String packet = receiver.nextLine();
                    String[] code = packet.split(":");

                    String packetType = code[0];
                    String playerId = code[1];
                    String cardCode = code[2];

                    System.out.println("CODE: " + packetType + "-" + playerId + "-" + cardCode);

                    switch (packetType) {
                        // READY PACKET
                        case "00":

                            break;

                        // START/COUNT PACKET
                        case "01":

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
            int port = 8081;
            Thread t = new Server(port);
            t.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
