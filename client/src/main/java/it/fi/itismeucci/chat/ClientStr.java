package it.fi.itismeucci.chat;

import java.io.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientStr extends Comunica{
    
    public static String stringaUtente; // stringa inserita da utente
    public static String stringaRicevutaDalServer = ""; // stringa ricevuta dal server
    public static DataOutputStream outVersoServer; // stream output
    public static BufferedReader inVersoServer;
    
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static Messaggio mexInviato = new Messaggio();
    public static Messaggio mexRicevuto = new Messaggio();

    public ClientStr() throws IOException {
		super();
	}

	public void comunica() throws IOException {
        Comunica C1 = new Comunica();
        ThreadInput ti = new ThreadInput(C1);
        ThreadOutput to = new ThreadOutput(C1);

        to.start();
        ti.start();
    }

    
    public void registra() throws IOException{
        /* do
            System.out.println("Inserisci nome:");
            stringaUtente = tastiera.readLine();

            //serializzare in una classe
            outVersoServer.writeBytes(stringaUtente + '\n');

            //leggi la risposta (deserilizza)
        while();
        */
    }
    

    public void messaggioErrore(String err) {
        mexInviato.setMittente("Server");
        mexInviato.setCorpo(err);
        System.out.println(err);
    }

    public static void inviaMessaggio() throws IOException {
        System.out.println("Client acceso...");
        System.out.println("...Pronto a Scrivere...");
        //legge il buffer della tastiera salva il valor e
        stringaUtente = tastiera.readLine();
        // serializzo
        String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
        // scrivo il messaggio serializzato
        outVersoServer.writeBytes(stringaSerializzata + '\n');
    }

    public static Messaggio riceviMessaggio() throws IOException {
        //leggo il messaggio ricevuto dal server
        stringaRicevutaDalServer = inVersoServer.readLine();
        // deserializzo
        Messaggio stringaDeserializzata = objectMapper.readValue(stringaRicevutaDalServer, Messaggio.class);
        // ritorno il l'istanza deserializzata
        return stringaDeserializzata;
    }

}
