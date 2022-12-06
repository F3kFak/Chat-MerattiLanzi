package it.fi.itismeucci.chat;

public class App 
{
    public static void main( String[] args )
    {
        ClientStr cliente = new ClientStr();
        try {
            cliente.comunica();
        } catch (Exception e) {
            System.out.println("Errore durante l'avvio del client");
            System.out.println("Errore: " + e);
        }
    }
}
