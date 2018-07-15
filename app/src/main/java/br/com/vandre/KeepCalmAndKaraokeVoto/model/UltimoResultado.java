package br.com.vandre.KeepCalmAndKaraokeVoto.model;

public class UltimoResultado {

    String apelidoCantor;
    Double nota;

    public UltimoResultado(String apelidoCantor, Double nota) {
        this.apelidoCantor = apelidoCantor;
        this.nota = nota;
    }

    public String getApelidoCantor() {
        return apelidoCantor;
    }

    public void setApelidoCantor(String apelidoCantor) {
        this.apelidoCantor = apelidoCantor;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }


}
