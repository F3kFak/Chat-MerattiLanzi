package it.fi.itismeucci.chat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientStr{

    public Socket miosocket;
    public int portaServer = 6969; // porta
    public String nomeServer = "localhost"; // indirizzo server locale
    public static BufferedReader tastiera; // buffer per l'input da tastiera

    public static String stringaUtente; // stringa inserita da utente
    public static String stringaRicevutaDalServer = ""; // stringa ricevuta dal server
    public static DataOutputStream outVersoServer; // stream output
    public static BufferedReader inVersoServer;

    public static ObjectMapper objectMapper = new ObjectMapper();
    public static Messaggio mexInviato = new Messaggio();
    public static Messaggio mexRicevuto = new Messaggio();

    public ClientStr() throws IOException {
        
	}

	public void comunica() throws IOException {
        Comunica C1 = new Comunica();
        ThreadInput ti = new ThreadInput(C1);
        ThreadOutput to = new ThreadOutput(C1);

        to.start();
        ti.start();
    }

    public Socket connect() throws UnknownHostException, IOException{
        this.miosocket = new Socket(nomeServer, portaServer);
        ClientStr.tastiera = new BufferedReader(new InputStreamReader(System.in));
        ClientStr.outVersoServer = new DataOutputStream(miosocket.getOutputStream());
        ClientStr.inVersoServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
        return miosocket;
    }
    
    public void registra() throws IOException{
        boolean entrato = true;
        
        do{
            System.out.print("Inserisci nome: ");
            stringaUtente = tastiera.readLine();
            //serializzare in una classe
            mexInviato.setMittente(stringaUtente);
            inviaMessaggio(mexInviato);
            //leggi la risposta (deserilizza)
            mexRicevuto = threadRiceviMessaggio();
            //controllo se sono entrato a far parte della chat
            if(mexRicevuto.getMittente().equals("Server") && mexRicevuto.getCorpo().equals("entrato"))
                entrato = false;
            else
                System.out.println("Nome utente già esistente. Riprova...");
        }while(entrato);
        System.out.println("Entrato nella chat");
    }

    public void messaggioErrore(String err) {
        mexInviato.setMittente("Server");
        mexInviato.setCorpo(err);
        System.out.println(err);
    }

    public void inviaMessaggio(Messaggio mex) throws IOException {
        // serializzo
        String stringaSerializzata = objectMapper.writeValueAsString(mex);
        // scrivo il messaggio
        outVersoServer.writeBytes(stringaSerializzata + '\n');
    }

    public static void threadInviaMessaggio() throws IOException {
        System.out.println("...Pronto a Scrivere...");
        //legge il buffer della tastiera salva il valor e
        stringaUtente = tastiera.readLine();
        // serializzo
        String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
        // scrivo il messaggio serializzato
        outVersoServer.writeBytes(stringaSerializzata + '\n');
    }

    public static Messaggio threadRiceviMessaggio() throws IOException {
        //leggo il messaggio ricevuto dal server
        stringaRicevutaDalServer = inVersoServer.readLine();
        // deserializzo
        Messaggio stringaDeserializzata = objectMapper.readValue(stringaRicevutaDalServer, Messaggio.class);
        // ritorno il l'istanza deserializzata
        return stringaDeserializzata;
    }

}
