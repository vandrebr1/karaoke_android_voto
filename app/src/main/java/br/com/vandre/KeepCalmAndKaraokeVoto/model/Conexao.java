package br.com.vandre.KeepCalmAndKaraokeVoto.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_ENDERECOSERVIDOR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_KEY;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_PORTASERVIDOR;

public class Conexao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String enderecoServidor;
    private Integer portaServidor;
    Context context;

    public Conexao(Context context) {
        this.context = context;
        carregarConfiguracao();
    }

    public String getEnderecoServidor() {
        return enderecoServidor;
    }

    public void setEnderecoServidor(String enderecoServidor) {
        this.enderecoServidor = enderecoServidor;
    }

    public Integer getPortaServidor() {
        return portaServidor;
    }

    public void setPortaServidor(Integer portaServidor) {
        this.portaServidor = portaServidor;
    }

    private void carregarConfiguracao() {

        final SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        enderecoServidor = sharedPreferences.getString(PREF_ENDERECOSERVIDOR, "");
        portaServidor = sharedPreferences.getInt(PREF_PORTASERVIDOR, 0);
    }

    public void salvarConfiguracao() {

        final SharedPreferences.Editor edit = context.getSharedPreferences(PREF_KEY, 0).edit();
        edit.putString(PREF_ENDERECOSERVIDOR, enderecoServidor);
        edit.putInt(PREF_PORTASERVIDOR, portaServidor);
        edit.apply();

    }
}
