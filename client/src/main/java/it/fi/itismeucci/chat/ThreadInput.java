package it.fi.itismeucci.chat;

public class ThreadInput extends Thread{
    Comunica comunica;
    public ThreadInput(Comunica comunica){
        this.comunica = comunica;
    }

    public void run(){
        try {
            for (;;) {
                ClientStr.inviaMessaggio();
            }
        } catch (Exception e) {}
    }
}
