/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.estruturas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import EFS.uteis.EfsException;
import EFS.uteis.EfsUtil;
import EFS.conexao.Conexao;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Leandro Rasch
 */
public abstract class EstruturaBase implements Cloneable{
    
    protected Boolean sugerirPK = false;
    Registros registros;
    /*Os métodos abaixo deverão ser implementados pelos herdeiros dessa classe*/
    public abstract String getNomeTabela();//Nome da tabela que a classe referencia 
    public abstract Map<String, Object> getPK();
    protected abstract Map<String, Object> getCampos(Boolean comPk);
    public abstract EstruturaBase resultSetToEstruturaBase(ResultSet rs) throws EfsException;
    public abstract void setValorAtributo(String atributo, String valor);
    public abstract String getValorAtributo(String atributo);    
    public abstract void limparAtributos();   
    
     public EstruturaBase(){
         registros = new Registros();
    
    }
    
    @Override
    public EstruturaBase clone() throws CloneNotSupportedException {
        return (EstruturaBase) super.clone();
    }
    
    public Boolean getSugerirPK(){
        return sugerirPK;
    }
    
    public void atualizarAtributos(Map<String, Object> registro){
        for (String s : registro.keySet()){
            setValorAtributo(s, String.valueOf(registro.get(s)));            
        }
    }
    
    protected void atualizaPK() {        
        if (getSugerirPK()){
            Map<String, Object> pk = getPK();
            if (pk.size() == 1)
                for (String s: pk.keySet())
                    try {
                        ResultSet rs = Conexao.getInstance().getPrepareStatement("SELECT nextval('comunica.sq_"+s+"') ").executeQuery();
                        if (rs.next()){                
                            setValorAtributo(s, rs.getString(1));
                        } else {
                            setValorAtributo(s, "1");
                        }
                    } catch (SQLException ex) {
                        System.out.println("Erro ao buscar o próximo código:" + ex.getMessage());
                    }                
        }
    }
    
    public PreparedStatement getSQLInsert() throws EfsException{        
        //Vai montar o SQL de Insert com "?" representando os parametros 
        StringBuilder insert       = new StringBuilder();
        Map<String, Object> campos = getCampos(true);
        removeEntradasNulas(campos);
        insert.append("INSERT INTO ");
        insert.append(getNomeTabela());
        insert.append("(");        
        insert.append(keySetToString(campos.keySet(), ",", ","));
        insert.append(") VALUES (");
        insert.append(EfsUtil.deletaCharFinal(EfsUtil.repeteString("?,", campos.keySet().size()), 1));
        insert.append(");");          
        Conexao cnx = Conexao.getInstance();
        if (cnx != null){
            PreparedStatement st = cnx.getPrepareStatement(insert.toString());
            int i = 1;
            for (String s : campos.keySet()){
                try{
                    setValorParametro(st, i, campos.get(s));
                } catch(SQLException e){
                    throw new EfsException(e);
                }
                i++;
            }
            return st;
        }
        return null;        
    }
    
    public PreparedStatement getSQLUpdate() throws EfsException{
        StringBuilder update = new StringBuilder(); 
        Map<String, Object> set = getCampos(false);
        Map<String, Object> pk  = getPK();  
        removeEntradasNulas(set);
        update.append("UPDATE ");
        update.append(getNomeTabela());
        update.append(" SET ");
        update.append(keySetToString(set.keySet(), " = ?,", ","));
        update.append(" WHERE ");
        update.append(keySetToString(pk.keySet(), " = ? AND ", "AND "));
        update.append(";");        
        Conexao cnx = Conexao.getInstance();
        if (cnx != null){
            PreparedStatement st = cnx.getPrepareStatement(update.toString());
            int i = 1;
            //Não dá para confiar na ordem em que o HashMap coloca novos valores, por isso tem que fazer dois for separados
            for (String s : set.keySet()){
                try {
                    setValorParametro(st, i, set.get(s));
                } catch (SQLException ex) {
                    throw new EfsException(ex);
                }
                i++;
            }
            for (String s : pk.keySet()){
                try {
                    setValorParametro(st, i, pk.get(s));
                } catch (SQLException ex) {
                    throw new EfsException(ex);
                }
                i++;
            }
            return st;
        }
        return null;
    }
        
    public PreparedStatement getSQLDelete() throws EfsException{
        StringBuilder delete      = new StringBuilder();
        Map<String, Object> where = getPK();
        delete.append("DELETE FROM ");
        delete.append(getNomeTabela());
        delete.append(" WHERE ");
        delete.append(keySetToString(where.keySet(), " = ? AND ", "AND "));
        delete.append(";");         
        Conexao cnx = Conexao.getInstance();
        if (cnx != null){
            PreparedStatement st = cnx.getPrepareStatement(delete.toString());
            int i = 1;
            for (String s : where.keySet()){
                try {
                    setValorParametro(st, i, where.get(s));
                } catch (SQLException ex) {
                    throw new EfsException(ex);
                }
                i++;
            }
            return st;
        }
        return null;
    }
    
    public PreparedStatement getSQLSelect() throws EfsException{
        StringBuilder select = new StringBuilder();
        Map<String, Object> campos = getCampos(true);
        select.append("SELECT * FROM ");
        select.append(getNomeTabela());
        removeEntradasNulas(campos);
        select.append(EfsUtil.whereSQL(keySetToString(campos.keySet(), " = ? AND ", "AND ")));        
        select.append(" ORDER BY 1;");        
        Conexao cnx = Conexao.getInstance();
        if (cnx != null){
            PreparedStatement st = cnx.getPrepareStatement(select.toString());
            int i = 1;
            for (String s : campos.keySet()){
                try {
                    setValorParametro(st, i, campos.get(s));
                } catch (SQLException ex) {
                    throw new EfsException(ex);
                }
                i++;
            }
            return st;
        }
        return null;
    }
    
    public void setValorParametro(PreparedStatement st, int indice, Object valor) throws SQLException{        
        if (valor instanceof String){
            st.setString(indice, String.valueOf(valor));
        } else if (valor instanceof Integer) {
            st.setInt(indice, Integer.parseInt(String.valueOf(valor)));
        } else if (valor instanceof Date){
            st.setDate(indice, (Date) valor);
        } else if (valor instanceof Double){
            st.setDouble(indice, (Double) valor);
        } else if (valor instanceof Timestamp){
            st.setTimestamp(indice, (Timestamp) valor);
        }
            
    }
    
    public void removeEntradasNulas(Map<String, Object> valores){
        ArrayList<String> exclusoes = new ArrayList();
        for (String s : valores.keySet()){
            if (EfsUtil.ehNulo(valores.get(s))){
                exclusoes.add(s);
            }
        }
        for (String x : exclusoes){
            valores.remove(x);
        }
    }
    
    public String keySetToString(Set<String> ks, String delimitador, String deletarFinal){
        String retorno = "";
        for (String key : ks){
            retorno += key + delimitador;      
        }
        if (EfsUtil.getStringNula(retorno))
            return retorno;
        return EfsUtil.deletaCharFinal(retorno, deletarFinal.length());
        
    }
}
