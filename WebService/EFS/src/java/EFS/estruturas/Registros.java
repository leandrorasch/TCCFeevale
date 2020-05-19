/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.estruturas;

import EFS.conexao.Conexao;
import EFS.uteis.EfsException;
import EFS.uteis.EfsUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Leandro Rasch
 */
public class Registros {
    
    protected EstruturaBase atual;
    protected final EfsDataSet registros;
    
    public Registros (){
        this.registros = new EfsDataSet();
    }
    
    public Registros (EstruturaBase atual){
        this.registros = new EfsDataSet();
        this.atual = atual;
    }
    
    public EstruturaBase getAtual(){
        return atual;
    }
    
    public void setAtual(EstruturaBase atual){
        this.atual = atual;
    }
    
    public void busca(){
        try {
            registros.setRegistros(atual.getSQLSelect().executeQuery());
        } catch (EfsException e) {
            System.out.println("Erro: " + e);
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }        
    }       
    
    /*public EstruturaBase busca(EstruturaBase b1){
        busca();
        Map<String, Object> linha = registros.getAtual();
        atual.atualizarAtributos(linha);
        return atual;
    }*/
    
    public Map<String, Object> busca(EstruturaBase atual){
        this.atual = atual;
        busca();
        return registros.getProximo();
    }
    
    public ArrayList<Dispositivo> buscaDispositivoUsuario (Integer id_usuario) throws SQLException, EfsException{
        ArrayList<Dispositivo> retorno = new ArrayList();
        
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setSugerirPK(false);
        dispositivo.setId_usuario(id_usuario);
        
        registros.setRegistros(dispositivo.getSQLSelect().executeQuery());
        
        ArrayList<Map<String, Object>> lista = registros.getRegistros();
        
        for (int i = 0; i < lista.size(); i++){
            Dispositivo d = new Dispositivo();
            d.atualizarAtributos(lista.get(i));
            retorno.add(d);
        }
        
        return retorno;
    }
    
    public ArrayList<Topico> buscaTopicos (Integer id_dispositivo) throws SQLException, EfsException{
        ArrayList<Topico> retorno = new ArrayList();
        
        String comando;
        
        if (id_dispositivo > 0){
            comando = "select distinct id_topico from comunica.evento_sensor where id_dispositivo = " + EfsUtil.IntegerToString(id_dispositivo);
        }else{
            comando = "select distinct id_topico from comunica.topico where tp_topico = 'S'";
        }
                
        PreparedStatement p = Conexao.getInstance().getPrepareStatement(comando);   
        
        registros.setRegistros(p.executeQuery());
        
        ArrayList<Map<String, Object>> lista = registros.getRegistros();
        
        for (int i = 0; i < lista.size(); i++){
            Topico t = new Topico(EfsUtil.StringToInteger(String.valueOf(lista.get(i).get(lista.get(i).keySet()))));
            retorno.add(t);
        }
        
        return retorno;
    }
    
    public ArrayList<Config_Dashboard> buscaConfigDashboard(Integer id_usuario, Integer id_topico, Integer id_dispositivo) throws SQLException, EfsException{
        ArrayList<Config_Dashboard> retorno = new ArrayList();
        
        String comando = "select id_usuario, id_topico, id_dispositivo from comunica.config_dashboard where 1 = 1 ";
        
        if (id_usuario > 0    ){ comando = comando + " and id_usuario = "     + EfsUtil.IntegerToString(id_usuario); }
        if (id_topico > 0     ){ comando = comando + " and id_topico = "      + EfsUtil.IntegerToString(id_topico); }
        if (id_dispositivo > 0){ comando = comando + " and id_dispositivo = " + EfsUtil.IntegerToString(id_dispositivo); }
                
        
        PreparedStatement p = Conexao.getInstance().getPrepareStatement(comando);   
        
        registros.setRegistros(p.executeQuery());
        
        ArrayList<Map<String, Object>> lista = registros.getRegistros();
        
        for (int i = 0; i < lista.size(); i++){
            Config_Dashboard cd = new Config_Dashboard(lista.get(i));
            retorno.add(cd);
        }
        
        return retorno;
    }    
    
    public ArrayList<Map<String, Object>> executaComando (String ds_comando) throws SQLException, EfsException{
        PreparedStatement p = Conexao.getInstance().getPrepareStatement(ds_comando);
        registros.setRegistros(p.executeQuery());
        return registros.getRegistros();
    }
    
    public ArrayList<Alarme> buscaAlarmesPorUsuario(Integer id_usuario) throws SQLException, EfsException{
        ArrayList<Alarme> retorno = new ArrayList();
        
        String comando = "SELECT a.id_alarme "                        +
                         "  from comunica.alarme a"                   +
                         "     , comunica.dispositivo d"              +
                         " where a.id_dispositivo = d.id_dispositivo" +
                         "   and d.id_usuario = "                     + id_usuario.toString();
        
        PreparedStatement p = Conexao.getInstance().getPrepareStatement(comando);   
        
        registros.setRegistros(p.executeQuery());
        
        ArrayList<Map<String, Object>> lista = registros.getRegistros();
        
        for (int i = 0; i < lista.size(); i++){
            Alarme cd = new Alarme(lista.get(i));
            retorno.add(cd);
        }
        
        return retorno;
    }    
    
    
}
