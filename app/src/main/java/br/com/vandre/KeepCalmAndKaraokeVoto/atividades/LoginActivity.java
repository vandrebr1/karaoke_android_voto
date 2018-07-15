package br.com.vandre.KeepCalmAndKaraokeVoto.atividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import br.com.vandre.KeepCalmAndKaraokeVoto.R;
import br.com.vandre.KeepCalmAndKaraokeVoto.app.KaraokeAplicacao;
import br.com.vandre.KeepCalmAndKaraokeVoto.util.FontManager;

import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_IMEI;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_KEY;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_JURADO;

public class LoginActivity extends AppCompatActivity {
    Typeface iconFontKeepCalm;
    Button btnLogin;
    EditText edtUsuario;
    RelativeLayout rlActLogin;
    KaraokeAplicacao app;
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.iniciar();
    }

    public void declarar() {
        this.app = (KaraokeAplicacao) this.getApplication();
        imei = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        btnLogin = findViewById(R.id.btnLogin);
        edtUsuario = findViewById(R.id.edtUsuario);
        rlActLogin = findViewById(R.id.actLogin);
        this.iconFontKeepCalm = FontManager.getTypeface(this.getApplicationContext(), FontManager.FONTKEEPCALM);

        btnLogin.setOnClickListener(new BtnLoginClickListener());

    }

    public void iniciar() {
        this.declarar();
        FontManager.markAsIconContainer(this.findViewById(R.id.rlKeepCalmAndKaraoke), this.iconFontKeepCalm);
        carregarConfiguracao();
    }

    private class BtnLoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(edtUsuario.getText().toString().isEmpty()){
                Snackbar.make(rlActLogin, "Digite seu login", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }else{
                salvarConfiguracao();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

        }
    }

    private void carregarConfiguracao() {
        final SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        this.edtUsuario.setText(sharedPreferences.getString(PREF_JURADO, ""));

    }

    private void salvarConfiguracao() {

        final SharedPreferences.Editor edit = this.getSharedPreferences(PREF_KEY, 0).edit();
        edit.putString(PREF_JURADO, this.edtUsuario.getText().toString());
        edit.putString(PREF_IMEI, this.imei);
        edit.apply();

    }
}
