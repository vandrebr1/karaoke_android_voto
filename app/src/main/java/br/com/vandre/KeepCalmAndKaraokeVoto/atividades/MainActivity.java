package br.com.vandre.KeepCalmAndKaraokeVoto.atividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.com.vandre.KeepCalmAndKaraokeVoto.R;
import br.com.vandre.KeepCalmAndKaraokeVoto.app.KaraokeAplicacao;
import br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient.AsyncConnection;
import br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient.AsyncWriter;
import br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient.ConnectionHandler;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.Conexao;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.Play;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.Pontuacao;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.UltimoResultado;
import br.com.vandre.KeepCalmAndKaraokeVoto.util.FontManager;
import br.com.vandre.KeepCalmAndKaraokeVoto.util.Funcoes;

import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_CONECTAR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_DESCONECTAR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_SOLICITARCANTORES;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_ULTIMORESULTADO;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_VOTAR;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.MSG_CONECTANDO;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.MSG_DESCONECTADO;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.MSG_SUCESSO;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_IMEI;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_JURADO;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_KEY;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.PREF_ULTIMAPLAYID;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.RESULTADO_CONEXAO_KEY;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.SOCKET_MIN_TIMEOUT;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.SOCKET_TIMEOUT;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.RequestConstantes.REQUEST_CONEXAO_RESULT;

public class MainActivity extends AppCompatActivity implements ConnectionHandler {


