package br.com.vandre.KeepCalmAndKaraokeVoto.atividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.KeepCalmAndKaraokeVoto.R;
import br.com.vandre.KeepCalmAndKaraokeVoto.adapters.MusicaAdapter;
import br.com.vandre.KeepCalmAndKaraokeVoto.app.KaraokeAplicacao;
import br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient.AsyncSolicitarMusicas;
import br.com.vandre.KeepCalmAndKaraokeVoto.asyncclient.ConnectionHandler;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.Conexao;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.Musica;
import br.com.vandre.KeepCalmAndKaraokeVoto.util.Funcoes;

import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.KEY_MUSICAS;

public class MusicasActivity extends AppCompatActivity implements ConnectionHandler {
    ListView lvMusicas;
    TextView tvMusicasNaoEncontrado;
    KaraokeAplicacao app;
    Conexao conexao;
    AsyncSolicitarMusicas asyncSolicitarMusicas;
    List<Musica> musicas = new ArrayList<>();
    CheckBox chkNacional;
    CheckBox chkInternacional;
    MusicaAdapter musicaAdapter;
    SearchView mSearchView;
    MenuItem mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicas);
        Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        this.iniciar();
    }

    private void declarar() {
        this.musicaAdapter = new MusicaAdapter(MusicasActivity.this, musicas);
        this.app = (KaraokeAplicacao) this.getApplication();
        conexao = new Conexao(this);
        this.lvMusicas = findViewById(R.id.lvMusicas);
        this.tvMusicasNaoEncontrado = findViewById(R.id.tvMusicasNaoEncontrado);
        this.chkNacional = findViewById(R.id.chkNacional);
        this.chkInternacional = findViewById(R.id.chkInternacional);

        chkInternacional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkNacional.setChecked(false);
                musicaAdapter.setNacional(false);
                musicaAdapter.setIgnorarNacionalInternacional(ignorarNacionalInternacional());
                musicaAdapter.getFilter().filter(mSearchView.getQuery());

            }
        });

        chkNacional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkInternacional.setChecked(false);
                musicaAdapter.setNacional(true);
                musicaAdapter.setIgnorarNacionalInternacional(ignorarNacionalInternacional());
                musicaAdapter.getFilter().filter(mSearchView.getQuery());

            }
        });

    }

    private void iniciar() {
        this.declarar();
        conectar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_musicas, menu);

        mSearch = menu.findItem(R.id.action_searchable_activity);

        mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint(getResources().getString(R.string.pesquisar));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                musicaAdapter.getFilter().filter(newText);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        lvMusicas.requestFocus();
        if (id == R.id.action_pedirmusica) {
            openWhatsApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void conectar() {
        asyncSolicitarMusicas = new AsyncSolicitarMusicas(conexao.getEnderecoServidor(),
                conexao.getPortaServidor(),
                5000,
                MusicasActivity.this,
                this,
                app.getSocket());

        asyncSolicitarMusicas.execute();

    }

    private boolean ignorarNacionalInternacional() {
        return (!this.chkNacional.isChecked()) && (!this.chkInternacional.isChecked());
    }

    @Override
    public void didReceiveData(String data) {
        if (data != null) {
            String key = data.substring(0, 1);
            String values = data.substring(1);
            Gson gson = new Gson();

            switch (key) {
                case KEY_MUSICAS:
                    musicas = gson.fromJson(values, new TypeToken<List<Musica>>() {
                    }.getType());

                    lvMusicas.setVisibility(View.VISIBLE);
                    tvMusicasNaoEncontrado.setVisibility(View.GONE);

                    musicaAdapter = new MusicaAdapter(MusicasActivity.this, musicas);
                    musicaAdapter.setIgnorarNacionalInternacional(ignorarNacionalInternacional());

                    lvMusicas.setAdapter(musicaAdapter);

                    break;
                default:
                    lvMusicas.setVisibility(View.GONE);
                    tvMusicasNaoEncontrado.setVisibility(View.VISIBLE);
                    tvMusicasNaoEncontrado.setText("Erro de conexao: " + data);

                    break;

            }
        } else {
            lvMusicas.setVisibility(View.GONE);
            tvMusicasNaoEncontrado.setVisibility(View.VISIBLE);
            tvMusicasNaoEncontrado.setText("Erro de conexao");
        }

    }

    @Override
    public void didDisconnect(Exception error) {

    }

    @Override
    public void didConnect() {

    }

    private void openWhatsApp() {
        try {
            Intent sendIntent = Funcoes.openWhatsApp(getResources().getString(R.string.mensagem_musica));
            startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
