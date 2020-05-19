package com.example.efs.estruturas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.example.efs.conexao.ConexaoWS;
import com.example.efs.uteis.EfsUtil;

import java.sql.Timestamp;

/**
 *
 * @author Leandro Rasch
 */
public class Acao_esp {

        Integer     id_acao_esp;
        Dispositivo dispositivo;
        Integer     usuario;
        Topico      topico;
        String      valor;
        Timestamp   dt_evento;
        String      tp_status;
        Timestamp   dt_confirmacao;

        public Acao_esp(){
            super();
        }

        public Acao_esp(Integer id_dispositivo, Integer id_usuario, Integer id_topico, String valor){
            super();
            setId_dispositivo(id_dispositivo);
            setId_topico(id_topico);
            this.usuario = id_usuario;
            this.valor = valor;
            this.dt_evento = EfsUtil.getDataAtualTimestamp();
            this.tp_status = "P";
        }

        public Integer getId_acao_esp() {
            return id_acao_esp;
        }

        public void setId_acao_esp(Integer id_acao_esp) {
            this.id_acao_esp = id_acao_esp;
        }

        public Integer getId_dispositivo() {
            if (dispositivo == null){
                return null;
            }
            return dispositivo.getId_dispositivo();
        }

        public void setId_dispositivo(Integer id_dispositivo){
            this.dispositivo = new ConexaoWS().buscaDispositivo(id_dispositivo);
        }

        public Dispositivo getDispositivo(){
            return dispositivo;
        }

        public void setDispositivo(Dispositivo dispositivo){
            this.dispositivo = dispositivo;
        }

        public Integer getId_usuario() {
            return usuario;
        }

        public void setId_usuario(Integer id_usuario) {
            usuario = id_usuario;
        }

        public Integer getId_topico() {
            if (topico == null){
                return null;
            }
            return topico.getId_topico();
        }

        public void setId_topico(Integer id_topico){
            this.topico = new ConexaoWS().buscaTopico(id_topico);
        }

        public Topico getTopico(){
            return topico;
        }

        public void setTopico(Topico topico){
            this.topico = topico;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public Timestamp getDt_evento() {
            return dt_evento;
        }

        public void setDt_evento(Timestamp dt_evento) {
            this.dt_evento = dt_evento;
        }

        public String getTp_status() {
            return tp_status;
        }

        public void setTp_status(String tp_status) {
            this.tp_status = tp_status;
        }

        public Timestamp getDt_confirmacao() {
            return dt_confirmacao;
        }

        public void setDt_confirmacao(Timestamp dt_confirmacao) {
            this.dt_confirmacao = dt_confirmacao;
        }

        public void setValorAtributo(String atributo, String valor) {
            switch (atributo){
                case "id_acao_esp"   : setId_acao_esp(EfsUtil.StringToInteger(valor)); break;
                case "id_dispositivo": setId_dispositivo(EfsUtil.StringToInteger(valor)); break;
                case "id_usuario"    : setId_usuario(EfsUtil.StringToInteger(valor)); break;
                case "id_topico"     : setId_topico(EfsUtil.StringToInteger(valor)); break;
                case "dt_evento"     : setDt_evento(EfsUtil.stringToTimestamp(valor)); break;
                case "valor"         : setValor(valor); break;
                case "tp_status"     : setTp_status(valor); break;
                case "dt_confirmacao": setDt_confirmacao(EfsUtil.stringToTimestamp(valor)); break;
            }
        }
}
