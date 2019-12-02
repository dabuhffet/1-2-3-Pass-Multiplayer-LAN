package com.game.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Handler extends Thread{

    private Socket client;

    public Handler (Socket client) throws IOException, InterruptedException {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Connected to handler!");

        try {
            Scanner receiver = new Scanner(this.client.getInputStream());
            while (receiver.hasNextLine()) {
                String packet = receiver.nextLine();
                Server.handle(packet);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
