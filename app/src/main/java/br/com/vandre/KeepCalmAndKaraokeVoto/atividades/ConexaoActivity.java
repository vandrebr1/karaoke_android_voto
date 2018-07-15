package br.com.vandre.KeepCalmAndKaraokeVoto.atividades;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import br.com.vandre.KeepCalmAndKaraokeVoto.R;
import br.com.vandre.KeepCalmAndKaraokeVoto.app.KaraokeAplicacao;
import br.com.vandre.KeepCalmAndKaraokeVoto.app.RequestConstantes;
import br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient.AsyncConnection;
import br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient.ConnectionHandler;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.Conexao;
import br.com.vandre.KeepCalmAndKaraokeVoto.util.Funcoes;

import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.CAMPO_LEITORCODIGOBARRAS;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_CONECTAR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.RESULTADO_CONEXAO_KEY;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.RequestConstantes.REQUEST_CODIGOBARRAS;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.RequestConstantes.REQUEST_PERMISSIONS_CAMERA;

public class ConexaoActivity extends AppCompatActivity implements ConnectionHandler {

    private RelativeLayout actConexao;
    private KaraokeAplicacao app;
    private AsyncConnection asyncConnection;
    private Button btnConectar;
    private Button btnLerQRCode;
    private EditText edtEnderecoServidor;
    private EditText edtPortaServidor;
    private boolean isClique;
    private TextView tvStatusServidor;
    private boolean conectado;
    private Conexao conexao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexao);
        this.iniciar();
    }

    private void carregarConfiguracao() {

        this.edtEnderecoServidor.setText(conexao.getEnderecoServidor());
        this.edtPortaServidor.setText(conexao.getPortaServidor().toString());
        this.edtEnderecoServidor.setSelection(this.edtEnderecoServidor.getText().length());
    }

    private void declarar() {
        this.app = (KaraokeAplicacao) this.getApplication();
        conexao = new Conexao(this);
        this.btnLerQRCode = this.findViewById(R.id.btnLeitorCodigoBarras);
        this.btnConectar = this.findViewById(R.id.btnConectar);
        this.tvStatusServidor = this.findViewById(R.id.tvStatusCenter);
        this.actConexao = this.findViewById(R.id.actConexao);
        this.edtEnderecoServidor = this.findViewById(R.id.edtEnderecoServidor);
        this.edtPortaServidor = this.findViewById(R.id.edtPortaServidor);
        btnConectar.setOnClickListener(new BtnConectarClickListener());

        this.btnLerQRCode.setOnClickListener(new BtnLerCodigoBarrasPedidoClickListener());

    }

    private void desabilitarHabilitar() {

        edtEnderecoServidor.setEnabled(!edtEnderecoServidor.isEnabled());
        edtPortaServidor.setEnabled(!edtPortaServidor.isEnabled());
        btnConectar.setEnabled(!btnConectar.isEnabled());
        btnLerQRCode.setEnabled(!btnLerQRCode.isEnabled());
    }

    private void iniciar() {
        this.declarar();
        carregarConfiguracao();
    }

    private class BtnLerCodigoBarrasPedidoClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isClique) {
                lerCodigoBarras(RequestConstantes.REQUEST_CODIGOBARRAS);
                isClique = true;
            }
        }
    }

    private void lerCodigoBarras(int requestCode) {

        if (ContextCompat.checkSelfPermission(ConexaoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConexaoActivity.this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(ConexaoActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS_CAMERA);

            }
        } else {
            Intent intent = new Intent(this, LeitorCodigoBarrasActivity.class);
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    lerCodigoBarras(REQUEST_CODIGOBARRAS);

                } else {
                    btnLerQRCode.setEnabled(false);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void salvarConfiguracao() {
        conexao.setEnderecoServidor(this.edtEnderecoServidor.getText().toString());
        conexao.setPortaServidor(Integer.parseInt(this.edtPortaServidor.getText().toString()));
        conexao.salvarConfiguracao();

    }

    private class BtnConectarClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            conectar();

        }
    }

    private void conectar(){
        salvarConfiguracao();
        desabilitarHabilitar();
        asyncConnection = new AsyncConnection(edtEnderecoServidor.getText().toString(),
                Integer.parseInt(edtPortaServidor.getText().toString()),
                5000,
                ConexaoActivity.this,
                app.getSocket());
        asyncConnection.setInterromperConexao(true);

        asyncConnection.execute();
    }

    public void didConnect() {

    }

    public void didDisconnect(final Exception result) {
        if (result != null) {
            desabilitarHabilitar();
        }
    }

    public void didReceiveData(final String data) {
        conectado = false;
        if (data != null) {
            if (!data.equals("")) {
                String messageFlag = data.substring(0, 1);
                if (messageFlag.equals(KEY_CONECTAR)) {
                    conectado = true;
                    this.tvStatusServidor.setText(data.substring(1));
                    onBackPressed();
                } else {
                    this.tvStatusServidor.setText(data);
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        isClique = false;
        boolean blnErro = false;

        if (resultCode == RESULT_OK) {
            if (data != null) {
                Barcode barcode;
                switch (requestCode) {
                    case RequestConstantes.REQUEST_CODIGOBARRAS:
                        barcode = data.getParcelableExtra(CAMPO_LEITORCODIGOBARRAS);
                        String retornoLeituraCodigoBarras = barcode.displayValue;
                        String[] split = retornoLeituraCodigoBarras.toString().split(":");
                        String ip = split[0].trim();
                        String porta = split[1].trim();

                        if (split.length == 2) {
                            this.edtEnderecoServidor.setText(ip);
                            if (Funcoes.isLong(porta)) {
                                this.edtPortaServidor.setText(porta);
                                conectar();
                            }
                        } else {
                            blnErro = true;
                        }

                        break;
                    default:
                        blnErro = true;
                }

            } else {
                blnErro = true;
            }

        } else {
            blnErro = true;
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (blnErro) {
            Snackbar.make(this.actConexao, "QR Code inválido", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }

    }

    public void onBackPressed() {
        final Intent intent = new Intent();
        if (conectado) {
            intent.putExtra(RESULTADO_CONEXAO_KEY, KEY_CONECTAR);
        } else {
            intent.putExtra(RESULTADO_CONEXAO_KEY, this.tvStatusServidor.getText().toString());
        }
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

}
