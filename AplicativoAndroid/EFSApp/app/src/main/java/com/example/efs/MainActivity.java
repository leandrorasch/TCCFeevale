package com.example.efs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.efs.conexao.ConexaoWS;
import com.example.efs.estruturas.Usuario;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    TextView txtmensagem;
    EditText txtemail, txtsenha;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtemail    = (EditText) findViewById(R.id.txtEmail);
        txtsenha    = (EditText) findViewById(R.id.txtSenha);
        txtmensagem = (TextView) findViewById(R.id.txtMensagem);
    }

    public void entra (View v){
        txtmensagem.setText("");
        if (txtemail.getText().toString().isEmpty()){
            txtmensagem.setText("Informe o e-mail!");
        } else if (txtsenha.getText().toString().isEmpty()) {
            txtmensagem.setText("Informe a senha!");
        } else {
            usuario = new Usuario(txtemail.getText().toString(), txtsenha.getText().toString());
            new ExecutaConsulta().execute();
        }
    }

    public void abreCadastro (View v){
        txtmensagem.setText("");
        Intent it = new Intent(getBaseContext(), cadastraUsuario.class);
        startActivity(it);
    }

    public void abreTelaInicial (){
        txtmensagem.setText("");
        Intent it = new Intent(getBaseContext(), telaInicial.class);
        it.putExtra("usuario", usuario);
        startActivity(it);
    }

    class ExecutaConsulta extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... txtURL) {

            ConexaoWS c = new ConexaoWS();
            usuario = c.buscaUsuario(usuario);

            if (usuario != null) {
                abreTelaInicial();
            } else{
                txtmensagem.setText("Verifique e-mail e/ou senha!");
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) { }

        @Override
        protected void onPostExecute(String out) {

        }

    }
}
