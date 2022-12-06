package it.fi.itismeucci.chat;

public class App 
{
    public static void main( String[] args )
    {
        ServerStr servente = new ServerStr();
        try {
            servente.avvia();
        } catch (Exception e) {
            System.out.println("Errore durante l'avvio del server");
            System.out.println("Errore: " + e);
        }
        
    }
}
