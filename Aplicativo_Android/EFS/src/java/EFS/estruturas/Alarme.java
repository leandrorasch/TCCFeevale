/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.estruturas;

import EFS.mqtt.clienteMQTT;
import EFS.uteis.EfsException;
import EFS.uteis.EfsUtil;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Leandro Rasch
 */
public class Alarme extends EstruturaBase{
    
    Integer     id_alarme;
    Dispositivo dispositivo = new Dispositivo();
    Topico      topico      = new Topico();
    String      fl_ativo;
    Double      vlr_minimo;
    Double      vlr_maximo;
    String      tp_confirmacao;
    Timestamp   dt_confirmacao;
    Topico      topico_acao;
    String      valor;
    
    public Alarme(){
        super();
    }
    
    public Alarme(Integer id_dispositivo, Integer id_topico, Double vlr_minimo, Double vlr_maximo, Integer id_topico_acao, String valor){
        super();
        this.sugerirPK   = true;
        atualizaPK();
        this.dispositivo = new Dispositivo(id_dispositivo);
        this.topico      = new Topico(id_topico);
        this.fl_ativo    = "N";
        this.vlr_minimo  = vlr_minimo;
        this.vlr_maximo  = vlr_maximo;
        this.topico_acao = new Topico(id_topico_acao);
        this.valor       = valor;
    }

    public Alarme (Integer id_alarme){
        super();
        this.id_alarme = id_alarme;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
    }

    public Alarme (Map<String, Object> registro){
        super();
        this.atualizarAtributos(registro);
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
    }
    
    public Integer getId_alarme() {
        return id_alarme;
    }

    public void setId_alarme(Integer id_alarme) {
        this.id_alarme = id_alarme;
    }

    public Integer getId_dispositivo() {
        return dispositivo.getId_dispositivo();
    }

    public void setId_dispositivo(Integer id_dispositivo) {
        this.dispositivo = new Dispositivo(id_dispositivo);
    }
    
    public Dispositivo getDispositivo(){
        return dispositivo;
    }
    
    public void setDispositivo(Dispositivo dispositivo){
        this.dispositivo = dispositivo;
    }

    public Integer getId_topico() {
        return topico.getId_topico();
    }

    public void setId_topico(Integer id_topico) {
        this.topico = new Topico(id_topico);
    }
    
    public Topico getTopico(){
        return topico;
    }
    
