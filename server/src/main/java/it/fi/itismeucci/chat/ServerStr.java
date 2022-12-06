package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerStr {
    
    ArrayList<Socket>listaClient = new ArrayList<>();

    public void avvia() throws IOException {
        System.out.println("1 Server partito in esecuzione ... ");
        ServerSocket server = new ServerSocket(6969);
        for (;;) {
            Socket client;
            try {
                client = server.accept();
                Thread t = new Thread(() -> comunica(client, server));
                t.start();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    public void comunica(Socket client, ServerSocket server) {
        try {
            System.out.println("Nuovo Thread creato");
            for (;;) {
                BufferedReader inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String stringaRicevuta = inDalClient.readLine();
                
                System.out.println("Stringa ricevuta: " + stringaRicevuta);
                for (Socket c : listaClient) {
                    System.out.println("Inoltro messaggio a " + c);
                    DataOutputStream out = new DataOutputStream(c.getOutputStream());
                    out.writeBytes(stringaRicevuta + '\n');
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        // server.close();
    }
}
