package com.example.efs.estruturas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.example.efs.conexao.ConexaoWS;
import com.example.efs.uteis.EfsUtil;

import java.util.Map;

/**
 *
 * @author Leandro Rasch
 */
public class Alarme {

    Integer     id_alarme;
    Dispositivo dispositivo = new Dispositivo();
    Topico      topico      = new Topico();
    String      fl_ativo;
    Double      vlr_minimo;
    Double      vlr_maximo;

    public Alarme(){
        super();
    }

    public Alarme(Integer id_alarme, Integer id_dispositivo, Integer id_topico, Double vlr_minimo, Double vlr_maximo){
        super();
        setId_dispositivo(id_dispositivo);
        setId_topico(id_topico);
        this.fl_ativo    = "N";
        this.vlr_minimo  = vlr_minimo;
        this.vlr_maximo  = vlr_maximo;
    }

    public Integer getId_alarme() {
        return id_alarme;
    }

    public void setId_alarme(Integer id_alarme) {
        this.id_alarme = id_alarme;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public void setId_dispositivo(Integer id_dispositivo){
        this.dispositivo = new ConexaoWS().buscaDispositivo(id_dispositivo);
    }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public void setId_topico(Integer id_topico){
        this.topico = new ConexaoWS().buscaTopico(id_topico);
    }

    public String getFl_ativo() {
        return fl_ativo;
    }

    public void setFl_ativo(String fl_ativo) {
        this.fl_ativo = fl_ativo;
    }

    public Double getVlr_minimo() {
        return vlr_minimo;
    }

    public void setVlr_minimo(Double vlr_minimo) {
        this.vlr_minimo = vlr_minimo;
    }

    public Double getVlr_maximo() {
        return vlr_maximo;
    }

    public void setVlr_maximo(Double vlr_maximo) {
        this.vlr_maximo = vlr_maximo;
    }

    public void setValorAtributo(String atributo, String valor) {
        switch (atributo){
            case "id_alarme"     : setId_alarme(EfsUtil.StringToInteger(valor)); break;
            case "id_dispositivo": setId_dispositivo(EfsUtil.StringToInteger(valor)); break;
            case "id_topico"     : setId_topico(EfsUtil.StringToInteger(valor)); break;
            case "fl_ativo"      : setFl_ativo(valor); break;
            case "vlr_minimo"    : setVlr_minimo(EfsUtil.StringToDouble(valor)); break;
            case "vlr_maximo"    : setVlr_maximo(EfsUtil.StringToDouble(valor)); break;
        }
    }
}
