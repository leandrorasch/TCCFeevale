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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Leandro Rasch
 */
public class EventoSensor extends EstruturaBase{
    
    Integer id_evento;
    Integer id_dispositivo;
    Integer id_topico;
    Timestamp dt_evento;
    Double valor;
    
    public EventoSensor(){
        super();
    }
    
    public EventoSensor(Integer id_dispositivo, Integer id_topico, Double valor){
        super();
        this.sugerirPK = true;
        atualizaPK();
        this.id_dispositivo = id_dispositivo;
        this.id_topico = id_topico;
        this.valor = valor;
        this.dt_evento = EfsUtil.getDataAtualTimestamp();
    }

    public Integer getId_evento() {
        return id_evento;
    }

    public void setId_evento(Integer id_evento) {
        this.id_evento = id_evento;
    }

    public Integer getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(Integer id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public Integer getId_topico() {
        return id_topico;
    }

    public void setId_topico(Integer id_topico) {
        this.id_topico = id_topico;
    }

    public Timestamp getDt_evento() {
        return dt_evento;
    }

    public void setDt_evento(Timestamp dt_evento) {
        this.dt_evento = dt_evento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    @Override
    public String getNomeTabela() {
        return "comunica.evento_sensor";
    }

    @Override
    public Map<String, Object> getPK() {
        Map<String, Object> pk = new HashMap();
        pk.put("id_evento", getId_evento());
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
        all.put("dt_evento"     , getDt_evento());
        all.put("valor"         , getValor());
        return all;
    }
    
    @Override
    public EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException {
        EventoSensor evento = new EventoSensor();
        try{
            evento.setId_evento(rs.getInt("id_evento"));
            evento.setId_dispositivo(rs.getInt("id_dispositivo"));
            evento.setId_topico(rs.getInt("id_topico"));
            evento.setDt_evento(EfsUtil.stringToTimestamp(rs.getString("dt_evento")));
            evento.setValor(rs.getDouble("valor"));
        } catch (SQLException e){
            throw new EfsException(e);
        }        
        return evento;
    }

    @Override
    public void setValorAtributo(String atributo, String valor) {        
        switch (atributo){
            case "id_evento"     : setId_evento(EfsUtil.StringToInteger(valor)); break;
            case "id_dispositivo": setId_dispositivo(EfsUtil.StringToInteger(valor)); break;
            case "id_topico"     : setId_topico(EfsUtil.StringToInteger(valor)); break;
            case "dt_evento"     : setDt_evento(EfsUtil.stringToTimestamp(valor)); break;
            case "valor"         : setValor(EfsUtil.StringToDouble(valor)); break;
        }        
    }        

    @Override
    public String getValorAtributo(String atributo) {
        switch (atributo){
            case "id_evento"    : return EfsUtil.IntegerToString(getId_evento());
            case "id_dipositivo": return EfsUtil.IntegerToString(getId_dispositivo());
            case "id_topico"    : return EfsUtil.IntegerToString(getId_topico());
            case "dt_evento"    : return EfsUtil.TimestampToString(getDt_evento());
            case "valor"        : return EfsUtil.DoubleToString(getValor());
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
        setValorAtributo("id_evento"     , null);
        setValorAtributo("id_dispositivo", null);
        setValorAtributo("id_topico"     , null);
        setValorAtributo("dt_evento"     , null);
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
