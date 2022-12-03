package it.fi.itismeucci.chat;

import java.io.*;
import java.net.*;

public class ClientStr {
    
    public void comunica() throws IOException {
        Comunica C1 = new Comunica();
        ThreadInput tin = new ThreadInput(C1);
        ThreadOutput to = new ThreadOutput(C1);

        to.start();
        tin.start();
    }

}
