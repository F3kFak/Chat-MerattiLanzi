package it.fi.itismeucci.chat;

public class ThreadInput extends Thread{
    Comunica comunica;
    public ThreadInput(Comunica comunica){
        this.comunica = comunica;
    }

    public void run(){
        try {
<<<<<<< HEAD
            comunica.ricezione();
=======
            for (;;) {
                comunica.ricezione();
            }
>>>>>>> risolto chat comunicazione tra client
        } catch (Exception e) {}
    }
}
