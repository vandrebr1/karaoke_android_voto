package br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AsyncWriter extends android.os.AsyncTask<String, String, String> {
    private String TAG;
    private ConnectionHandler connectionHandler;
    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;

    public AsyncWriter(final ConnectionHandler connectionHandler, final Socket socket) {
        this.TAG = this.getClass().getName();
        this.connectionHandler = connectionHandler;
        this.socket = socket;
    }

    public String doInBackground(final String... params) {
        String retorno = null;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.write(params[0] + params[1]);

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
}