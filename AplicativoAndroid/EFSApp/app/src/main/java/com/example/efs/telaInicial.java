package com.example.efs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.efs.conexao.ConexaoWS;
import com.example.efs.estruturas.Acao_esp;
import com.example.efs.estruturas.Config_Dashboard;
import com.example.efs.estruturas.Dispositivo;
import com.example.efs.estruturas.Usuario;
import com.example.efs.uteis.EfsUtil;

import java.util.ArrayList;

public class telaInicial extends AppCompatActivity {

    Usuario usuario;
    TextView txtmensagem, txtmensagem2, txtId_topico_acao, txtValor_acao;
    ArrayList<Config_Dashboard> config_dashboard = new ArrayList();
    Acao_esp a = new Acao_esp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        txtmensagem  = (TextView) findViewById(R.id.txtTelaInicialMensagem);
        txtmensagem2 = (TextView) findViewById(R.id.txtmensagem2);

        txtId_topico_acao = (TextView) findViewById(R.id.txtId_topicoAcao);
        txtValor_acao     = (TextView) findViewById(R.id.txtValor_acao);
        //txtmensagem.setText(usuario.getDs_usuario());
        new carregaTela().execute();
    }

    public void abreAlarmes (View v){
        Intent it = new Intent(getBaseContext(), alarmeActivity.class);
        it.putExtra("usuario", usuario);
        startActivity(it);
    }

    public void executaAcao (View v){
        Toast.makeText(this.getBaseContext(), "Enviando ação...", Toast.LENGTH_SHORT);
        new ExecutaAcao().execute();
        //Toast.makeText(this.getBaseContext(), "Ação "+ a.getId_acao_esp().toString() + " enviada!", Toast.LENGTH_SHORT);
    }

    public void exibeDashboard(){
        for (int i = 0; i < config_dashboard.size(); i++){
            Config_Dashboard cd = config_dashboard.get(i);

            if (cd.getTp_visao().equals("T")){
                String comando = cd.getConfiguracao();
                comando = comando.replace("#id_topico", EfsUtil.IntegerToString(cd.getTopico().getId_topico()));
                comando = comando.replace("#id_dispositivo", EfsUtil.IntegerToString(cd.getDispositivo().getId_dispositivo()));

                String retorno = "";
                ArrayList<String> lista = new ConexaoWS().executaConsulta(comando);

                for (int x = 0; x < lista.size(); x++){
                    retorno = lista.get(x);
                }

                if (i==0){
                    txtmensagem.setText(cd.getTopico().getDs_topico() + ": " + retorno);
                } else{
                    txtmensagem2.setText(cd.getTopico().getDs_topico() + ": " + retorno);
                }
            }
        }
    }

    class carregaTela extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... txtURL) {

            ConexaoWS c = new ConexaoWS();
            config_dashboard = c.buscaConfig_Dashboard(usuario.getId_usuario());

            exibeDashboard();

            //if (usuario != null){
            //    txtmensagem.setText(config_dashboard.get(0).getDispositivo().getDs_dispositivo());
            //} else {
            //    txtmensagem.setText("Erro ao buscar dispositivos. Contate o suporte!");
            //}
            return null;
        }

        protected void onProgressUpdate(Integer... progress) { }

        @Override
        protected void onPostExecute(String out) {

        }

    }

    class ExecutaAcao extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... txtURL) {

            Integer id_topico_acao = Integer.parseInt(txtId_topico_acao.getText().toString());
            String  valor          = txtValor_acao.getText().toString();
            Integer id_dispositivo = 1;


            ConexaoWS c = new ConexaoWS();
            a = c.enviAcao(id_dispositivo, usuario.getId_usuario(), id_topico_acao, valor);

            txtId_topico_acao.setText("");
            txtValor_acao.setText("");

            //if (usuario != null){
            //    txtmensagem.setText(config_dashboard.get(0).getDispositivo().getDs_dispositivo());
            //} else {
            //    txtmensagem.setText("Erro ao buscar dispositivos. Contate o suporte!");
            //}
            return null;
        }

        protected void onProgressUpdate(Integer... progress) { }

        @Override
        protected void onPostExecute(String out) {

        }

    }
}
