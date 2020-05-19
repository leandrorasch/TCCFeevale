/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.conexao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import EFS.uteis.EfsException;
/**
 *
 * @author Leandro Rasch
 */
public class Conexao {
    
    private static Conexao mySelf; 
    private Connection cnx;       

    public static Conexao getInstance() {
        if( mySelf == null ) {
            try {
                mySelf = new Conexao();
            } catch (EfsException e) {                
                System.out.println("Não foi possível conectar ao banco de dados: " + e.getMessage());
                return null;
            }
        }
        return mySelf;
    }

    private Conexao() throws EfsException {        
        try {    
            ParametrosSGBD p = ParametrosSGBD.getInstance();
            Class.forName( p.getParametro( "driverJDBC" ) );
            String urlBanco = p.getParametro( "urlBanco" );
            String nomeUsuario = p.getParametro( "nomeUsuario" );
            String senha = p.getParametro( "senhaBanco" );
            cnx = DriverManager.getConnection( urlBanco, nomeUsuario, senha );        
        } catch(ClassNotFoundException | IOException |SQLException e){
            throw new EfsException(e);
        }
    }

    public PreparedStatement getPrepareStatement(String cmd) {
        try {
            return cnx.prepareStatement(cmd, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
	
    public ResultSet executaQuery(String select) throws EfsException{
        ResultSet rs = null;
        try{
           rs = cnx.prepareStatement(select).executeQuery();
        } catch (SQLException e){           
           throw new EfsException(e);
        }
        return rs;
    }
	
    public void desconecta() {
        try {
            mySelf = null;
            cnx.close();
        } catch (SQLException e) {
            e.printStackTrace();			
        }
    }
	
    public Connection getConnection() {
        return cnx;
    }
}
