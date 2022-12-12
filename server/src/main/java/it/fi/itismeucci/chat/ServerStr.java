package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerStr {
    
    public static ArrayList<ClientHandler>listaClient = new ArrayList<>();
    

    public void avvia() throws IOException {
        System.out.println("Server partito in esecuzione ... ");
        ServerSocket server = new ServerSocket(6969);
        for (;;) {
            Socket client;
            try {
                client = server.accept();
                ClientHandler newT = new ClientHandler();
                newT.start();
                System.out.println("Nuovo Thread creato");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }
}
