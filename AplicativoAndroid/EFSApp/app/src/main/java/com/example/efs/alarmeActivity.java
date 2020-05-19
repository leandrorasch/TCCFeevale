package com.example.efs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.efs.Adapter.listAlarmesAdapter;
import com.example.efs.conexao.ConexaoWS;
import com.example.efs.estruturas.Alarme;
import com.example.efs.estruturas.Usuario;

import java.util.ArrayList;

public class alarmeActivity extends AppCompatActivity {

    ArrayList<Alarme> alarmes;
    ListView listaAlarmes;
    listAlarmesAdapter alarmesAdapter;
    Usuario usuario;
    boolean carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmes);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        listaAlarmes = (ListView) findViewById(R.id.listAlarmes);
        new carregaAlarmes().execute();

        while (alarmes == null){
            new carregaAlarmes().execute();
        }
        alarmesAdapter = new listAlarmesAdapter(getBaseContext(), alarmes);
        listaAlarmes.setAdapter(alarmesAdapter);
        alarmesAdapter.notifyDataSetChanged();
    }

    public void salvaAlarmes (View v) {
        new AtualizaFlag().execute();
    }

    class carregaAlarmes extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... txtURL) {

            alarmes = new ConexaoWS().buscaAlarmes(usuario.getId_usuario());
            return null;
        }

        protected void onProgressUpdate(Integer... progress) { }

        @Override
        protected void onPostExecute(String out) {

        }

    }

    class AtualizaFlag extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... txtURL) {
            for (int i = 0; i < alarmesAdapter.getLista().size(); i++) {
                Log.i("EFSAPPatualiza", alarmesAdapter.getLista().get(i).getId_alarme()+ alarmesAdapter.getLista().get(i).getFl_ativo());
                ConexaoWS c = new ConexaoWS();
                c.setaAlarme(alarmesAdapter.getLista().get(i).getId_alarme(), alarmesAdapter.getLista().get(i).getFl_ativo());
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) { }

        @Override
        protected void onPostExecute(String out) {

        }
    }
}