    public KaraokeAplicacao app;
    private AsyncConnection asyncConnection;
    private Typeface iconFontAwesome;
    private Conexao conexao;
    private TextView tvStatusCenter;
    private TextView tvStatusDebug;
    private TextView tvCantorApelido1;
    private TextView tvCantorApelido2;
    private TextView tvMusica;
    private TextView tvMusicaCantor;
    private RatingBar votoCator1;
    private RatingBar votoCator2;
    private ImageView imgIconeBatalha;
    private View vwComCantor;
    private RelativeLayout rlCantor2;
    private FloatingActionButton fab;
    private Play play;
    private String jurado;
    private String imei;
    private int ultimaPlayID;
    private boolean isVotar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getSocket().isConnected()) {
                    if (play != null) {
                        fab.setEnabled(false);
                        showSnackbar(R.string.enviandovoto, Snackbar.LENGTH_LONG);
                        writer(KEY_VOTAR);
                        isVotar = true;
                    } else {
                        fab.setEnabled(false);
                        showSnackbar(R.string.solicitandocantores, Snackbar.LENGTH_LONG);
                        writer(KEY_SOLICITARCANTORES);
                    }
                } else {
                    showSnackbar(R.string.semconexaoreconexão, Snackbar.LENGTH_LONG);
                    conectarServidor(5000);
                }
            }
        });

        iniciar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_icons8_coroa_pequeno);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.action_settings) {
            intent = new Intent(this, ConexaoActivity.class);
            this.startActivityForResult(intent, REQUEST_CONEXAO_RESULT);
            return true;
        } else if (id == R.id.action_musicas) {
            intent = new Intent(this, MusicasActivity.class);
            this.startActivityForResult(intent, REQUEST_CONEXAO_RESULT);
            return true;
        } else if (id == R.id.action_sobre) {
            sobre();
            return true;
        } else if (id == R.id.action_ultimoresultado) {
            writer(KEY_ULTIMORESULTADO);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregarConfiguracao() {
        conexao = new Conexao(this);
        final SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        this.jurado = sharedPreferences.getString(PREF_JURADO, "NAO INFORMADO");
        this.imei = sharedPreferences.getString(PREF_IMEI, "NAO INFORMADO");
        this.ultimaPlayID = sharedPreferences.getInt(PREF_ULTIMAPLAYID, 0);
    }

    private void salvarConfiguracao() {

        final SharedPreferences.Editor edit = this.getSharedPreferences(PREF_KEY, 0).edit();
        edit.putInt(PREF_ULTIMAPLAYID, this.ultimaPlayID);
        edit.apply();

    }

    private void conectarServidor(final int timeout) {
        fab.setEnabled(false);
        if (!app.getSocket().isConnected()) {
            asyncConnection = new AsyncConnection(conexao.getEnderecoServidor(),
                    conexao.getPortaServidor(), timeout,
                    this, app.getSocket());
            asyncConnection.setInterromperConexao(false);
            asyncConnection.execute();
        }
    }

    private void declarar() {

        this.app = (KaraokeAplicacao) this.getApplication();
        this.iconFontAwesome = FontManager.getTypeface(this.getApplicationContext(), FontManager.FONTAWESOME);
        this.votoCator1 = findViewById(R.id.votoCantor1);
        this.votoCator2 = findViewById(R.id.votoCantor2);
        this.tvStatusDebug = findViewById(R.id.tvStatusMain);
        this.tvStatusCenter = findViewById(R.id.tvStatusCenter);
        this.vwComCantor = findViewById(R.id.vwComCantor);
        this.tvCantorApelido1 = findViewById(R.id.tvCantorApelido1);
        this.tvCantorApelido2 = findViewById(R.id.tvCantorApelido2);
        this.tvMusica = findViewById(R.id.tvMusica);
        this.rlCantor2 = findViewById(R.id.rlCantor2);
        this.tvMusicaCantor = findViewById(R.id.tvMusicaCantor);
        this.imgIconeBatalha = findViewById(R.id.imgIconeBatalha);
    }

    private void iniciar() {

        this.declarar();
        this.carregarConfiguracao();
        FontManager.markAsIconContainer(this.findViewById(R.id.tvIconeCantor1), this.iconFontAwesome);
        FontManager.markAsIconContainer(this.findViewById(R.id.tvIconeCantor2), this.iconFontAwesome);
        //FontManager.markAsIconContainer(this.findViewById(R.id.tvIconeMusica), this.iconFontAwesome);

        votoCator1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                votar(votoCator1, v, event);
                return true;
            }
        });

        votoCator2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                votar(votoCator2, v, event);
                return true;
            }
        });

        //conectarServidor(SOCKET_MIN_TIMEOUT);
    }

    private void writer(final String key) {
        AsyncWriter asyncWriter = new AsyncWriter(this, app.getSocket());

        switch (key) {
            case KEY_DESCONECTAR:
                if (app.getSocket().isConnected()) {
                    showSnackbar(R.string.desconctando, Snackbar.LENGTH_LONG);
                    asyncWriter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, key, MSG_DESCONECTADO);
                }
                break;

            case KEY_SOLICITARCANTORES:
                tvStatusDebug.setText(this.getResources().getString(R.string.solicitandocantores));
                if (app.getSocket().isConnected()) {
                    asyncWriter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, key, imei);
                }
                break;

            case KEY_VOTAR:
                tvStatusDebug.setText(this.getResources().getString(R.string.enviandovoto));
                String voto;
                Gson gson = new Gson();
                Pontuacao pontuacao = new Pontuacao();
                List<Pontuacao> pontuacoes = new ArrayList<>();

                pontuacao.setCantorID(play.getCantor1().getId());
                pontuacao.setJurado(jurado);
                pontuacao.setImei(imei);
                pontuacao.setPonto((int) votoCator1.getRating());
                pontuacao.setPlayID(play.getId());

                //pontos cantor 1
                pontuacoes.add(pontuacao);

                if (play.getCantor2() != null) {
                    pontuacao = new Pontuacao();

                    pontuacao.setCantorID(play.getCantor2().getId());
                    pontuacao.setJurado(jurado);
                    pontuacao.setImei(imei);
                    pontuacao.setPonto((int) votoCator2.getRating());
                    pontuacao.setPlayID(play.getId());
                    pontuacoes.add(pontuacao);

                }
                voto = gson.toJson(pontuacoes);

                if (app.getSocket().isConnected()) {
                    asyncWriter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, key, voto);
                }
                break;

            case KEY_ULTIMORESULTADO:
                tvStatusDebug.setText(this.getResources().getString(R.string.action_ultimoresultado));
                if (app.getSocket().isConnected()) {
                    asyncWriter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, key, imei);
                }
                break;
        }
    }

    public void didConnect() {

    }

    public void didDisconnect(final Exception ex) {
        if (ex != null) {
            showSnackbar(R.string.erroconexao, Snackbar.LENGTH_LONG);
            this.tvStatusDebug.setText(ex.toString());

        } else {
            showSnackbar(R.string.desconectado, Snackbar.LENGTH_LONG);
            this.tvStatusDebug.setText(this.getResources().getString(R.string.desconectado));

        }

        play = null;
        alternarViewCantorStatus(R.string.desconectado, false);

    }

    public void didReceiveData(final String data) {
        fab.setEnabled(false);

        tvStatusDebug.setText(data);
        if (data != null) {
            String key = data.substring(0, 1);
            String values = data.substring(1);
            Gson gson = new Gson();
            switch (key) {
                case KEY_SOLICITARCANTORES:
                    play = gson.fromJson(values, Play.class);

                    resetarTela();

                    if (play.getId() != null) {
                        if (play.getId() != ultimaPlayID) {
                            if (!play.getMusica().getId().equals("")) {
                                tvMusica.setText(play.getMusica().getMusica());
                                tvMusicaCantor.setText(play.getMusica().getCantor());
                                tvCantorApelido1.setText(play.getCantor1().getApelido());


                                if (play.getCantor2() != null) {
                                    tvCantorApelido2.setText(play.getCantor2().getApelido());
                                    showSnackbar(R.string.novocantor2, Snackbar.LENGTH_LONG);
                                    rlCantor2.setVisibility(View.VISIBLE);
                                    if (play.isBatalha()) {
                                        imgIconeBatalha.setImageDrawable(getResources().getDrawable(R.drawable.versus));
                                    } else {
                                        imgIconeBatalha.setImageDrawable(getResources().getDrawable(R.drawable.dupla));
                                    }
                                } else {
                                    showSnackbar(R.string.novocantor, Snackbar.LENGTH_LONG);
                                    rlCantor2.setVisibility(View.GONE);
                                    imgIconeBatalha.setImageDrawable(getResources().getDrawable(R.drawable.dupla));
                                }

                            } else {
                                tvStatusDebug.setText(this.getResources().getText(R.string.semcantor) + " - " + data);
                            }

                            alternarViewCantorStatus(0, true);
                            tvStatusDebug.setText("playID: " + play.getId());
                        } else {
                            if (isVotar) {
                                showSnackbar(R.string.vocejavotou, Snackbar.LENGTH_LONG);
                                alternarViewCantorStatus(R.string.vocejavotou, false);
                                this.tvStatusDebug.setText(this.getResources().getText(R.string.obrigadovoto));
                            } else {
                                this.tvStatusDebug.setText(this.getResources().getText(R.string.conectado));
                                showSnackbar(R.string.semcantor, Snackbar.LENGTH_LONG);
                            }

                            play = null;

                        }
                    } else {
                        play = null;
                    }
                    break;

                case KEY_VOTAR:

                    String retornoVoto[] = values.split(Pattern.quote("|"));
                    String mensagemConclusao = retornoVoto[1];
                    String mensagemException = retornoVoto[2];

                    if (mensagemConclusao.equals(MSG_SUCESSO)) {
                        showSnackbar(R.string.obrigadovoto, Snackbar.LENGTH_LONG);
                        tvStatusCenter.setText(this.getResources().getText(R.string.obrigadovoto));
                        ultimaPlayID = play.getId();
                        salvarConfiguracao();
                        play = null;
                    } else {
                        showSnackbar(R.string.errovoto, Snackbar.LENGTH_LONG);
                        tvStatusCenter.setText(this.getResources().getText(R.string.errovoto) + ":" + mensagemException);
                    }

                    alternarViewCantorStatus(R.string.obrigadovoto, false);

                    isVotar = false;
                    break;

                case KEY_CONECTAR:
                    showSnackbar(R.string.conectado, Snackbar.LENGTH_LONG);
                    tvStatusDebug.setText(values);
                    writer(KEY_SOLICITARCANTORES);
                    break;

                case KEY_ULTIMORESULTADO:
                    ultimoResultado(values);

                    break;

                default:
                    alternarViewCantorStatus(R.string.semcantor, false);
                    break;
            }
        }

        fab.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode != REQUEST_CONEXAO_RESULT) {
                this.tvStatusDebug.setText(this.getResources().getText(R.string.semconexao));
            } else if (data.getStringExtra(RESULTADO_CONEXAO_KEY).equals(KEY_CONECTAR)) {
                carregarConfiguracao();
                this.conectarServidor(SOCKET_TIMEOUT);
            } else if (data.getStringExtra(RESULTADO_CONEXAO_KEY).equals("") || data.getStringExtra(RESULTADO_CONEXAO_KEY).equals(MSG_CONECTANDO)) {
                this.tvStatusDebug.setText(this.getResources().getText(R.string.semconexao));
                alternarViewCantorStatus(R.string.semconexao, false);
            } else {
                this.tvStatusDebug.setText(data.getStringExtra(RESULTADO_CONEXAO_KEY));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void onBackPressed() {

        MainActivity.this.finish();

    }

    public void votar(final RatingBar ratingBar, final View view, final MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float touchPositionX = motionEvent.getX();
            float width = ratingBar.getWidth();
            float starsf = (touchPositionX / width) * 5.0f;
            int stars = (int) starsf + 1;

            if (ratingBar.getRating() == 1 && stars == 1) {
                stars = 0;
            }
            ratingBar.setRating(stars);

            view.setPressed(false);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            view.setPressed(true);
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
            view.setPressed(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        carregarConfiguracao();
        play = null;

        if (!app.getSocket().isConnected()) {

            conectarServidor(SOCKET_TIMEOUT);
        }
        Log.e("TAG", "resume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        asyncConnection.setInterromperConexao(true);

        try {
            app.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("TAG", "pause");

    }

    private void showSnackbar(int rIdMensagem, int length) {
        Snackbar snackbar = Snackbar.make(rlCantor2, rIdMensagem, length);
        snackbar.show();

    }

    private void alternarViewCantorStatus(int statusMensagemID, boolean exibirCantores) {
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        if (exibirCantores) {
            vwComCantor.setVisibility(View.VISIBLE);
            vwComCantor.startAnimation(slideUp);
            tvStatusCenter.setVisibility(View.GONE);

            tvStatusCenter.setText("");
        } else {
            play = null;
            vwComCantor.setVisibility(View.GONE);
            tvStatusCenter.setVisibility(View.VISIBLE);
            tvStatusCenter.setText(this.getResources().getText(statusMensagemID));
        }

    }

    private void sobre() {
        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.ic_icons8_coroa)
                .titleGravity(GravityEnum.CENTER)
                .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                .contentGravity(GravityEnum.CENTER)
                .title(R.string.app_name_completo)
                .content(R.string.sobre_texto)
                .contentColor(getResources().getColor(R.color.colorPrimaryLight))
                .positiveText(R.string.ok)
                .buttonsGravity(GravityEnum.CENTER)
                .neutralText(R.string.sobre_contato)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        openWhatsApp();
                    }
                })
                .show();
    }

    private void ultimoResultado(String values) {

        tvStatusDebug.setText("Último resultado: " + values.replaceAll(Pattern.quote("|"), " - "));

        values = values.replaceAll(Pattern.quote("|"), "\n");
        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.ic_icons8_coroa)
                .titleGravity(GravityEnum.CENTER)
                .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                .contentGravity(GravityEnum.START)
                .title(R.string.action_ultimoresultado)
                .content(values)
                .contentColor(getResources().getColor(R.color.colorPrimaryLight))
                .positiveText(R.string.ok)
                .buttonsGravity(GravityEnum.CENTER)
                .show();
    }

    private void openWhatsApp() {
        try {
            Intent sendIntent = Funcoes.openWhatsApp("Oi ");
            startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void resetarTela() {
        votoCator1.setRating(0);
        votoCator2.setRating(0);

    }


}
