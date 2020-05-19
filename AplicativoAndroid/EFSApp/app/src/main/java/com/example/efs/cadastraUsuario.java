package com.example.efs;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.efs.conexao.ConexaoWS;
import com.example.efs.estruturas.Usuario;

public class cadastraUsuario extends AppCompatActivity {

    EditText txtNome, txtEmail, txtSenha1, txtSenha2;
    TextView txtmensagem;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_usuario);

        txtNome = (EditText) findViewById(R.id.txtCadastraUsuarioNome);
        txtEmail = (EditText) findViewById(R.id.txtCadastraUsuarioEmail);
        txtSenha1 = (EditText) findViewById(R.id.txtCadastroUsuarioSenha);
        txtSenha2 = (EditText) findViewById(R.id.txtCadastroUsuarioSenha2);
        txtmensagem = (TextView) findViewById(R.id.txtCadastraUsuarioMensagem);
    }

    public void realizaCadastro (View v){
        txtmensagem.setText("");
        if (txtNome.getText().toString().isEmpty()){
            txtmensagem.setText("Informe o nome do usuário!");
        } else if (txtEmail.getText().toString().isEmpty()){
            txtmensagem.setText("Informe o e-mail do usuário!");
        } else if (txtSenha1.getText().toString().isEmpty() || txtSenha2.getText().toString().isEmpty()){
            txtmensagem.setText("Informe a senha!");
        } else if (!txtSenha1.getText().toString().equals(txtSenha2.getText().toString())){
            txtmensagem.setText("A senha e a confirmação devem ser iguais!");
        } else if (txtSenha1.getText().toString().length() < 8) {
            txtmensagem.setText("A senha deve ter no mínimo 8 caracteres!");
        } else {
            usuario = new Usuario(txtNome.getText().toString(), txtEmail.getText().toString(), txtSenha1.getText().toString());
            new cadastroUsuario().execute();
        }
    }

    class cadastroUsuario extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... txtURL) {

            ConexaoWS c = new ConexaoWS();
            usuario = c.realizaCadastro(usuario);

            if (usuario != null){
                txtmensagem.setText(usuario.getDs_usuario());
            } else {
                txtmensagem.setText("Erro ao inserir usuário. Contate o suporte!");
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) { }

        @Override
        protected void onPostExecute(String out) {

        }

    }
}
