package it.fi.itismeucci.chat;

public class App 
{
    public static void main( String[] args )
    {
        try {
            ClientStr cliente = new ClientStr();


            cliente.registra();
            
            cliente.comunica();
        
        
        } catch (Exception e) {
            System.out.println("Errore durante l'avvio del client");
            System.out.println("Errore: " + e);
        }
    }
}
