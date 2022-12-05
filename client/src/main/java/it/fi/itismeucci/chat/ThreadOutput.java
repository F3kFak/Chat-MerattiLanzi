package it.fi.itismeucci.chat;

public class ThreadOutput extends Thread {
    Comunica comunica;
    public ThreadOutput(Comunica comunica){
        this.comunica = comunica;
    }

    public void run(){
        try {
            comunica.invio();
            for (;;) {
                comunica.invio();
            }
        } catch (Exception e) {}
    }
}
