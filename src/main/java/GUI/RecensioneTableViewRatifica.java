package GUI;

public class RecensioneTableViewRatifica {

    private String nomeStruttura,nickname,indirizzo,citta;

    public RecensioneTableViewRatifica(String nomeStruttura, String nickname, String indirizzo, String citta) {
        this.nomeStruttura = nomeStruttura;
        this.nickname = nickname;
        this.indirizzo = indirizzo;
        this.citta = citta;
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

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }
}
