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
public class Config_Dashboard extends EstruturaBase{
    
    Integer id_usuario;
    Integer id_topico;
    Integer id_dispositivo;
    String  tp_visao;
    Integer ordenacao;
    String  configuracao;
    
    public Config_Dashboard(){
        super();
    }
    
    public Config_Dashboard(Integer id_usuario, Integer id_topico, Integer id_dispositivo, String tp_visao, Integer ordenacao, String configuracao){
        super();
        this.id_usuario     = id_usuario;
        this.id_topico      = id_topico;
        this.id_dispositivo = id_dispositivo;
        this.tp_visao       = tp_visao;
        this.ordenacao      = ordenacao;
        this.configuracao  = configuracao;
    }
    
    public Config_Dashboard (Map<String, Object> registro){
        super();
        this.atualizarAtributos(registro);
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
    
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Integer getId_topico() {
        return id_topico;
    }

    public void setId_topico(Integer id_topico) {
        this.id_topico = id_topico;
    }

    public Integer getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(Integer id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
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
    

    @Override
    public String getNomeTabela() {
        return "comunica.config_dashboard";
    }

    @Override
    public Map<String, Object> getPK() {
        Map<String, Object> pk = new HashMap();
        pk.put("id_usuario"    , getId_usuario());
        pk.put("id_topico"     , getId_topico());
        pk.put("id_dispositivo", getId_dispositivo());
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
        all.put("tp_visao"     , getTp_visao());
        all.put("ordenacao"    , getOrdenacao());
        all.put("configuracao" , getConfiguracao());
        return all;
    }
    
    @Override
    public EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException {
        Config_Dashboard topico = new Config_Dashboard();
        try{
            topico.setId_usuario(rs.getInt("id_usuario"));
            topico.setId_topico(rs.getInt("id_topico"));
            topico.setId_dispositivo(rs.getInt("id_dispositivo"));
            topico.setTp_visao(rs.getString("tp_visao"));
            topico.setOrdenacao(rs.getInt("ordenacao"));
            topico.setConfiguracao(rs.getString("configuracao"));
        } catch (SQLException e){
            throw new EfsException(e);
        }        
        return topico;
    }

    @Override
    public void setValorAtributo(String atributo, String valor) {        
        switch (atributo){
            case "id_usuario"    : setId_usuario(EfsUtil.StringToInteger(valor));     break;
            case "id_topico"     : setId_topico(EfsUtil.StringToInteger(valor));      break;
            case "id_dispositivo": setId_dispositivo(EfsUtil.StringToInteger(valor)); break;
            case "tp_visao"      : setTp_visao(valor);                                break;
            case "ordenacao"     : setOrdenacao(EfsUtil.StringToInteger(valor));      break;
            case "configuracao"  : setConfiguracao(valor);                            break;
        }        
    }        

    @Override
    public String getValorAtributo(String atributo) {
        switch (atributo){
            case "id_usuario"    : return EfsUtil.IntegerToString(getId_usuario());
            case "id_topico"     : return EfsUtil.IntegerToString(getId_topico());
            case "id_dispositivo": return EfsUtil.IntegerToString(getId_dispositivo());
            case "tp_visao"      : return getTp_visao();
            case "ordenacao"     : return EfsUtil.IntegerToString(getOrdenacao());
            case "configuracao"  : return getConfiguracao();
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
        setValorAtributo("id_usuario"    , null);
        setValorAtributo("id_topico"     , null);
        setValorAtributo("id_dispositivo", null);
        setValorAtributo("tp_visao"      , null);
        setValorAtributo("ordenacao"     , null);
        setValorAtributo("configuracao"  , null);
    }

    @Override
    public EstruturaBase clone() throws CloneNotSupportedException  {
        /*É necessário somente quando tem atributos não primitivos na classe, mas deixei aqui no caso de copiar essa classe
          Para tipos não primitivos, por exemplo o atributo Estado na Classe Cidade, tem que implementar esse método e ainda 
          usar o Estado.clone(), se não vai copiar somente a referência do Objeto e não seus atributos*/        
        return super.clone();
    }
}
