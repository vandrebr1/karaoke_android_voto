package br.com.vandre.KeepCalmAndKaraokeVoto.app;

import android.app.Application;

import java.net.Socket;

public class KaraokeAplicacao extends Application {

    private Socket socket;
    private String imei;
    private String jurado;

    public KaraokeAplicacao() {
    }

    public Socket getSocket() {

        if (this.socket == null) {
            this.socket = new Socket();
        } else if (this.socket.isClosed()) {
            this.socket = new Socket();
        }
        return this.socket;
    }

    public void setSocket(Socket paramSocket) {
        this.socket = paramSocket;
    }

}
