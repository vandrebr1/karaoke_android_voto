package br.com.vandre.KeepCalmAndKaraokeVoto.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dispositivoID;
    private String imei;

    public Usuario(String paramString1, String paramString2) {
        this.dispositivoID = paramString1;
        this.imei = paramString2;
    }

    public String getDispositivoID() {
        return dispositivoID;
    }

    public void setDispositivoID(String dispositivoID) {
        this.dispositivoID = dispositivoID;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
