package it.fi.itismeucci.chat;

import java.util.ArrayList;
import java.util.Objects;

public class Messaggio {
    private String mittente;
    private String comando;
    private ArrayList<String> destinatario;
    private String corpo;

    public Messaggio() {
    }

    public Messaggio(String mittente, String comando, ArrayList<String> destinatario, String corpo) {
        this.mittente = mittente;
        this.comando = comando;
        this.destinatario = destinatario;
        this.corpo = corpo;
    }

    public String getMittente() {
        return this.mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public String getComando() {
        return this.comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public ArrayList<String> getDestinatario() {
        return this.destinatario;
    }

    public void setDestinatario(ArrayList<String> destinatario) {
        this.destinatario = destinatario;
    }

    public String getCorpo() {
        return this.corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public Messaggio mittente(String mittente) {
        setMittente(mittente);
        return this;
    }

    public Messaggio comando(String comando) {
        setComando(comando);
        return this;
    }

    public Messaggio destinatario(ArrayList<String> destinatario) {
        setDestinatario(destinatario);
        return this;
    }

    public Messaggio corpo(String corpo) {
        setCorpo(corpo);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Messaggio)) {
            return false;
        }
        Messaggio messaggio = (Messaggio) o;
        return Objects.equals(mittente, messaggio.mittente) && Objects.equals(comando, messaggio.comando) && Objects.equals(destinatario, messaggio.destinatario) && Objects.equals(corpo, messaggio.corpo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mittente, comando, destinatario, corpo);
    }

    @Override
    public String toString() {
        return "{" +
            " mittente='" + getMittente() + "'" +
            ", comando='" + getComando() + "'" +
            ", destinatario='" + getDestinatario() + "'" +
            ", corpo='" + getCorpo() + "'" +
            "}";
    }

}
