package br.com.vandre.KeepCalmAndKaraokeVoto.model;

import java.io.Serializable;
import java.util.Objects;

public class Cantor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;

    private String apelido;

    public Cantor() {
    }

    public Cantor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cantor cantor = (Cantor) o;
        return Objects.equals(id, cantor.id) &&
                Objects.equals(nome, cantor.nome) &&
                Objects.equals(apelido, cantor.apelido);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nome, apelido);
    }
}
