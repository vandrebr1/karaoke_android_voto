package br.com.vandre.KeepCalmAndKaraokeVoto.model;

import java.io.Serializable;
import java.util.Objects;

public class Pontuacao implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private int playID;
    private int cantorID;
    private int ponto;
    private String imei;
    private String jurado;

    public Pontuacao() {
    }

    public Pontuacao(Integer id) {
        this.id = id;
    }

    public Pontuacao(Integer id, int playID, int cantorID, int ponto, String imei, String jurado) {
        this.id = id;
        this.playID = playID;
        this.cantorID = cantorID;
        this.ponto = ponto;
        this.imei = imei;
        this.jurado = jurado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPlayID() {
        return playID;
    }

    public void setPlayID(int playID) {
        this.playID = playID;
    }

    public int getCantorID() {
        return cantorID;
    }

    public void setCantorID(int cantorID) {
        this.cantorID = cantorID;
    }

    public int getPonto() {
        return ponto;
    }

    public void setPonto(int ponto) {
        this.ponto = ponto;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getJurado() {
        return jurado;
    }

    public void setJurado(String jurado) {
        this.jurado = jurado;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pontuacao pontuacao = (Pontuacao) o;
        return playID == pontuacao.playID &&
                cantorID == pontuacao.cantorID &&
                ponto == pontuacao.ponto &&
                Objects.equals(id, pontuacao.id) &&
                Objects.equals(imei, pontuacao.imei) &&
                Objects.equals(jurado, pontuacao.jurado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, playID, cantorID, ponto, imei, jurado);
    }

    @Override
    public String toString() {
        return "Pontuacaoimplements{" +
                "id=" + id +
                ", playID=" + playID +
                ", cantorID=" + cantorID +
                ", ponto=" + ponto +
                ", imei='" + imei + '\'' +
                ", jurado='" + jurado + '\'' +
                '}';
    }
}
