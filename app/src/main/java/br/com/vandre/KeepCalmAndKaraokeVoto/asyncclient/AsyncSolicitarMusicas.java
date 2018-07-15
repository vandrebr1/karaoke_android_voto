package br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_DESCONECTAR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_MUSICAS;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.MSG_DESCONECTADO;

public class AsyncSolicitarMusicas extends android.os.AsyncTask<Void, String, String> {
    private String TAG;
    private ConnectionHandler connectionHandler;
    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private ProgressDialog load;
    Context context;
    private int port;
    private int timeout;
    private String url;

    public AsyncSolicitarMusicas(final String url, final int port, final int timeout, final ConnectionHandler connectionHandler, Context context, final Socket socket) {
        this.TAG = this.getClass().getName();
        this.url = url;
        this.port = port;
        this.timeout = timeout;
        this.connectionHandler = connectionHandler;
        this.socket = socket;
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: " + Thread.currentThread().getName());
        load = ProgressDialog.show(context, "Por favor Aguarde ...",
                "Listando ...");
    }

    public String doInBackground(Void... params) {
        String retorno = null;
        try {
            socket.connect(new InetSocketAddress(url, port), timeout);

            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            this.write(KEY_MUSICAS + " ");
            retorno = in.readLine();

            disconnect();
        } catch (UnknownHostException ex) {
            Log.e(TAG, "doInBackground(): " + ex.toString());
            retorno = ex.toString();
        } catch (IOException ex) {
            Log.d(TAG, "doInBackground(): " + ex.toString());
            retorno = ex.toString();
        } catch (Exception ex) {
            Log.e(TAG, "doInBackground(): " + ex.toString());
            retorno = ex.toString();
        }
        return retorno;
    }

    public void onPostExecute(final String ex) {
        super.onPostExecute(ex);
        this.connectionHandler.didReceiveData(ex);
        load.dismiss();
    }


    public void write(final String data) {
        try {
            Log.d(TAG, "write(): data = " + data);
            out.write(data + "\n");
            out.flush();
        } catch (IOException ex) {
            Log.e(TAG, "write(): " + ex.toString());
        } catch (NullPointerException ex) {
            Log.e(TAG, "write(): " + ex.toString());
        }
    }

    public void disconnect() {
        try {
            Log.d(TAG, "Closing the socket connection.");
            write(KEY_DESCONECTAR + MSG_DESCONECTADO);
            if (socket != null) {
                socket.close();
            }
            if (out != null & in != null) {
                out.close();
                in.close();
            }
        } catch (IOException ex) {
            Log.e(TAG, "disconnect(): " + ex.toString());
            publishProgress(ex.toString());
        }
    }
}