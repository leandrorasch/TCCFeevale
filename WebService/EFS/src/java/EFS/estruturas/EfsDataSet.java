/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.estruturas;

import EFS.uteis.EfsException;
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
public class EfsDataSet {
    
    
    private ArrayList<Map<String, Object>> registros;
    private int regAtual;    
    
    public EfsDataSet(){
        registros  = new ArrayList();                
    }
    
    public EfsDataSet(ResultSet rs) throws EfsException{
        this();
        setRegistros(rs);
    }
    
    /*Vai copiar os dados do ResultSet para o Array. Além de ser meio perigoso 
      trabalhar com o ResultSet (por conseguir faz operações diretas no banco),
      também consume recursos do BD, deixando ele mais lento*/
    public void setRegistros(ResultSet rs) throws EfsException{ 
        regAtual = -1;
        registros.clear();
        Map<String, Object> linha;
        if (rs != null){            
            try{
                try{    
                    ResultSetMetaData meta = rs.getMetaData();                    
                    rs.beforeFirst();
                    while (rs.next()){
                        linha = new LinkedHashMap();
                        for (int i = 1; i <= meta.getColumnCount(); i++){
                            linha.put(meta.getColumnName(i), rs.getObject(i));
                        }
                        registros.add(linha);
                    }   
                } finally {
                    rs.close();//Enquanto o ResultSet estiver aberto, vai consumir recursos do BD
                }
            } catch (SQLException e){
                throw new EfsException(e);
            } 
        }
    }
    
    public void setRegistros(ArrayList<Map<String, Object>> registros){
        this.registros = registros;
    }
    
    public void deletaRegistro(){
        registros.remove(regAtual);
    }
    
    public void atualizaRegistro(EstruturaBase c){
        Map<String, Object> linha = registros.get(regAtual);
        for (String s : linha.keySet()){
            linha.replace(s, c.getValorAtributo(s));
        }
    }  
    
    public void insereRegistro(Map<String, Object> novo){
        registros.add(novo);
    }
    
    public void limpar(){
        registros.clear();
    }
            
    
    private Map<String, Object> getLinha(Integer linha){
        Map<String, Object> retorno = null;
        if (registros.size() > 0){
            if (registros.size() > linha && linha >= 0){
                regAtual = linha;
                retorno = registros.get(linha);
            }
        } else {            
            regAtual = -1;
        }   
        return retorno;
    }
    
    public Boolean estaVazio(){
        return registros.isEmpty();
    }
    
    public Boolean estaNoPrimeiro(){
        return regAtual == 0;
    }
    
    public Boolean estaNoUltimo(){
        return regAtual == registros.size() - 1;
    }
    
    public int getSize(){
        return registros.size();
    }
    
    public Map<String, Object> getAtual(){
        return getLinha(regAtual);
    }
    
    public Map<String, Object> getPrimeiro(){                
        return getLinha(0);        
    }
    
    public Map<String, Object> getAnterior(){        
        return getLinha(regAtual-1);
    }
    
    public Map<String, Object> getProximo(){        
        return getLinha(regAtual+1);
    }
    
    public Map<String, Object> getUltimo(){        
        return getLinha(registros.size() - 1);
    }
    
    public Map<String, Object> get(Integer i, Boolean atualizarPonteiro){
        if (atualizarPonteiro)
            regAtual = i;
        return getLinha(i);
    }
    
    public ArrayList<Map<String, Object>> getRegistros() {
        return registros;
    }
    
    public void copiaDados(EfsDataSet copia){
        //Tentei usar esse método, mas dá erro de concurrentModification (Testar na tTorneiosCampeonato: incluir, cancelar, incluir novamente)
        Map<String, Object> linha;        
        ArrayList<Map<String, Object>> regCopia = copia.getRegistros();
        for (Map<String, Object> reg : registros){
            linha = new LinkedHashMap();
            for (String s : reg.keySet())
                linha.put(s, reg.get(s));
            regCopia.add(linha);
        }
    }
}
