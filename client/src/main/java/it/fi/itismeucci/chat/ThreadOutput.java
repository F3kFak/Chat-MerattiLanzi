package it.fi.itismeucci.chat;

public class ThreadOutput extends Thread {
    Comunica comunica;
    public ThreadOutput(Comunica comunica){
        this.comunica = comunica;
    }

    public void run(){
        try {
<<<<<<< HEAD
            comunica.invio();
=======
            for (;;) {
                comunica.invio();
            }
>>>>>>> risolto chat comunicazione tra client
        } catch (Exception e) {}
    }
}
