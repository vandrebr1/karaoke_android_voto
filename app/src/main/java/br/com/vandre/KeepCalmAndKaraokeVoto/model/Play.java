package br.com.vandre.KeepCalmAndKaraokeVoto.model;

import java.io.Serializable;
import java.util.Objects;

public class Play implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Musica musica;
    private Cantor cantor1;
    private Cantor cantor2;
    private boolean batalha;
    private String dtcadastro;
    private boolean votacaoEncerrada;
    private boolean concluida;

    public Play() {

    }

    public Play(Integer id) {
        this.id = id;
    }

    public Play(Integer id, Musica musica, Cantor cantor1, Cantor cantor2, boolean batalha, String dtcadastro, boolean votacaoEncerrada, boolean concluida) {
        this.id = id;
        this.musica = musica;
        this.cantor1 = cantor1;
        this.cantor2 = cantor2;
        this.batalha = batalha;
        this.dtcadastro = dtcadastro;
        this.votacaoEncerrada = votacaoEncerrada;
        this.concluida = concluida;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public String getDtcadastro() {
        return dtcadastro;
    }

    public void setDtcadastro(String dtcadastro) {
        this.dtcadastro = dtcadastro;
    }

    public Cantor getCantor1() {
        return cantor1;
    }

    public void setCantor1(Cantor cantor1) {
        this.cantor1 = cantor1;
    }

    public Cantor getCantor2() {
        return cantor2;
    }

    public void setCantor2(Cantor cantor2) {
        this.cantor2 = cantor2;
    }

    public boolean isBatalha() {
        return batalha;
    }

    public void setBatalha(boolean batalha) {
        this.batalha = batalha;
    }

    public boolean isVotacaoEncerrada() {
        return votacaoEncerrada;
    }

    public void setVotacaoEncerrada(boolean votacaoEncerrada) {
        this.votacaoEncerrada = votacaoEncerrada;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Play play = (Play) o;
        return batalha == play.batalha &&
                votacaoEncerrada == play.votacaoEncerrada &&
                concluida == play.concluida &&
                Objects.equals(id, play.id) &&
                Objects.equals(musica, play.musica) &&
                Objects.equals(cantor1, play.cantor1) &&
                Objects.equals(cantor2, play.cantor2) &&
                Objects.equals(dtcadastro, play.dtcadastro);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, musica, cantor1, cantor2, batalha, dtcadastro, votacaoEncerrada, concluida);
    }

    @Override
    public String toString() {
        return "Play{" +
                "id=" + id +
                ", musica=" + musica +
                ", cantor1=" + cantor1 +
                ", cantor2=" + cantor2 +
                ", batalha=" + batalha +
                ", dtcadastro='" + dtcadastro + '\'' +
                ", votacaoEncerrada=" + votacaoEncerrada +
                ", concluida=" + concluida +
                '}';
    }
}
