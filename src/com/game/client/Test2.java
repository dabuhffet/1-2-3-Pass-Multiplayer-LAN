package com.game.client;

public class Test2 {
    public static void main (String [] args) {
        try {
            Thread t = new Client();
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
