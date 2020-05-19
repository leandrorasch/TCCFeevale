package com.example.efs.conexao;

import android.os.AsyncTask;
import android.util.Log;

import com.example.efs.estruturas.Acao_esp;
import com.example.efs.estruturas.Alarme;
import com.example.efs.estruturas.Config_Dashboard;
import com.example.efs.estruturas.Dispositivo;
import com.example.efs.estruturas.Topico;
import com.example.efs.estruturas.Usuario;
import com.example.efs.uteis.EfsUtil;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConexaoWS{

    SoapObject retorno;
    ArrayList<SoapObject> retornoLista = new ArrayList();
    Map<String, Object> parametros;
    String erro;

    public ConexaoWS (){

    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public void consultaWebService(String metodo, Map<String, Object> parametros ) {
        erro = "";
        Log.i("EFSAPP", metodo);
        SoapObject soap = new SoapObject("http://server.EFS/", metodo);
        for (String s : parametros.keySet()){
            soap.addProperty(s, String.valueOf(parametros.get(s)));
        }

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope((SoapEnvelope.VER11));
            envelope.setOutputSoapObject(soap);
            String url = "http://192.168.2.116:8080/EFS/EfsService?wsdl";

            HttpTransportSE httpTransport = new HttpTransportSE(url);
            httpTransport.call("", envelope);
            if (metodo == "executaConsulta"){
                retorno = new SoapObject("retorno", envelope.getResponse().toString());
            }else {
                retorno = (SoapObject) envelope.getResponse();
            }
            //
        } catch (IOException e){
            erro = e.getMessage();
        } catch (XmlPullParserException e){
            erro = e.getMessage();
        }
    }

    public void consultaWebServiceLista(String metodo, Map<String, Object> parametros ) {
        erro = "";
        Log.i("EFSAPP", metodo);
        SoapObject soap = new SoapObject("http://server.EFS/", metodo);
        for (String s : parametros.keySet()){
            soap.addProperty(s, String.valueOf(parametros.get(s)));
        }

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope((SoapEnvelope.VER11));
            envelope.setOutputSoapObject(soap);
            String url = "http://192.168.2.116:8080/EFS/EfsService?wsdl";

            HttpTransportSE httpTransport = new HttpTransportSE(url);
            httpTransport.call("", envelope);
            retorno = (SoapObject) envelope.bodyIn;

            for (int i = 0; i < retorno.getPropertyCount(); i++){
                retornoLista.add((SoapObject) retorno.getProperty(i));
            }
        } catch (IOException e){
            erro = e.getMessage();
        } catch (XmlPullParserException e){
            erro = e.getMessage();
        }
    }

    public Usuario buscaUsuario(Usuario usuario) {

        Usuario user = null;
        parametros = new HashMap();
        parametros.put("email", usuario.getDs_email());
        parametros.put("senha", usuario.getDs_senha());

        consultaWebService("buscaUsuario", parametros);

        if (erro == "") {
            user = new Usuario();
            user.setValorAtributo("ds_email", retorno.getProperty("ds_email").toString());
            user.setValorAtributo("ds_senha", retorno.getProperty("ds_senha").toString());
            user.setValorAtributo("ds_usuario", retorno.getProperty("ds_usuario").toString());
            user.setValorAtributo("dt_criacao", retorno.getProperty("dt_criacao").toString());
            user.setValorAtributo("id_usuario", retorno.getProperty("id_usuario").toString());
            user.setValorAtributo("tp_status", retorno.getProperty("tp_status").toString());
        }
        return user;
    }

    public Usuario realizaCadastro(Usuario usuario) {

        Usuario user = null;
        parametros = new HashMap();
        parametros.put("nome" , usuario.getDs_usuario());
        parametros.put("email", usuario.getDs_email());
        parametros.put("senha", usuario.getDs_senha());

        consultaWebService("cadastraUsuario", parametros);

        if (erro == "") {
            user = new Usuario();
            user.setValorAtributo("ds_email", retorno.getProperty("ds_email").toString());
            user.setValorAtributo("ds_senha", retorno.getProperty("ds_senha").toString());
            user.setValorAtributo("ds_usuario", retorno.getProperty("ds_usuario").toString());
            user.setValorAtributo("dt_criacao", retorno.getProperty("dt_criacao").toString());
            user.setValorAtributo("id_usuario", retorno.getProperty("id_usuario").toString());
            user.setValorAtributo("tp_status", retorno.getProperty("tp_status").toString());
        }
        return user;
    }

    public Dispositivo buscaDispositivo(Integer id_dispositivo) {

        Dispositivo d = null;
        parametros = new HashMap();
        parametros.put("id_dispositivo", id_dispositivo);

        consultaWebService("buscaDispositivo", parametros);

        if (erro == "") {
            d = new Dispositivo();
            d.setValorAtributo("id_dispositivo", retorno.getProperty("id_dispositivo").toString());
            d.setValorAtributo("ds_dispositivo", retorno.getProperty("ds_dispositivo").toString());
            d.setValorAtributo("id_usuario"    , retorno.getProperty("id_usuario"    ).toString());
            d.setValorAtributo("local"         , retorno.getProperty("local"         ).toString());
            d.setValorAtributo("fl_ativo"      , retorno.getProperty("fl_ativo"      ).toString());
            d.setValorAtributo("dt_cadastro"   , retorno.getProperty("dt_cadastro"   ).toString());
            d.setValorAtributo("aplicacao_uso" , retorno.getProperty("aplicacao_uso" ).toString());
        }
        return d;
    }

    public ArrayList<Dispositivo> listaDispositivos(Integer id_usuario){
        ArrayList<Dispositivo> lista = new ArrayList();
        parametros = new HashMap();
        parametros.put("id_usuario", id_usuario);

        consultaWebServiceLista("listaDispositivos", parametros);

        if (erro == ""){
            for (int i = 0; i < retornoLista.size(); i++){
                Dispositivo d = new Dispositivo();
                d.setValorAtributo("id_dispositivo", retornoLista.get(i).getProperty("id_dispositivo").toString());
                d.setValorAtributo("ds_dispositivo", retornoLista.get(i).getProperty("ds_dispositivo").toString());
                d.setValorAtributo("id_usuario"    , retornoLista.get(i).getProperty("id_usuario"    ).toString());
                d.setValorAtributo("local"         , retornoLista.get(i).getProperty("local"         ).toString());
                d.setValorAtributo("fl_ativo"      , retornoLista.get(i).getProperty("fl_ativo"      ).toString());
                d.setValorAtributo("dt_cadastro"   , retornoLista.get(i).getProperty("dt_cadastro"   ).toString());
                d.setValorAtributo("aplicacao_uso" , retornoLista.get(i).getProperty("aplicacao_uso" ).toString());
                lista.add(d);
            }
        }

        return lista;
    }

    public Topico buscaTopico(Integer id_topico) {

        Topico topico = null;
        parametros = new HashMap();
        parametros.put("id_topico", id_topico);

        consultaWebService("buscaTopico", parametros);

        if (erro == "") {
            topico = new Topico();
            topico.setValorAtributo("id_topico"   , retorno.getProperty("id_topico"   ).toString());
            topico.setValorAtributo("ds_topico"   , retorno.getProperty("ds_topico"   ).toString());
            topico.setValorAtributo("dt_inclusao" , retorno.getProperty("dt_inclusao" ).toString());
            topico.setValorAtributo("fl_ativo"    , retorno.getProperty("fl_ativo"    ).toString());
            topico.setValorAtributo("dt_alteracao", retorno.getProperty("dt_alteracao").toString());
        }
        return topico;
    }

    public ArrayList<Config_Dashboard> buscaConfig_Dashboard(Integer id_usuario){
        ArrayList<Config_Dashboard> lista = new ArrayList();
        parametros = new HashMap();
        parametros.put("id_usuario"    , id_usuario);
        parametros.put("id_topico"     , -1);
        parametros.put("id_dispositivo", -1);

        consultaWebServiceLista("listaConfigDashboard", parametros);

        if (erro == ""){
            for (int i = 0; i < retornoLista.size(); i++){
                Config_Dashboard d = new Config_Dashboard();
                d.setValorAtributo("id_usuario"  , retornoLista.get(i).getProperty("id_usuario"    ).toString());
                d.setValorAtributo("topico"      , retornoLista.get(i).getProperty("id_topico"     ).toString());
                d.setValorAtributo("dispositivo" , retornoLista.get(i).getProperty("id_dispositivo").toString());
                d.setValorAtributo("tp_visao"    , retornoLista.get(i).getProperty("tp_visao"      ).toString());
                d.setValorAtributo("ordenacao"   , retornoLista.get(i).getProperty("ordenacao"     ).toString());
                d.setValorAtributo("configuracao", retornoLista.get(i).getProperty("configuracao"  ).toString());
                lista.add(d);
            }
        }

        return lista;
    }

    public ArrayList<Alarme> buscaAlarmes (Integer id_usuario){
        ArrayList<Alarme> lista = new ArrayList();
        parametros = new HashMap();
        parametros.put("id_usuario", id_usuario);

        consultaWebServiceLista("buscaalarmes", parametros);

        if (erro == ""){
            for (int i = 0; i < retornoLista.size(); i++){
                Alarme a = new Alarme();
                a.setValorAtributo("id_alarme"     , retornoLista.get(i).getProperty("id_alarme"     ).toString());
                a.setValorAtributo("id_dispositivo", retornoLista.get(i).getProperty("id_dispositivo").toString());
                a.setValorAtributo("id_topico"     , retornoLista.get(i).getProperty("id_topico"     ).toString());
                a.setValorAtributo("fl_ativo"      , retornoLista.get(i).getProperty("fl_ativo"      ).toString());
                a.setValorAtributo("vlr_minimo"    , retornoLista.get(i).getProperty("vlr_minimo"    ).toString());
                a.setValorAtributo("vlr_maximo"    , retornoLista.get(i).getProperty("vlr_maximo"    ).toString());
                lista.add(a);
            }
        }
        return lista;
    }

    public void setaAlarme (Integer id_alarme, String fl_ativo){
        ArrayList<Alarme> lista = new ArrayList();
        parametros = new HashMap();
        parametros.put("id_alarme", id_alarme);
        parametros.put("fl_ativo" , fl_ativo);

        consultaWebService("setaalarme", parametros);

        if (erro == ""){
            Alarme a = new Alarme();
            a.setValorAtributo("id_alarme"     , retorno.getProperty("id_alarme"     ).toString());
            a.setValorAtributo("id_dispositivo", retorno.getProperty("id_dispositivo").toString());
            a.setValorAtributo("id_topico"     , retorno.getProperty("id_topico"     ).toString());
            a.setValorAtributo("fl_ativo"      , retorno.getProperty("fl_ativo"      ).toString());
            a.setValorAtributo("vlr_minimo"    , retorno.getProperty("vlr_minimo"    ).toString());
            a.setValorAtributo("vlr_maximo"    , retorno.getProperty("vlr_maximo"    ).toString());
            Log.i("EFSAPP", a.getDispositivo().getDs_dispositivo());
        }
    }


    public Acao_esp enviAcao (Integer id_dispositivo, Integer id_usuario, Integer id_topico, String valor){
        Acao_esp a = new Acao_esp();
        parametros = new HashMap();
        parametros.put("id_dispositivo", id_dispositivo);
        parametros.put("id_usuario"    , id_usuario);
        parametros.put("id_topico"     , id_topico);
        parametros.put("valor"         , valor);

        consultaWebService("enviaacao", parametros);

        if (erro == ""){
            a = new Acao_esp();
            a.setValorAtributo("id_acao_esp"   , retorno.getProperty("id_acao_esp"   ).toString());
            Log.i("EFSAPP", a.getId_acao_esp().toString());
        }

        return a;
    }

    public ArrayList<String> executaConsulta(String ds_comando){
        ArrayList<String> lista = new ArrayList();
        parametros = new HashMap();
        parametros.put("ds_comando"    , ds_comando);

        consultaWebService("executaConsulta", parametros);

        if (erro == ""){
            //for (int i = 0; i < retornoLista.size(); i++){
            //    lista.add(retornoLista.get(i).getProperty(0).toString());
            //}
            //for (int i = 0; i < retorno.getPropertyCount(); i++) {
            //    lista.add(retorno.getName());
           // }
            lista.add(retorno.getName());
        }

        return lista;
    }
}
