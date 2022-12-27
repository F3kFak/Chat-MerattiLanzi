package it.fi.itismeucci.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Client handler del server
 * 
 * gestisce il client dal punto di vista del server
 * 
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private String nomeUtente;
    private BufferedReader input;
    private DataOutputStream output;
    private ObjectMapper objectMapper;
    private Messaggio mexInviato;
    private Messaggio mexRicevuto;
    private Messaggio utente;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new DataOutputStream(socket.getOutputStream());
        this.objectMapper = new ObjectMapper();
        this.mexInviato = new Messaggio();
        this.utente = new Messaggio();
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    @Override
    public void run() {
        // si effettua la registrazione
        try {
            registrazione(socket);
        } catch (IOException e) {
            System.out.println(e);
            messaggioErrore("Errore di registrazione");
        }
        //si notifica del nuovo client in chat
        try{
            notificaClients(socket);
        }
        catch(Exception e){
            System.out.println(e);
            messaggioErrore("Errore nella notificazione del nuovo client");
        }
        //rivevi messaggi
        try{
            for(;;)
            riceviMessaggio(input.readLine());
        }
        catch(Exception e){
            System.out.println(e);
            messaggioErrore("Nella ricezione del messaggio");
        }
        /*
         *  
         * // chat
         * 
         * ricevimessaggio
         * 
         * switch (tipo) {
         * 
         * scrivi a tutti
         * 
         * per tutti i client ClientHandler.listaClient c
         * 
         * 
         * // si sta utilizzando l'istanza che gestisce i vari clien
         * // utilizzanod il thread del client che ha inviato il messaggio
         * c.inviaMessaggio
         * 
         * 
         * -----------------------
         * 
         * scrivi a uno
         * 
         * per tutti i client ClientHandler.listaClient c
         * cercando che c.nomeUtente = al destinatario
         * c.invia messaggio
         */
    }

    public void registrazione(Socket socket) throws IOException {
        boolean exists = false;
        do {
            // ricevo il messaggio e lo deserializzo
            utente = riceviMessaggioDes(input.readLine());
            System.out.println("Utente connesso come: " + utente.getMittente());
            for (ClientHandler c : ServerStr.listaClient) {
                // controllo che il nome utente non sia esistente
                if (utente.getMittente().equals(c.getNomeUtente())) {
                    // var impostata su true per ripetere il ciclo
                    exists = true;
                    // invio messaggio di errore
                    invioMessaggioServer("Connessione rifiutata, client già esistente");
                    break;
                }
            }
        } while (exists);
        // il client non è un doppione
        // aggiungo il client alla lista
        ServerStr.listaClient.add(this);
        // imposto il nome del client
        nomeUtente = utente.getMittente();
    }

    public void notificaClients(Socket socket) {
        // il client è entrato a far parte della chat e lo notifica
        for (ClientHandler c : ServerStr.listaClient) {
            try {
                c.invioMessaggioServer(nomeUtente + " si e' unito alla chat!" + '\n');
            } catch (IOException e) {
                messaggioErrore("Errore nell'invio del messaggio dal server");
            }
        }
    }

    public void invioMessaggioServer(String mex) throws IOException{
        mexInviato.setMittente("Server");
        mexInviato.setCorpo(mex);
        inviaMessaggio(mexInviato);
    }

    public void messaggioErrore(String err) {
        mexInviato.setMittente("Server");
        mexInviato.setCorpo(err);
        System.out.println(err);
    }

    public void inviaMessaggio(Messaggio mexInviato) throws IOException {
        // serializzo
        String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
        // scrivo il messaggio
        output.writeBytes(stringaSerializzata + '\n');
    }

    public Messaggio riceviMessaggioDes(String messaggioRicevuto) throws JsonMappingException, JsonProcessingException {
        // deserializzo
        Messaggio stringaDeserializzata = objectMapper.readValue(messaggioRicevuto, Messaggio.class);
        // ritorno il l'istanza
        return stringaDeserializzata;
    }

    public void riceviMessaggio(String messaggioRicevuto) throws JsonMappingException, JsonProcessingException{
        mexRicevuto = riceviMessaggioDes(messaggioRicevuto);
        if(mexRicevuto.getComando().equals(""))
    }

    /*
     * public String serializzazionemex(Messaggio mexInviato) throws
     * JsonProcessingException{
     * String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
     * return stringaSerializzata;
     * }
     * 
     * public Messaggio deserializzazionemex(String mexRicevuto) throws
     * JsonMappingException, JsonProcessingException{
     * 
     * Messaggio stringaDeserializzata = objectMapper.readValue(mexRicevuto,
     * Messaggio.class);
     * return stringaDeserializzata;
     * }
     */
}

// Thread sia un istanza di oggetto che un thread
