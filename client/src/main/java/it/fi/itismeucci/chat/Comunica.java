package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;

public class Comunica {
    String nomeServer = "localhost"; // indirizzo server locale
    int portaServer = 6969; // porta
    Socket miosocket;
    BufferedReader tastiera; // buffer per l'input da tastiera
    String StringaUtente; // stringa inserita da utente
    String StringaRicevutaDalServer; // stringa ricevuta dal server
    DataOutputStream outVersoServer; // stream output
    BufferedReader inDalServer; // stream input

    public void invio() throws IOException {
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        miosocket = new Socket(nomeServer, portaServer);
        outVersoServer = new DataOutputStream(miosocket.getOutputStream());
        inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
        for(;;)
        {
            System.out.println("Client acceso...");
            System.out.println("...Pronto a Scrivere...");
            StringaUtente = tastiera.readLine();
            outVersoServer.writeBytes(StringaUtente + '\n');
            StringaRicevutaDalServer = inDalServer.readLine();
        }
    }

    public void ricezione() throws IOException {
        for(;;)
        {
            StringaRicevutaDalServer = inDalServer.readLine();
            System.out.println(StringaRicevutaDalServer);
        }
    }
}
