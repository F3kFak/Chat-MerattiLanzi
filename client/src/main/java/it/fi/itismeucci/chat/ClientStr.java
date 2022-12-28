package it.fi.itismeucci.chat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientStr{

    public Socket miosocket;
    public int portaServer = 6969; // porta
    public String nomeServer = "localhost"; // indirizzo server locale
    public static BufferedReader tastiera; // buffer per l'input da tastiera

    public static String nomeClient; //nome del client
    public static String stringaRicevutaDalServer = ""; // stringa ricevuta dal server
    public static DataOutputStream outVersoServer; // stream output
    public static BufferedReader inVersoServer;

    public static ObjectMapper objectMapper = new ObjectMapper();
    public static Messaggio mexInviato = new Messaggio();
    public static Messaggio mexRicevuto = new Messaggio();
    public static ArrayList<String> destinatarioArrayList = new ArrayList<String>();

    public ClientStr() throws IOException {
        
	}

	public void comunica() throws IOException {
        ClientStr C1 = new ClientStr();
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
            nomeClient = tastiera.readLine();
            //serializzare in una classe
            mexInviato.setMittente(nomeClient);
            inviaMessaggio(mexInviato);
            //leggi la risposta (deserilizza)
            mexRicevuto = riceviMessaggio();
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

    public static Messaggio riceviMessaggio() throws IOException{
        //leggo il messaggio ricevuto dal server
        stringaRicevutaDalServer = inVersoServer.readLine();
        // deserializzo
        Messaggio stringaDeserializzata = objectMapper.readValue(stringaRicevutaDalServer, Messaggio.class);
        return stringaDeserializzata;
    }

    public static void inviaMessaggio(Messaggio mex) throws IOException {
        // serializzo
        String stringaSerializzata = objectMapper.writeValueAsString(mex);
        // scrivo il messaggio
        outVersoServer.writeBytes(stringaSerializzata + '\n');
    }

    public static void threadInviaMessaggio() throws IOException {
        mexInviato.setMittente(nomeClient);
        menuOpzioni();
        //seleziona l'opzione del destinatario
        String opzione = tastiera.readLine();
        //imposto il destinatario del messaggio ed il tipo di comando
        switch(opzione){
            case "1":
                destinatarioArrayList.add("all");
                mexInviato.setDestinatario(destinatarioArrayList);
                mexInviato.setComando("1");
                break;
            case "2":
                //TOFIX
                String destinatario = tastiera.readLine();
                destinatarioArrayList.clear();
                destinatarioArrayList.add(destinatario);
                mexInviato.setDestinatario(destinatarioArrayList);
                mexInviato.setComando("2");
                break;
            default:
                System.out.println("Opzione non valida");
                break;
        }
        System.out.print("Inserisci il messaggio: ");
        //scrivo il corpo del messaggio
        String corpo = tastiera.readLine();
        mexInviato.setCorpo(corpo);
        //invio il messaggio al server
        inviaMessaggio(mexInviato);
    }

    public static void threadRiceviMessaggio() throws IOException {
        Messaggio stringaDeserializzata = riceviMessaggio();
        //messaggio dal server
        if(stringaDeserializzata.getMittente().equals("Server")){
            //messaggio di sistema
            System.out.println(stringaDeserializzata.getCorpo());
            System.out.println("");
        }
        // messaggio in broadcast da un altro client
        else if(stringaDeserializzata.getComando().equals("1")){
            System.out.println(stringaDeserializzata.getMittente() + " ha scritto a tutti: " + stringaDeserializzata.getCorpo());
            System.out.println("");
        }
        // messaggio privato da un altro client
        else if(stringaDeserializzata.getComando().equals("2")){
            System.out.println(stringaDeserializzata.getMittente() + ": " + stringaDeserializzata.getCorpo());
            System.out.println("");
        }
        menuOpzioni();
    }

    public static void menuOpzioni() throws IOException{
        System.out.println("--------------Menu---------------");
        System.out.println("1--> A tutti" + '\n' + "2--> destinatario" + '\n');
        System.out.println("Seleziona l'opzione per il destinatario: ");
    }
}
