/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.estruturas;

import EFS.uteis.EfsUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import EFS.uteis.EfsException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Leandro Rasch
 */
public class Dispositivo extends EstruturaBase{

    private Integer id_dispositivo;
    private String ds_dispositivo;
    private Integer id_usuario;
    private String fl_ativo;
    private String aplicacao_uso;
    private String local;
    private Date   dt_cadastro;
    
    public Dispositivo(){
        super();
        this.sugerirPK = true;
    }
    
    public Dispositivo(Integer id_dispositivo){
        this();
        this.id_dispositivo = id_dispositivo;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
    }
    
    public Dispositivo (String ds_dispositivo){
        super();
        this.ds_dispositivo = ds_dispositivo;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
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
    
    public void setSugerirPK(boolean sugerirPK){
        this.sugerirPK = sugerirPK;
    }

    
    
    @Override
    public String getNomeTabela() {
        return "comunica.dispositivo";
    }

    @Override
    public Map<String, Object> getPK() {
        Map<String, Object> pk = new HashMap();
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
        all.put("ds_dispositivo", getDs_dispositivo());
        all.put("fl_ativo"      , getFl_ativo());
        all.put("aplicacao_uso" , getAplicacao_uso());
        all.put("local"         , getLocal());
        all.put("dt_cadastro"   , getDt_cadastro());
        all.put("id_usuario"    , getId_usuario());
        return all;
    }
    
    @Override
    public EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException {
        Dispositivo dispositivo = new Dispositivo();
        try{
            dispositivo.setId_dispositivo(rs.getInt("id_dispositivo"));
            dispositivo.setDs_dispositivo(rs.getString("ds_dispositivo"));
            dispositivo.setFl_ativo(rs.getString("fl_ativo"));
            dispositivo.setLocal(rs.getString("local"));
            dispositivo.setDt_cadastro(EfsUtil.stringToDate(rs.getString("dt_cadastro")));
            dispositivo.setId_usuario(EfsUtil.StringToInteger(rs.getString("id_usuario")));
        } catch (SQLException e){
            throw new EfsException(e);
        }        
        return dispositivo;
    }

    @Override
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

    @Override
    public String getValorAtributo(String atributo) {
        switch (atributo){
            case "id_dispositivo": return EfsUtil.IntegerToString(getId_dispositivo());
            case "ds_dispositivo": return getDs_dispositivo();
            case "fl_ativo"      : return getFl_ativo();
            case "local"         : return getLocal();
            case "dt_cadastro"   : return EfsUtil.dateToString(getDt_cadastro());
            case "id_usuario"    : return EfsUtil.IntegerToString(getId_usuario());
            case "aplicacao_uso" : return getAplicacao_uso();
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
        setValorAtributo("id_dispositivo", null);
        setValorAtributo("ds_dispositivo", null);
        setValorAtributo("fl_ativo"      , null);
        setValorAtributo("local"         , null);
        setValorAtributo("dt_cadastro"   , null);
        setValorAtributo("id_usuario"    , null);
        setValorAtributo("aplicacao_uso" , null);
    }

    @Override
    public EstruturaBase clone() throws CloneNotSupportedException  {
        /*É necessário somente quando tem atributos não primitivos na classe, mas deixei aqui no caso de copiar essa classe
          Para tipos não primitivos, por exemplo o atributo Estado na Classe Cidade, tem que implementar esse método e ainda 
          usar o Estado.clone(), se não vai copiar somente a referência do Objeto e não seus atributos*/        
        return super.clone();
    }
    
}
