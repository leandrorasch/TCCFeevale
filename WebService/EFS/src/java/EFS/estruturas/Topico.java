/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.estruturas;

import EFS.uteis.EfsException;
import EFS.uteis.EfsUtil;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Leandro Rasch
 */
public class Topico extends EstruturaBase{
    
    Integer id_topico;
    String ds_topico;
    Date dt_inclusao;
    String fl_ativo;
    Date dt_alteracao;
    String tp_topico;
    
    public Topico(){
        super();
    }
    
    public Topico(Integer id_topico, String ds_topico, String tp_topico){
        super();
        this.id_topico = id_topico;
        this.ds_topico = ds_topico;
        this.dt_inclusao = EfsUtil.getDataAtual();
        this.fl_ativo = "S";
        this.dt_alteracao = EfsUtil.getDataAtual();
        this.tp_topico = tp_topico;
    }
    
    public Topico (String ds_topico){
        super();
        this.ds_topico = ds_topico;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
    }
    
    public Topico (Integer id_topico){
        super();
        this.id_topico = id_topico;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
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

    public String getTp_topico() {
        return tp_topico;
    }

    public void setTp_topico(String tp_topico) {
        this.tp_topico = tp_topico;
    }
    
    @Override
    public String getNomeTabela() {
        return "comunica.topico";
    }

    @Override
    public Map<String, Object> getPK() {
        Map<String, Object> pk = new HashMap();
        pk.put("id_topico", getId_topico());
        return pk;
    }

    @Override
    protected Map<String, Object> getCampos(Boolean comPk) {
        Map<String, Object> all;
        if (comPk){
            all = getPK();
        } else {
            all = new HashMap();
        }
        all.put("ds_topico"   , getDs_topico());
        all.put("dt_inclusao" , getDt_inclusao());
        all.put("fl_ativo"    , getFl_ativo());
        all.put("dt_alteracao", getDt_alteracao());
        all.put("tp_topico"   , getTp_topico());
        return all;
    }
    
    @Override
    public EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException {
        Topico topico = new Topico();
        try{
            topico.setId_topico(rs.getInt("id_topico"));
            topico.setDs_topico(rs.getString("ds_topico"));
            topico.setDt_inclusao(EfsUtil.stringToDate(rs.getString("dt_inclusao")));
            topico.setFl_ativo(rs.getString("fl_ativo"));
            topico.setDt_alteracao(EfsUtil.stringToDate(rs.getString("dt_alteracao")));
            topico.setTp_topico(rs.getString("tp_topico"));
        } catch (SQLException e){
            throw new EfsException(e);
        }        
        return topico;
    }

    @Override
    public void setValorAtributo(String atributo, String valor) {        
        switch (atributo){
            case "id_topico"   : setId_topico(EfsUtil.StringToInteger(valor)); break;
            case "ds_topico"   : setDs_topico(valor); break;
            case "dt_inclusao" : setDt_inclusao(EfsUtil.stringToDate(valor)); break;
            case "fl_ativo"    : setFl_ativo(valor); break;
            case "dt_alteracao": setDt_alteracao(EfsUtil.stringToDate(valor)); break;
            case "tp_topico"   : setTp_topico(valor); break;
        }        
    }        

    @Override
    public String getValorAtributo(String atributo) {
        switch (atributo){
            case "id_topico"   : return EfsUtil.IntegerToString(getId_topico());
            case "ds_topico"   : return getDs_topico();
            case "dt_inclusao" : return EfsUtil.dateToString(getDt_inclusao());
            case "fl_ativo"    : return getFl_ativo();
            case "dt_alteracao": return EfsUtil.dateToString(getDt_alteracao());
            case "tp_topico"   : return getTp_topico();
        }
        return null;
    }

    @Override
    public void atualizarAtributos(Map<String, Object> registro) {
        for (String s : registro.keySet()){
            setValorAtributo(s, String.valueOf(registro.get(s)));            
        }
    }

    @Override
    public void limparAtributos() {
        setValorAtributo("id_topico"   , null);
        setValorAtributo("ds_topico"   , null);
        setValorAtributo("dt_inclusao" , null);
        setValorAtributo("fl_ativo"    , null);
        setValorAtributo("dt_alteracao", null);
        setValorAtributo("tp_topico"   , null);
    }

    @Override
    public EstruturaBase clone() throws CloneNotSupportedException  {
        /*É necessário somente quando tem atributos não primitivos na classe, mas deixei aqui no caso de copiar essa classe
          Para tipos não primitivos, por exemplo o atributo Estado na Classe Cidade, tem que implementar esse método e ainda 
          usar o Estado.clone(), se não vai copiar somente a referência do Objeto e não seus atributos*/        
        return super.clone();
    }
}
