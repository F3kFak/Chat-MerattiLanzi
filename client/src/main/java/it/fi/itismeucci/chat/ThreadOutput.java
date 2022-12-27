package it.fi.itismeucci.chat;

public class ThreadOutput extends Thread {
    ClientStr clientStr;
    public ThreadOutput(ClientStr clientstr){
        this.clientStr = clientstr;
    }

    public void run(){
        try {
            for (;;) {
                ClientStr.threadRiceviMessaggio();
            }
        } catch (Exception e) {}
    }
}
