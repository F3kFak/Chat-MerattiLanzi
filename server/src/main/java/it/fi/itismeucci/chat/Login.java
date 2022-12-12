package it.fi.itismeucci.chat;

import java.util.Objects;

public class Login {
    private String utente;


    public Login() {
    }

    public Login(String utente) {
        this.utente = utente;
    }

    public String getUtente() {
        return this.utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public Login utente(String utente) {
        setUtente(utente);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Login)) {
            return false;
        }
        Login login = (Login) o;
        return Objects.equals(utente, login.utente);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(utente);
    }

    @Override
    public String toString() {
        return "{" +
            " utente='" + getUtente() + "'" +
            "}";
    }

}
