package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerStr {
    ArrayList<Socket>ListaClient = new ArrayList<>();

    public void avvia() throws IOException {
        System.out.println("1 Server partito in esecuzione ... ");
        ServerSocket server = new ServerSocket(6969);
        for (;;) {
            Socket client;
            try {
                client = server.accept();
                ListaClient.add(client);
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
            for (;;) {
                System.out.println("Nuovo Thread creato");
                BufferedReader inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                DataOutput outVersoClient = new DataOutputStream(client.getOutputStream());
                String StringaRicevuta = inDalClient.readLine();
                System.out.print("Stringa ricevuto: ");
                System.out.println(StringaRicevuta);
                for (Socket c : ListaClient) {
                    DataOutput out = new DataOutputStream(c.getOutputStream());
                    out.writeBytes(StringaRicevuta + '\n');
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        // server.close();
    }
}
