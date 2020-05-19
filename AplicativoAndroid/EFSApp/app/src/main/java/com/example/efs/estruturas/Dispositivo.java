package com.example.efs.estruturas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Date;
import com.example.efs.uteis.EfsUtil;

/**
 *
 * @author Leandro Rasch
 */
public class Dispositivo{

    private Integer id_dispositivo;
    private String ds_dispositivo;
    private Integer id_usuario;
    private String fl_ativo;
    private String aplicacao_uso;
    private String local;
    private Date   dt_cadastro;

    public Dispositivo(){

    }

    public Dispositivo(Integer id_dispositivo, String ds_dispositivo, String fl_ativo, String aplicacao_uso, String local, Integer id_usuario){
        this();
        this.id_dispositivo = id_dispositivo;
        this.ds_dispositivo = ds_dispositivo;
        this.fl_ativo       = fl_ativo;
        this.aplicacao_uso  = aplicacao_uso;
        this.local          = local;
        this.dt_cadastro    = EfsUtil.getDataAtual();
        this.id_usuario     = id_usuario;
    }

    public Integer getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(Integer id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public String getDs_dispositivo() {
        return ds_dispositivo;
    }

    public void setDs_dispositivo(String ds_dispositivo) {
        this.ds_dispositivo = ds_dispositivo;
    }

    public String getFl_ativo() {
        return fl_ativo;
    }

    public void setFl_ativo(String fl_ativo) {
        this.fl_ativo = fl_ativo;
    }

    public String getAplicacao_uso() {
        return aplicacao_uso;
    }

    public void setAplicacao_uso(String aplicacao_uso) {
        this.aplicacao_uso = aplicacao_uso;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(Date dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setValorAtributo(String atributo, String valor) {
        switch (atributo){
            case "id_dispositivo": setId_dispositivo(EfsUtil.StringToInteger(valor)); break;
            case "ds_dispositivo": setDs_dispositivo(valor); break;
            case "fl_ativo"      : setFl_ativo(valor); break;
            case "local"         : setLocal(valor); break;
            case "dt_cadastro"   : setDt_cadastro(EfsUtil.stringToDate(valor)); break;
            case "id_usuario"    : setId_usuario(EfsUtil.StringToInteger(valor)); break;
            case "aplicacao_uso" : setAplicacao_uso(valor); break;
        }
    }

}
