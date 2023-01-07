package it.fi.itismeucci.chat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientStr {

    public static Socket miosocket;
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
    public static String ultimoDestinatario; // utlizzato per rispondere all'utlimo messaggio

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

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // chiusura client
                chiusuraClient();
                // invio il messaggio al server
                try {
                    inviaMessaggio(mexInviato);
                } catch (IOException e) {
                    //
                }
            }
        });
        do {
            //controllo se il nome non è valido
            do{
                System.out.print("Inserisci il nome utente: ");
                nomeClient = tastiera.readLine();
                if(nomeClient.isEmpty() || nomeClient.isBlank() || nomeClient.equals("")){
                    System.out.println("Inserisci un nome valido");
                }
            }
            while(nomeClient.isEmpty() || nomeClient.isBlank() || nomeClient.equals(""));
            // serializza in una classe il nome
            mexInviato.setMittente(nomeClient);
            inviaMessaggio(mexInviato);
            // leggi la risposta (deserilizzata)
            mexRicevuto = riceviMessaggio();
            // controllo se sono entrato a far parte della chat
            if (mexRicevuto.getMittente().equals("Server") && mexRicevuto.getCorpo().equals("entrato"))
                entrato = false;
            else{
                System.out.println(mexRicevuto.getCorpo());
                entrato = true;
            }
        } while (entrato);
        System.out.println("Entrato nella chat");
        menuOpzioni();
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
        boolean entratoMenuOpzioni = false;
        boolean entratoMenuDestinatario = false;
        do {
            // seleziona l'opzione del destinatario
            opzione = tastiera.readLine();
            switch (opzione) {
                case "1":
                    // scrivo un messaggio ad altri client
                    String opzioneDestinatario;
                    do {
                        // imposto che sono entrato in entrambi i menu, all'occorenza imposto il
                        // contrario
                        entratoMenuOpzioni = true;
                        entratoMenuDestinatario = true;
                        // scrivo il menu
                        menuDestinatari();
                        // l'utente scrive l'opzione
                        opzioneDestinatario = tastiera.readLine();
                        switch (opzioneDestinatario) {
                            case "0":
                                // esco dal menu destinatario e quindi continuo il loop del primo do while
                                entratoMenuOpzioni = false;
                                menuOpzioni();
                                break;
                            case "1":
                                // scrivo a tutti
                                destinatarioArrayList.add("all");
                                mexInviato.setDestinatario(destinatarioArrayList);
                                mexInviato.setComando("1");
                                break;
                            case "2":
                                // scrivo ad una sola persona
                                System.out.print("Inserisci il destinatario: ");
                                String destinatario = tastiera.readLine();
                                destinatarioArrayList.add(destinatario);
                                mexInviato.setDestinatario(destinatarioArrayList);
                                mexInviato.setComando("2");
                                break;
                            case "3":
                                // rispondi all'ultima persona che ti ha scritto
                                if (ultimoDestinatario == null) {
                                    System.out.println("Nessuno ti ha ancora scritto. \n" +
                                            "Qualche volta devi essere te a fare il primo passo. :P");
                                    entratoMenuDestinatario = false;
                                } else {
                                    System.out.println("Stai rispondendo a " + ultimoDestinatario);
                                    destinatarioArrayList.add(ultimoDestinatario);
                                    mexInviato.setDestinatario(destinatarioArrayList);
                                    mexInviato.setComando("risposta");
                                }
                                break;
                            default:
                                entratoMenuOpzioni = false;
                                entratoMenuDestinatario = false;
                                System.out.println("Opzione non esistente nella scelta del destinatario");
                                break;

                        }
                    } while (!entratoMenuDestinatario && !opzioneDestinatario.equals("0"));
                    if (entratoMenuOpzioni) {
                        // invio il messaggio da inviare
                        System.out.print("Inserisci il messaggio: ");
                        // scrivo il corpo del messaggio
                        String corpo = tastiera.readLine();
                        mexInviato.setCorpo(corpo);
                        System.out.println("");
                        menuOpzioni();
                    }
                    break;
                case "2":
                    // richiedo la lista di utenti connessi
                    entratoMenuOpzioni = true;
                    destinatarioArrayList.add("Server");
                    mexInviato.setDestinatario(destinatarioArrayList);
                    mexInviato.setComando("-1");
                    break;
                case "3":
                    // chiusura client
                    entratoMenuOpzioni = true;
                    chiusuraClient();
                    break;
                default:
                    System.out.println("Opzione non valida");
                    entratoMenuOpzioni = false;
                    menuOpzioni();
                    break;
            }
        } while (!entratoMenuOpzioni);
        // invio il messaggio al server
        inviaMessaggio(mexInviato);
        // pulisco l'arraylist di destinatari
        destinatarioArrayList.clear();
    }

    public static void threadRiceviMessaggio() throws IOException {
        Messaggio mexRicevuto = riceviMessaggio();
        // riscrivo il menu perche nel caso ricevo un messaggio so cosa devo scrivere
        // messaggio dal server
        System.out.println("\n");
        if (mexRicevuto.getMittente().equals("Server") && mexRicevuto.getComando().equals("0")) {
            // messaggio di sistema
            System.out.println(mexRicevuto.getCorpo());
        }
        // messaggio in broadcast da un altro client
        else if (mexRicevuto.getComando().equals("1")) {
            System.out.println(Colori.ANSI_GREEN + "Messaggio --> " + Colori.ANSI_RESET + mexRicevuto.getMittente()
                    + " ha scritto a tutti: " + mexRicevuto.getCorpo());
        }
        // messaggio privato da un altro client
        else if (mexRicevuto.getComando().equals("2")) {
            if (mexRicevuto.getMittente().equals(nomeClient)) {
                System.out.println(Colori.ANSI_GREEN + "Messaggio --> " + Colori.ANSI_RESET
                        + " hai scritto a te stesso: " + mexRicevuto.getCorpo());
            } else {
                System.out.println(Colori.ANSI_GREEN + "Messaggio --> " + Colori.ANSI_RESET + mexRicevuto.getMittente()
                        + " ti ha scritto: " + mexRicevuto.getCorpo());
                ultimoDestinatario = mexRicevuto.getMittente();
            }
        }
        // lista degli utenti connessi
        else if (mexRicevuto.getComando().equals("-1")) {
            System.out.println("Lista dei client connessi: ");
            System.out.println(mexRicevuto.getCorpo());
        } else if (mexRicevuto.getComando().equals("4")) {
            System.out.println("chiusura client");
            miosocket.close();
            System.exit(1);
        } else if (mexRicevuto.getComando().equals("chiusura")) {
            System.out.println(Colori.ANSI_YELLOW + "-> " + mexRicevuto.getMittente() + " <- " + " si e' Disconnesso."
                    + Colori.ANSI_RESET);
        } else if (mexRicevuto.getComando().equals("risposta"))
            System.out.println(Colori.ANSI_GREEN + "Messaggio --> " + Colori.ANSI_RESET + mexRicevuto.getMittente()
            + " ti ha risposto: " + mexRicevuto.getCorpo());
        menuOpzioni();
    }

    public static void chiusuraClient(){
        destinatarioArrayList.add("Server");
        mexInviato.setDestinatario(destinatarioArrayList);
        mexInviato.comando("4");
    }

    public static void menuDestinatari() throws IOException {
        System.out.println("");
        System.out.println(Colori.ANSI_RED +                  "------------------Menu-Messaggio----------------" + Colori.ANSI_RESET);
        System.out.println(
                  Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 0 --> Esci                                   "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + '\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 1 --> Scrivere a tutti                       "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + '\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 2 --> Scrivere ad una persona                "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + '\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 3 --> Rispondi all'ultima persona in privato "
                + Colori.ANSI_RESET + Colori.ANSI_RED + "|");
        System.out.println(Colori.ANSI_RED +                 "------------------------------------------------" + Colori.ANSI_RESET);
        System.out.print("Seleziona l'opzione per il destinatario: ");
    }

    public static void menuOpzioni() {
        System.out.println("");
        System.out.println(Colori.ANSI_RED +                  "---------------------Menu---------------------" + Colori.ANSI_RESET);
        System.out.println(
                  Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 1 --> Scrivere un messaggio                "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + '\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 2 --> Richiedo la lista di utenti connessi "
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + '\n'
                + Colori.ANSI_RED + "|" + Colori.ANSI_RESET + " 3 --> Chiudi la connessione                "
                + Colori.ANSI_RESET + Colori.ANSI_RED + "|");
        System.out.println(Colori.ANSI_RED +                 "----------------------------------------------" + Colori.ANSI_RESET);
        System.out.print("Seleziona un'opzione: ");
    }

}
