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
public class Usuario extends EstruturaBase{
    
    Integer id_usuario;
    String  ds_usuario;
    String  ds_email;
    String  ds_senha;
    Date    dt_criacao;
    String  tp_status;
    
    public Usuario(){
        super();
    }
    
    public Usuario(String ds_usuario, String ds_email, String ds_senha){
        super();
        this.sugerirPK  = true;
        this.ds_usuario = ds_usuario;
        this.ds_email   = ds_email;
        this.ds_senha   = ds_senha;
        this.dt_criacao = EfsUtil.getDataAtual();
        this.tp_status  = "A";
        this.atualizaPK();
    }
    
    public Usuario (String ds_email, String ds_senha){
        super();
        this.ds_email         = ds_email;
        this.ds_senha         = ds_senha;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
    }
    
    public Usuario (Integer id_usuario){
        super();
        this.id_usuario = id_usuario;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
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

    
    
    @Override
    public String getNomeTabela() {
        return "comunica.usuario";
    }

    @Override
    public Map<String, Object> getPK() {
        Map<String, Object> pk = new HashMap();
        pk.put("id_usuario", getId_usuario());
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
        all.put("ds_usuario" , getDs_usuario());
        all.put("ds_email"   , getDs_email());
        all.put("ds_senha"   , getDs_senha());
        all.put("dt_criacao" , getDt_criacao());
        all.put("tp_status"  , getTp_status());
        return all;
    }
    
    @Override
    public EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException {
        Usuario topico = new Usuario();
        try{
            topico.setId_usuario(rs.getInt("id_usuario"));
            topico.setDs_usuario(rs.getString("ds_usuario"));
            topico.setDs_email(rs.getString("ds_email"));
            topico.setDs_senha(rs.getString("ds_senha"));
            topico.setDt_criacao(EfsUtil.stringToDate(rs.getString("dt_criacao")));
            topico.setTp_status(rs.getString("tp_status"));
        } catch (SQLException e){
            throw new EfsException(e);
        }        
        return topico;
    }

    @Override
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

    @Override
    public String getValorAtributo(String atributo) {
        switch (atributo){
            case "id_usuario": return EfsUtil.IntegerToString(getId_usuario());
            case "ds_usuario": return getDs_usuario();
            case "ds_email"  : return getDs_email();
            case "ds_senha"  : return getDs_senha();
            case "dt_criacao": return EfsUtil.dateToString(getDt_criacao());
            case "tp_status" : return getTp_status();
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
        setValorAtributo("id_usuario", null);
        setValorAtributo("ds_usuario", null);
        setValorAtributo("ds_email"  , null);
        setValorAtributo("ds_senha"  , null);
        setValorAtributo("dt_criacao", null);
        setValorAtributo("tp_status" , null);
    }

    @Override
    public EstruturaBase clone() throws CloneNotSupportedException  {
        /*É necessário somente quando tem atributos não primitivos na classe, mas deixei aqui no caso de copiar essa classe
          Para tipos não primitivos, por exemplo o atributo Estado na Classe Cidade, tem que implementar esse método e ainda 
          usar o Estado.clone(), se não vai copiar somente a referência do Objeto e não seus atributos*/        
        return super.clone();
    }
}
