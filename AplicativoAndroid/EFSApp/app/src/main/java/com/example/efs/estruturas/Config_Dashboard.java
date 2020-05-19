package com.example.efs.estruturas;

import com.example.efs.conexao.ConexaoWS;
import com.example.efs.uteis.EfsUtil;

public class Config_Dashboard {

        Integer     id_usuario;
        Topico      topico;
        Dispositivo dispositivo;
        String      tp_visao;
        Integer     ordenacao;
        String      configuracao;

        public Config_Dashboard(){
            super();
        }

        public Config_Dashboard(Integer id_usuario, Topico id_topico, Dispositivo id_dispositivo, String tp_visao, Integer ordenacao, String configuracao){
            super();
            this.id_usuario     = id_usuario;
            this.topico         = topico;
            this.dispositivo    = dispositivo;
            this.tp_visao       = tp_visao;
            this.ordenacao      = ordenacao;
            this.configuracao  = configuracao;
        }

        public Integer getId_usuario() {
            return id_usuario;
        }

        public void setId_usuario(Integer id_usuario) {
            this.id_usuario = id_usuario;
        }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispotivo) {
        this.dispositivo = dispotivo;
    }

    public String getTp_visao() {
            return tp_visao;
        }

        public void setTp_visao(String tp_visao) {
            this.tp_visao = tp_visao;
        }

        public Integer getOrdenacao() {
            return ordenacao;
        }

        public void setOrdenacao(Integer ordenacao) {
            this.ordenacao = ordenacao;
        }

        public String getConfiguracao() {
            return configuracao;
        }

        public void setConfiguracao(String configuracao) {
            this.configuracao = configuracao;
        }

    public void setValorAtributo(String atributo, String valor) {
        switch (atributo){
            case "id_usuario"    : setId_usuario(EfsUtil.StringToInteger(valor));                                    break;
            case "topico"        : setTopico(new ConexaoWS().buscaTopico(EfsUtil.StringToInteger(valor)));           break;
            case "dispositivo"   : setDispositivo(new ConexaoWS().buscaDispositivo(EfsUtil.StringToInteger(valor))); break;
            case "tp_visao"      : setTp_visao(valor);                                                               break;
            case "ordenacao"     : setOrdenacao(EfsUtil.StringToInteger(valor));                                     break;
            case "configuracao"  : setConfiguracao(valor);                                                           break;
        }
    }

}