/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.conexao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/**
 *
 * @author Leandro Rasch
 */
public class ParametrosSGBD {
    
	private static ParametrosSGBD mySelf;  
	private Properties parametros;        
	
	private ParametrosSGBD() throws IOException {
		parametros = new Properties();
		carregaParametros();
	}
	
	public static ParametrosSGBD getInstance() throws IOException {
		
		if( mySelf == null ) {
			mySelf = new ParametrosSGBD();
		}
		
		return mySelf;
	}
	
	public void setParametro( String chave, String valor ) {
		parametros.setProperty( chave, valor );
	}
	
	public String getParametro( String chave ) {
		return parametros.getProperty( chave );
	}

	private void carregaParametros() throws IOException {

		File arquivo = new File( "ParametrosSGBD.xml" );
		
		if( arquivo.exists() ) {
			FileInputStream fis = new FileInputStream( arquivo );
			
			try {
				parametros.loadFromXML( fis );
			} finally {
				fis.close();
			}
		}
	}

	public void salvaParametros() throws IOException {

		File arquivo = new File( "ParametrosSGBD.xml" );
		FileOutputStream fos = new FileOutputStream( arquivo );
		
		try {
			parametros.storeToXML( fos, "Parametros do SGBD" );
		} finally {
			fos.close();
		}
	}
}