    public void setTopico(Topico topico){
        this.topico = topico;
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

    public String getTp_confirmacao() {
        return tp_confirmacao;
    }

    public void setTp_confirmacao(String tp_confirmacao) {
        this.tp_confirmacao = tp_confirmacao;
    }

    public Timestamp getDt_confirmacao() {
        return dt_confirmacao;
    }

    public void setDt_confirmacao(Timestamp dt_confirmacao) {
        this.dt_confirmacao = dt_confirmacao;
    }

    public Topico getTopico_acao() {
        return topico_acao;
    }

    public void setTopico_acao(Topico topico_acao) {
        this.topico_acao = topico_acao;
    }
    
    public Integer getId_topico_acao(){
        if (topico_acao == null){
            return null;
        }
        return topico_acao.getId_topico();
    }

    public void setId_topico_acao(Integer id_topico_acao){
        this.topico_acao = new Topico(id_topico_acao);
    }
    
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    @Override
    public String getNomeTabela() {
        return "comunica.alarme";
    }

    @Override
    public Map<String, Object> getPK() {
        Map<String, Object> pk = new HashMap();
        pk.put("id_alarme", getId_alarme());
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
        all.put("id_dispositivo", getId_dispositivo());
        all.put("id_topico"     , getId_topico());
        all.put("fl_ativo"      , getFl_ativo());
        all.put("vlr_minimo"    , getVlr_minimo());
        all.put("vlr_maximo"    , getVlr_maximo());
        all.put("tp_confirmacao", getTp_confirmacao());
        all.put("dt_confirmacao", getDt_confirmacao());
        all.put("topico_acao"   , getId_topico_acao());
        all.put("valor"         , getValor());
        return all;
    }
    
    @Override
    public EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException {
        Alarme acao = new Alarme();
        try{
            acao.setId_alarme(rs.getInt("id_alarme"));
            acao.setId_dispositivo(rs.getInt("id_dispositivo"));
            acao.setId_topico(rs.getInt("id_topico"));
            acao.setFl_ativo(rs.getString("fl_ativo"));
            acao.setVlr_minimo(rs.getDouble("vlr_minimo"));
            acao.setVlr_maximo(rs.getDouble("vlr_maximo"));
            acao.setTp_confirmacao(rs.getString("tp_confirmacao"));
            acao.setDt_confirmacao(EfsUtil.stringToTimestamp(rs.getString("dt_confirmacao")));
            acao.setId_topico(rs.getInt("topico_acao"));
            acao.setValor(rs.getString("valor"));
        } catch (SQLException e){
            throw new EfsException(e);
        }        
        return acao;
    }

    @Override
    public void setValorAtributo(String atributo, String valor) {        
        switch (atributo){
            case "id_alarme"     : setId_alarme(EfsUtil.StringToInteger(valor)); break;
            case "id_dispositivo": setId_dispositivo(EfsUtil.StringToInteger(valor)); break;
            case "id_topico"     : setId_topico(EfsUtil.StringToInteger(valor)); break;
            case "fl_ativo"      : setFl_ativo(valor); break;
            case "vlr_minimo"    : setVlr_minimo(EfsUtil.StringToDouble(valor)); break;
            case "vlr_maximo"    : setVlr_maximo(EfsUtil.StringToDouble(valor)); break;
            case "tp_confirmacao": setTp_confirmacao(valor); break;
            case "dt_confirmacao": setDt_confirmacao(EfsUtil.stringToTimestamp(valor)); break;
            case "topico_acao"   : setId_topico_acao(EfsUtil.StringToInteger(valor)); break;
            case "valor"         : setValor(valor); break;
        }        
    }        

    @Override
    public String getValorAtributo(String atributo) {
        switch (atributo){
            case "id_alarme"     : return EfsUtil.IntegerToString(getId_alarme());
            case "id_dipositivo" : return EfsUtil.IntegerToString(getId_dispositivo());
            case "id_topico"     : return EfsUtil.IntegerToString(getId_topico());
            case "fl_ativo"      : return getFl_ativo();
            case "vlr_minimo"    : return EfsUtil.DoubleToString(getVlr_minimo());
            case "vlr_maximo"    : return EfsUtil.DoubleToString(getVlr_minimo());
            case "tp_confirmacao": return getTp_confirmacao();
            case "dt_confirmacao": return EfsUtil.TimestampToString(getDt_confirmacao());
            case "topico_acao"   : return EfsUtil.IntegerToString(getId_topico_acao());
            case "valor"         : return getValor();
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
        setValorAtributo("id_alarme"     , null);
        setValorAtributo("id_dispositivo", null);
        setValorAtributo("id_topico"     , null);
        setValorAtributo("fl_ativo"      , null);
        setValorAtributo("vlr_minimo"    , null);
        setValorAtributo("vlr_maximo"    , null);
        setValorAtributo("tp_confirmacao", null);
        setValorAtributo("dt_confirmacao", null);
        setValorAtributo("topico_acao"   , null);
        setValorAtributo("valor"         , null);
    }

    @Override
    public EstruturaBase clone() throws CloneNotSupportedException  {
        /*É necessário somente quando tem atributos não primitivos na classe, mas deixei aqui no caso de copiar essa classe
          Para tipos não primitivos, por exemplo o atributo Estado na Classe Cidade, tem que implementar esse método e ainda 
          usar o Estado.clone(), se não vai copiar somente a referência do Objeto e não seus atributos*/        
        return super.clone();
    }
}
