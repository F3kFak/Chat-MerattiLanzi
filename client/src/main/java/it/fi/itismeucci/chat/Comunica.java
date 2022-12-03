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
    String StringaUtente; // stringa inserita da utente
    String StringaRicevutaDalServer = ""; // stringa ricevuta dal server
    DataOutputStream outVersoServer; // stream output
    BufferedReader inDalServer; // stream input
    boolean controllo = false;

    public void invio() throws IOException {
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        miosocket = new Socket(nomeServer, portaServer);
        outVersoServer = new DataOutputStream(miosocket.getOutputStream());
        inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
        Messaggio mexInviato = new Messaggio();
        Messaggio mexRicevuto = new Messaggio();



        for(;;)
        {
            System.out.println("Stringa ricevuta dal server: " + StringaRicevutaDalServer);
            System.out.println("Client acceso...");
            System.out.println("...Pronto a Scrivere...");
            StringaUtente = tastiera.readLine();
            outVersoServer.writeBytes(StringaUtente + '\n');
        }
    }

    public void ricezione() throws IOException {
        for(;;)
        {
            StringaRicevutaDalServer = inDalServer.readLine();
            System.out.println(StringaRicevutaDalServer);
            /*utilizziamo il synchronized per attivare il thread di invio cosi da
            ricevere il messaggio e poi si fu un notify per riattivare il thread*/
        }
    }

    ObjectMapper objectMapper = new ObjectMapper();

    public String serializzazione(Messaggio mexInviato) throws JsonProcessingException{
        String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
        return stringaSerializzata;
    }

    public Messaggio deserializzazione (String mexRicevuto) throws JsonMappingException, JsonProcessingException{
        Messaggio stringaDeserializzata= objectMapper.readValue(mexRicevuto, Messaggio.class);
        return stringaDeserializzata;
    }
}
