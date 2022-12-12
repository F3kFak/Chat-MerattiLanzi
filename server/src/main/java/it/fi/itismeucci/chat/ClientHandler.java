package it.fi.itismeucci.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.security.auth.login.LoginContext;

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


    private String nomeUtente;
    private Socket socket;
    private BufferedReader input;
    private DataOutputStream output;
    private ObjectMapper objectMapper;
    private Messaggio mexInviato;
    private String mexRicevuto;
    private Messaggio utente;

    public ClientHandler(){
        this.input = new BufferedReader(input);
        this.output = new DataOutputStream(output);
        this.objectMapper = new ObjectMapper();
        this.mexInviato = new Messaggio();
        this.utente = new Messaggio();
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    @Override
    public void run() {
        boolean exists = false;
        do {
            //ricevo il messaggio e lo deserializzo
            utente = riceviMessaggio(input.readLine());
            for (ClientHandler c : ServerStr.listaClient) {
                //controllo che il nome utente non sia esistente
                if(utente.getMittente().equals(c.getNomeUtente())){
                    exists = true;
                    mexInviato.setCorpo("Connessione riufutata, client già esistente");
                }
            }
        } while (exists);


    /* 
        1) Login

        do
            leggi nome utente 

            invia Messaggio

            ricevimessaggio

        while ricevuto ok

        this.nomeUtente = .....

        client va aggiunto alla lista


        ClientHandler.listaClient.add(this);


        // chat

        ricevimessaggio

        switch (tipo) {

            scrivi a tutti

            per tutti i client ClientHandler.listaClient c
            

            // si sta utilizzando l'istanza che gestisce i vari clien
            // utilizzanod il thread del client che ha inviato il messaggio
            c.inviaMessaggio


            -----------------------

            scrivi a uno

            per tutti i client ClientHandler.listaClient c
            cercando che c.nomeUtente = al destinatario
            c.invia messaggio

            

*/
        }


        






        //ciclo+


    public void inviaMessaggio(Messaggio mexInviato) throws JsonProcessingException{
        //serializzo
        String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
        //scrivo il messaggio
        output.writeBytes(stringaSerializzata + '\n');
    }

    public Messaggio riceviMessaggio(String mexRicevuto) {
        //deserializzo
        Messaggio stringaDeserializzata = objectMapper.readValue(mexRicevuto, Messaggio.class);
        //ritorno il l'istanza
        return stringaDeserializzata;
    }

    /*
    public String serializzazionemex(Messaggio mexInviato) throws JsonProcessingException{
        String stringaSerializzata = objectMapper.writeValueAsString(mexInviato);
        return stringaSerializzata;
    }

    public Messaggio deserializzazionemex(String mexRicevuto) throws JsonMappingException, JsonProcessingException{

        Messaggio stringaDeserializzata = objectMapper.readValue(mexRicevuto, Messaggio.class);
        return stringaDeserializzata;
    }
    */
}






Thread sia un istanza di oggetto che un thread
