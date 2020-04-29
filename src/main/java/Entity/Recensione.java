package Entity;

public class Recensione {

    private int idRecensione;
    private String nomeStruttura,nickname,indirizzo,nomeCitta,testoRecensione;

    public Recensione(int idRecensione, String nomeStruttura, String nickname, String indirizzo, String nomeCitta, String testoRecensione) {
        this.idRecensione = idRecensione;
        this.nomeStruttura = nomeStruttura;
        this.nickname = nickname;
        this.indirizzo = indirizzo;
        this.nomeCitta = nomeCitta;
        this.testoRecensione = testoRecensione;
    }

    public int getIdRecensione() {
        return idRecensione;
    }

    public void setIdRecensione(int idRecensione) {
        this.idRecensione = idRecensione;
    }

    public String getNomeStruttura() {
        return nomeStruttura;
    }

    public void setNomeStruttura(String nomeStruttura) {
        this.nomeStruttura = nomeStruttura;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getNomeCitta() {
        return nomeCitta;
    }

    public void setNomeCitta(String nomeCitta) {
        this.nomeCitta = nomeCitta;
    }

    public String getTestoRecensione() {
        return testoRecensione;
    }

    public void setTestoRecensione(String testoRecensione) {
        this.testoRecensione = testoRecensione;
    }
}
