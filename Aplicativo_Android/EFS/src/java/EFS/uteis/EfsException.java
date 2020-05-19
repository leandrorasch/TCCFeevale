/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.uteis;

/**
 *
 * @author Leandro Rasch
 */
public class EfsException extends Exception{
    
    private final String nomeClasse;
    private final String mensagem;
    
    public EfsException(Exception e){
        String s = e.getClass().getName(); 
        int pos;
        while ((pos = s.indexOf(".")) >= 0){            
            s = s.substring(pos + 1);            
        }
        this.nomeClasse = s;
        this.mensagem   = e.getMessage();
    }
    
    public EfsException(String mensagem){
        this.nomeClasse = this.getClass().getName();
        this.mensagem   = mensagem;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public String getMensagem() {
        return mensagem;
    }        
}
