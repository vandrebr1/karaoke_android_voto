package br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_CONECTAR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_DESCONECTAR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.MSG_CONECTADO;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.MSG_CONECTANDO;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.MSG_DESCONECTADO;

public class AsyncConnection extends android.os.AsyncTask<Void, String, Exception> {
    private String TAG;
    private ConnectionHandler connectionHandler;
    private BufferedReader in;
    private boolean interromperConexao;
    private BufferedWriter out;
    private int port;
    private Socket socket;
    private int timeout;
    private String url;

    public AsyncConnection(final String url, final int port, final int timeout, final ConnectionHandler connectionHandler, final Socket socket) {
        this.TAG = this.getClass().getName();
        this.url = url;
        this.port = port;
        this.timeout = timeout;
        this.connectionHandler = connectionHandler;
        this.socket = socket;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        connectionHandler.didReceiveData(values[0]);
    }

    @Override
    protected void onPostExecute(Exception result) {
        super.onPostExecute(result);
        Log.d(TAG, "Finished communication with the socket. Result = " + result);
        connectionHandler.didDisconnect(result);
    }

    @Override
    protected Exception doInBackground(Void... params) {
        Exception error = null;

        try {
            publishProgress(MSG_CONECTANDO);
            socket.connect(new InetSocketAddress(url, port), timeout);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            write(KEY_CONECTAR + MSG_CONECTADO);
            connectionHandler.didConnect();

            if (!interromperConexao) {
                while (!interromperConexao) {
                    String line = in.readLine();
                    publishProgress(line);
                }
            } else {
                String line = in.readLine();
                publishProgress(line);
            }

            disconnect();
        } catch (UnknownHostException ex) {
            Log.e(TAG, "doInBackground(): " + ex.toString());
            error = ex;
        } catch (IOException ex) {
            Log.d(TAG, "doInBackground(): " + ex.toString());
            error = ex;
        } catch (Exception ex) {
            Log.e(TAG, "doInBackground(): " + ex.toString());
            error = ex;
        } finally {
            try {
                socket.close();
                out.close();
                in.close();
            } catch (Exception ex) {
                publishProgress(error.toString());
            }
        }

        return error;
    }

    public void write(final String data) {
        try {

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

    public boolean isInterromperConexao() {
        return interromperConexao;
    }

    public void setInterromperConexao(boolean interromperConexao) {
        this.interromperConexao = interromperConexao;
    }
}