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
        String opzione;
        boolean entrato = false;
        do{
            menuOpzioni();
            //seleziona l'opzione del destinatario
            opzione = tastiera.readLine();
            //imposto il destinatario del messaggio ed il tipo di comando
            switch(opzione){
                case "1":
                    entrato = true;
                    destinatarioArrayList.add("all");
                    mexInviato.setDestinatario(destinatarioArrayList);
                    mexInviato.setComando("1");
                    break;
                case "2":
                    entrato = true;
                    System.out.print("Inserisci il destinatario: ");
                    String destinatario = tastiera.readLine();
                    destinatarioArrayList.clear();
                    destinatarioArrayList.add(destinatario);
                    mexInviato.setDestinatario(destinatarioArrayList);
                    mexInviato.setComando("2");
                    break;
                default:
                    System.out.println("Opzione non valida");
                    entrato = false;
                    break;
            }
        }while(!entrato);
        System.out.print("Inserisci il messaggio: ");
        //scrivo il corpo del messaggio
        String corpo = tastiera.readLine();
        mexInviato.setCorpo(corpo);
        //invio il messaggio al server
        inviaMessaggio(mexInviato);
    }

    public static void threadRiceviMessaggio() throws IOException {
        Messaggio mexRicevuto = riceviMessaggio();
        System.out.println('\n');
        //messaggio dal server
        if(mexRicevuto.getMittente().equals("Server") && mexRicevuto.getComando().equals("0")){
            //messaggio di sistema
            System.out.println(mexRicevuto.getCorpo());
            System.out.println("");
        }
        // messaggio in broadcast da un altro client
        else if(mexRicevuto.getComando().equals("1")){
            System.out.println(mexRicevuto.getMittente() + " ha scritto a tutti: " + mexRicevuto.getCorpo());
            System.out.println("");
        }
        // messaggio privato da un altro client
        else if(mexRicevuto.getComando().equals("2")){
            System.out.println(mexRicevuto.getMittente() + " ti ha scritto: " + mexRicevuto.getCorpo());
            System.out.println("");
        }
        else if(mexRicevuto.getComando().equals("-1") && mexRicevuto.getCorpo().equals("menu")){
            System.out.println(mexRicevuto.getDestinatario());
        }
        //riscrivo il menu perche nel caso ricevo un messaggio so cosa devo scrivere
        menuOpzioni();
    }

    public static void menuOpzioni() throws IOException{
        System.out.println("---------------Menu---------------");
        System.out.println("1--> A tutti" + '\n' + "2--> destinatario" + '\n');
        System.out.print("Seleziona l'opzione per il destinatario: ");
    }
}
