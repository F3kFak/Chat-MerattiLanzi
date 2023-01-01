package it.fi.itismeucci.chat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientStr {

    public Socket miosocket;
    public int portaServer = 6969; // porta
    public String nomeServer = "localhost"; // indirizzo server locale
    public static BufferedReader tastiera; // buffer per l'input da tastiera

    public static String nomeClient; // nome del client
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

    public Socket connect() throws UnknownHostException, IOException {
        this.miosocket = new Socket(nomeServer, portaServer);
        ClientStr.tastiera = new BufferedReader(new InputStreamReader(System.in));
        ClientStr.outVersoServer = new DataOutputStream(miosocket.getOutputStream());
        ClientStr.inVersoServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
        return miosocket;
    }

    public void registra() throws IOException {
        boolean entrato = true;

        do {
            System.out.print("Inserisci nome: ");
            nomeClient = tastiera.readLine();
            // serializzare in una classe
            mexInviato.setMittente(nomeClient);
            inviaMessaggio(mexInviato);
            // leggi la risposta (deserilizza)
            mexRicevuto = riceviMessaggio();
            // controllo se sono entrato a far parte della chat
            if (mexRicevuto.getMittente().equals("Server") && mexRicevuto.getCorpo().equals("entrato"))
                entrato = false;
            else
                System.out.println("Nome utente già esistente. Riprova...");
        } while (entrato);
        System.out.println("Entrato nella chat");
    }

    public void messaggioErrore(String err) {
        mexInviato.setMittente("Server");
        mexInviato.setCorpo(err);
        System.out.println(err);
    }

    public static Messaggio riceviMessaggio() throws IOException {
        // leggo il messaggio ricevuto dal server
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
        do {
            menuOpzioni();
            // seleziona l'opzione del destinatario
            opzione = tastiera.readLine();
            // imposto il destinatario del messaggio ed il tipo di comando
            switch (opzione) {
                //chiudo la connessione
                //TODO
                case "0":
                    break;
                case "1":
                //scrivo un messaggio con 2 opzioni di destinatario
                    entrato = true;
                    //scrivo il menu
                    menuDestinatari();
                    String opzioneDestinatario;
                    //scrivo l'opzione
                    opzioneDestinatario= tastiera.readLine();
                    switch(opzioneDestinatario){
                        // scrivo a tutti
                        case "1":
                            destinatarioArrayList.add("all");
                            mexInviato.setDestinatario(destinatarioArrayList);
                            mexInviato.setComando("1");
                            break;
                        // scrivo ad una sola persona
                        case "2":
                            System.out.print("Inserisci il destinatario: ");
                            String destinatario = tastiera.readLine();
                            destinatarioArrayList.add(destinatario);
                            mexInviato.setDestinatario(destinatarioArrayList);
                            mexInviato.setComando("2");
                            break;
                        case "0":
                            entrato = false;
                            break;
                        default:
                            System.out.println("Opzione non esistente nella scelta del destinatario");
                            break;
                    }
                    if(entrato){
                        //invio il messaggio da inviare
                        System.out.print("Inserisci il messaggio: ");
                        // scrivo il corpo del messaggio
                        String corpo = tastiera.readLine();
                        mexInviato.setCorpo(corpo);
                        System.out.println("");
                    }
                    break;
                case "2":
                // richiedo la lista di utenti connessi
                    entrato = true;
                    destinatarioArrayList.add("Server");
                    mexInviato.setDestinatario(destinatarioArrayList);
                    mexInviato.setComando("-1");
                    break;
                case "3":
                // rispondi all'ultima persona che ti ha scritto
                //TODO
                    break;
                default:
                    System.out.println("Opzione non valida");
                    entrato = false;
                    break;
            }
        } while (!entrato);
        // invio il messaggio al server
        inviaMessaggio(mexInviato);
        //pulisco l'arraylist
        destinatarioArrayList.clear();
    }

    public static void threadRiceviMessaggio() throws IOException {
        Messaggio mexRicevuto = riceviMessaggio();
        // riscrivo il menu perche nel caso ricevo un messaggio so cosa devo scrivere
        // messaggio dal server
        System.out.println("");
        if (mexRicevuto.getMittente().equals("Server") && mexRicevuto.getComando().equals("0")) {
            // messaggio di sistema
            System.out.println(mexRicevuto.getCorpo());
        }
        // messaggio in broadcast da un altro client
        else if (mexRicevuto.getComando().equals("1")) {
            System.out.println(Colori.ANSI_GREEN + "Messaggio --> " + Colori.ANSI_RESET + mexRicevuto.getMittente() + " ha scritto a tutti: " + mexRicevuto.getCorpo());
        }
        // messaggio privato da un altro client
        else if (mexRicevuto.getComando().equals("2")) {
            System.out.println(Colori.ANSI_GREEN + "Messaggio --> " + Colori.ANSI_RESET + mexRicevuto.getMittente() + " ti ha scritto: " + mexRicevuto.getCorpo());
        }
        //lista degli utenti connessi
        else if (mexRicevuto.getComando().equals("-1")) {
            System.out.println("Lista dei client connessi: ");
            System.out.println(mexRicevuto.getCorpo());
        }
        menuOpzioni();
    }

    public static void menuDestinatari() throws IOException {
        System.out.println("");
        System.out.println(Colori.ANSI_RED + "----------Menu-Messaggio----------" + Colori.ANSI_RESET);
        System.out.println(Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 0 --> Esci                     "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET +'\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 1 --> Scrivere a tutti         "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET +'\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 2 --> Scrivere ad una persona  " + Colori.ANSI_RED + "|"
                + Colori.ANSI_RESET);
        System.out.println(Colori.ANSI_RED + "----------------------------------" + Colori.ANSI_RESET);
        System.out.print("Seleziona l'opzione per il destinatario: ");
    }

    public static void menuOpzioni(){
        System.out.println("");
        System.out.println(Colori.ANSI_RED + "---------------------Menu---------------------" + Colori.ANSI_RESET);
        System.out.println(Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 1 --> Scrivere un messaggio                "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET +'\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 2 --> Richiedo la lista di utenti connessi " + Colori.ANSI_RED + "|"
                + Colori.ANSI_RESET);
        System.out.println(Colori.ANSI_RED + "----------------------------------------------" + Colori.ANSI_RESET);
        System.out.print("Seleziona un'opzione: ");
    }
}
