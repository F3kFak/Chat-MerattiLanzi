package it.fi.itismeucci.chat;

import java.io.*;

public class ClientStr extends Comunica{
    
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
        do
            System.out.println("Inserisci nome:");
            stringaUtente = tastiera.readLine();

            //serializzare in una classe
            outVersoServer.writeBytes(stringaUtente + '\n');

            //leggi la risposta (deserilizza)
            

        while non ok
        
    }

}
