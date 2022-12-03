package it.fi.itismeucci.chat;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ClientStr cliente = new ClientStr();
        try {
            cliente.comunica();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
