package Entity;


public class Utente {
    
    private String nickname,email,nome,cognome,stato,nomePubblico;
    private float media;
    private int numeroRecensioni;

    public Utente() {}

    public Utente(String nickname, String email, String nome, String cognome, String stato, String nomePubblico, float media, int numeroRecensioni) {
        this.nickname = nickname;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.stato = stato;
        this.nomePubblico = nomePubblico;
        this.media = media;
        this.numeroRecensioni = numeroRecensioni;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getNomePubblico() {
        return nomePubblico;
    }

    public void setNomePubblico(String nomePubblico) {
        this.nomePubblico = nomePubblico;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public int getNumeroRecensioni() {
        return numeroRecensioni;
    }

    public void setNumeroRecensioni(int numeroRecensioni) {
        this.numeroRecensioni = numeroRecensioni;
    }
    
}
