package it.fi.itismeucci.chat;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ServerStr servente = new ServerStr();
        try {
            servente.avvia();
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }
}
