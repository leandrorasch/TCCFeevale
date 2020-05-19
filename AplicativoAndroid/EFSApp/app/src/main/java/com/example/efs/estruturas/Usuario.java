package com.example.efs.estruturas;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import com.example.efs.uteis.EfsUtil;


public class Usuario implements Serializable {

    Integer id_usuario;
    String  ds_usuario;
    String  ds_email;
    String  ds_senha;
    Date dt_criacao;
    String  tp_status;

    public Usuario (){

    }

    public Usuario (String ds_email, String ds_senha){
        this.ds_email = ds_email;
        this.ds_senha = ds_senha;
    }

    public Usuario (String ds_usuario, String ds_email, String ds_senha){
        this.ds_usuario = ds_usuario;
        this.ds_email = ds_email;
        this.ds_senha = ds_senha;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDs_usuario() {
        return ds_usuario;
    }

    public void setDs_usuario(String ds_usuario) {
        this.ds_usuario = ds_usuario;
    }

    public String getDs_email() {
        return ds_email;
    }

    public void setDs_email(String ds_email) {
        this.ds_email = ds_email;
    }

    public String getDs_senha() {
        return ds_senha;
    }

    public void setDs_senha(String ds_senha) {
        this.ds_senha = ds_senha;
    }

    public Date getDt_criacao() {
        return dt_criacao;
    }

    public void setDt_criacao(Date dt_criacao) {
        this.dt_criacao = dt_criacao;
    }

    public String getTp_status() {
        return tp_status;
    }

    public void setTp_status(String tp_status) {
        this.tp_status = tp_status;
    }

    public void setValorAtributo(String atributo, String valor) {
        switch (atributo){
            case "id_usuario": setId_usuario(EfsUtil.StringToInteger(valor)); break;
            case "ds_usuario": setDs_usuario(valor); break;
            case "ds_email"  : setDs_email(valor); break;
            case "ds_senha"  : setDs_senha(valor); break;
            case "dt_criacao": setDt_criacao(EfsUtil.stringToDate(valor)); break;
            case "tp_status" : setTp_status(valor); break;
        }
    }
}
