package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerStr {
    
    //array di tutte le connessioni
    public static ArrayList<ClientHandler>listaClient = new ArrayList<>();
    public static ArrayList<String> allClientsName = new ArrayList<>();

    public void avvia() throws IOException {
        System.out.println("Server partito in esecuzione ... ");
        try (ServerSocket server = new ServerSocket(6969)) {
            for (;;) {
                Socket client;
                try {
                    client = server.accept();
                    ClientHandler newT = new ClientHandler(client);
                    newT.start();
                    System.out.println("Nuovo Thread creato");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
        }
    }
}
