package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Comunica {
    String nomeServer = "localhost"; // indirizzo server locale
    int portaServer = 6969; // porta
    Socket miosocket;
    BufferedReader tastiera; // buffer per l'input da tastiera

    String stringaUtente; // stringa inserita da utente
    String stringaRicevutaDalServer = ""; // stringa ricevuta dal server
    DataOutputStream outVersoServer; // stream output
    BufferedReader in;

    ObjectMapper objectMapper = new ObjectMapper();
    Messaggio mexInviato = new Messaggio();
    Messaggio mexRicevuto = new Messaggio();

    public Comunica() throws IOException {
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        miosocket = new Socket(nomeServer, portaServer);
        outVersoServer = new DataOutputStream(miosocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
    }

    public void invio() throws IOException {
        System.out.println("Client acceso...");
        System.out.println("...Pronto a Scrivere...");
        stringaUtente = tastiera.readLine();
        outVersoServer.writeBytes(stringaUtente + '\n');
    }

    public void ricezione() throws IOException {
        stringaRicevutaDalServer = in.readLine();
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
}
