package it.fi.itismeucci.chat;

public class ThreadInput extends Thread{
    ClientStr clientStr;
    public ThreadInput(ClientStr clientstr){
        this.clientStr = clientstr;
    }

    public void run(){
        try {
            for (;;) {
                ClientStr.threadInviaMessaggio();
            }
        } catch (Exception e) {}
    }
}
