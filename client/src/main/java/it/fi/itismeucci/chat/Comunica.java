package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;

public class Comunica {
    String nomeServer = "localhost"; // indirizzo server locale
    int portaServer = 6969; // porta
    Socket miosocket;
    static BufferedReader tastiera; // buffer per l'input da tastiera

    public Comunica() throws IOException {
        this.tastiera = new BufferedReader(new InputStreamReader(System.in));
        this.miosocket = new Socket(nomeServer, portaServer);
        ClientStr.outVersoServer = new DataOutputStream(miosocket.getOutputStream());
        ClientStr.inVersoServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
    }

    /*
     * public void invio() throws IOException {
        System.out.println("Client acceso...");
        System.out.println("...Pronto a Scrivere...");
        stringaUtente = tastiera.readLine();
        outVersoServer.writeBytes(stringaUtente + '\n');
    }

    public void ricezione() throws IOException {
        stringaRicevutaDalServer = inVersoServer.readLine();
        System.out.println(stringaRicevutaDalServer);

        // cha faccio ? deserializza e in base a cosa ho ricevuto stampo .....
    }

    public String serializzazione(Messaggio mexInviato) throws JsonProcessingException{
        String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
        return stringaSerializzata;
    }

    public Messaggio deserializzazione (String mexRicevuto) throws JsonMappingException, JsonProcessingException{

        Messaggio stringaDeserializzata = objectMapper.readValue(mexRicevuto, Messaggio.class);
        return stringaDeserializzata;
    }
     */
}
