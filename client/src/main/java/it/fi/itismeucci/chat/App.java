package it.fi.itismeucci.chat;

public class App 
{
    public static void main( String[] args )
    {
        try {
            System.out.println("Client acceso...");
            ClientStr cliente = new ClientStr();
            cliente.connect();
            //effettua la registrazione
            cliente.registra();
            //effettuo la comunicazione
            cliente.comunica();
        } catch (Exception e) {
            System.out.println("Errore durante l'avvio del client");
            System.out.println("Errore: " + e);
        }
    }
}
