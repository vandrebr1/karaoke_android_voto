package br.com.vandre.KeepCalmAndKaraokeVoto.model;

import java.io.Serializable;
import java.util.Objects;

public class Musica implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String nomearquivooriginal;
    private String musica;
    private String cantor;
    private int nacional;
    private int desativada;

    public Musica() {
    }

    public Musica(Integer id) {
        this.id = id;
    }

    public Musica(Integer id, String nomearquivooriginal, String musica, String cantor, int nacional, int desativada) {
        this.id = id;
        this.nomearquivooriginal = nomearquivooriginal;
        this.musica = musica;
        this.cantor = cantor;
        this.nacional = nacional;
        this.desativada = desativada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomearquivooriginal() {
        return nomearquivooriginal;
    }

    public void setNomearquivooriginal(String nomearquivooriginal) {
        this.nomearquivooriginal = nomearquivooriginal;
    }

    public String getMusica() {
        return musica;
    }

    public void setMusica(String musica) {
        this.musica = musica;
    }

    public String getCantor() {
        return cantor;
    }

    public void setCantor(String cantor) {
        this.cantor = cantor;
    }

    public int getNacional() {
        return nacional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musica musica1 = (Musica) o;
        return nacional == musica1.nacional &&
                desativada == musica1.desativada &&
                Objects.equals(id, musica1.id) &&
                Objects.equals(nomearquivooriginal, musica1.nomearquivooriginal) &&
                Objects.equals(musica, musica1.musica) &&
                Objects.equals(cantor, musica1.cantor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nomearquivooriginal, musica, cantor, nacional, desativada);
    }

    @Override
    public String toString() {
        return "Musica{" +
                "id=" + id +
                ", nomearquivooriginal='" + nomearquivooriginal + '\'' +
                ", musica='" + musica + '\'' +
                ", cantor='" + cantor + '\'' +
                ", nacional=" + nacional +
                ", desativada=" + desativada +
                '}';
    }
}
