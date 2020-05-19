package com.example.efs.estruturas;

import com.example.efs.uteis.EfsUtil;

import java.util.Date;

public class Topico {

        Integer id_topico;
        String ds_topico;
        Date dt_inclusao;
        String fl_ativo;
        Date dt_alteracao;

        public Topico(){
            super();
        }

        public Topico(Integer id_topico, String ds_topico){
            super();
            this.id_topico = id_topico;
            this.ds_topico = ds_topico;
            this.dt_inclusao = EfsUtil.getDataAtual();;
            this.fl_ativo = "S";
            this.dt_alteracao = EfsUtil.getDataAtual();;
        }

        public Integer getId_topico() {
            return id_topico;
        }

        public void setId_topico(Integer id_topico) {
            this.id_topico = id_topico;
        }

        public String getDs_topico() {
            return ds_topico;
        }

        public void setDs_topico(String ds_topico) {
            this.ds_topico = ds_topico;
        }

        public Date getDt_inclusao() {
            return dt_inclusao;
        }

        public void setDt_inclusao(Date dt_inclusao) {
            this.dt_inclusao = dt_inclusao;
        }

        public String getFl_ativo() {
            return fl_ativo;
        }

        public void setFl_ativo(String fl_ativo) {
            this.fl_ativo = fl_ativo;
        }

        public Date getDt_alteracao() {
            return dt_alteracao;
        }

        public void setDt_alteracao(Date dt_alteracao) {
            this.dt_alteracao = dt_alteracao;
        }

        public void setValorAtributo(String atributo, String valor) {
            switch (atributo){
                case "id_topico"   : setId_topico(EfsUtil.StringToInteger(valor)); break;
                case "ds_topico"   : setDs_topico(valor); break;
                case "dt_inclusao" : setDt_inclusao(EfsUtil.stringToDate(valor)); break;
                case "fl_ativo"    : setFl_ativo(valor); break;
                case "dt_alteracao": setDt_alteracao(EfsUtil.stringToDate(valor)); break;
            }
        }

}
