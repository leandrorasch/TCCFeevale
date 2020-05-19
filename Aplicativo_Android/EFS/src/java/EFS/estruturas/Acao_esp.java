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
public class Acao_esp extends EstruturaBase{
    
    Integer     id_acao_esp;
    Dispositivo dispositivo;
    Usuario     usuario;
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
        this.sugerirPK = true;
        atualizaPK();
        this.dispositivo = new Dispositivo(id_dispositivo);
        this.topico = new Topico(id_topico);
        this.usuario = new Usuario(id_usuario);
        this.valor = valor;
        this.dt_evento = EfsUtil.getDataAtualTimestamp();
        this.tp_status = "P";
    }
    public Acao_esp (Integer id_acao){
        super();
        this.id_acao_esp = id_acao;
        Map<String, Object> a = registros.busca(this);
        atualizarAtributos(registros.busca(this));
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

    public void setId_dispositivo(Integer id_dispositivo) {
        this.dispositivo = new Dispositivo(id_dispositivo);
    }
    
    public Dispositivo getDispositivo(){
        return dispositivo;
    }
    
    public void setDispositivo(Dispositivo dispositivo){
        this.dispositivo = dispositivo;
    }

    public Integer getId_usuario() {
        if (usuario == null){
            return null;
        }
        return usuario.getId_usuario();
    }

    public void setId_usuario(Integer id_usuario) {
        this.usuario = new Usuario(id_usuario);
    }
    
    public Usuario getUsuario(){
        return usuario;
    }
    
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public Integer getId_topico() {
        if (topico == null){
            return null;
        }
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
    
    @Override
    public String getNomeTabela() {
        return "comunica.acao_esp";
    }

    @Override
    public Map<String, Object> getPK() {
        Map<String, Object> pk = new HashMap();
        pk.put("id_acao_esp", getId_acao_esp());
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
        all.put("id_usuario"    , getId_usuario());
        all.put("id_topico"     , getId_topico());
        all.put("dt_evento"     , getDt_evento());
        all.put("valor"         , getValor());
        all.put("tp_status"     , getTp_status());
        all.put("dt_confirmacao", getDt_confirmacao());
        return all;
    }
    
    @Override
    public EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException {
        Acao_esp acao = new Acao_esp();
        try{
            acao.setId_acao_esp(rs.getInt("id_acao_esp"));
            acao.setId_dispositivo(rs.getInt("id_dispositivo"));
            acao.setId_usuario(rs.getInt("id_usuario"));
            acao.setId_topico(rs.getInt("id_topico"));
            acao.setDt_evento(EfsUtil.stringToTimestamp(rs.getString("dt_evento")));
            acao.setValor(rs.getString("valor"));
            acao.setTp_status(rs.getString("tp_status"));
            acao.setDt_confirmacao(EfsUtil.stringToTimestamp(rs.getString("dt_confirmacao")));
        } catch (SQLException e){
            throw new EfsException(e);
        }        
        return acao;
    }

    @Override
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

    @Override
    public String getValorAtributo(String atributo) {
        switch (atributo){
            case "id_acao_esp"   : return EfsUtil.IntegerToString(getId_acao_esp());
            case "id_dipositivo" : return EfsUtil.IntegerToString(getId_dispositivo());
            case "id_usuario"    : return EfsUtil.IntegerToString(getId_usuario());
            case "id_topico"     : return EfsUtil.IntegerToString(getId_topico());
            case "dt_evento"     : return EfsUtil.TimestampToString(getDt_evento());
            case "valor"         : return getValor();
            case "tp_status"     : return getTp_status();
            case "dt_confirmacao": return EfsUtil.TimestampToString(getDt_confirmacao());
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
        setValorAtributo("id_acao_esp"   , null);
        setValorAtributo("id_dispositivo", null);
        setValorAtributo("id_usuario"    , null);
        setValorAtributo("id_topico"     , null);
        setValorAtributo("dt_evento"     , null);
        setValorAtributo("valor"         , null);
        setValorAtributo("tp_status"     , null);
        setValorAtributo("dt_confirmacao", null);
    }

    @Override
    public EstruturaBase clone() throws CloneNotSupportedException  {
        /*É necessário somente quando tem atributos não primitivos na classe, mas deixei aqui no caso de copiar essa classe
          Para tipos não primitivos, por exemplo o atributo Estado na Classe Cidade, tem que implementar esse método e ainda 
          usar o Estado.clone(), se não vai copiar somente a referência do Objeto e não seus atributos*/        
        return super.clone();
    }
}
