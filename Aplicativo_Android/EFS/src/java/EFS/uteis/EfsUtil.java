/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.uteis;

import java.awt.Component;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import EFS.uteis.EfsException;
import java.sql.Timestamp;

/**
 *
 * @author Leandro Rasch
 */
public class EfsUtil {
    
    public static final String FORMATO_DATA = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATO_DATA_SQL = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATO_DATA_SQL_CURTO = "yyyy-MM-dd";
    
    
    public static Date getDataAtual(){
        DateFormat formato = new SimpleDateFormat(FORMATO_DATA_SQL);
        java.util.Date dataUtil = new java.util.Date();
        String data = formato.format(dataUtil);
        try{
          return new Date(formato.parse(data).getTime());
        } catch (ParseException e){
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }
    
    public static Timestamp getDataAtualTimestamp(){
        DateFormat formato = new SimpleDateFormat(FORMATO_DATA_SQL);
        java.util.Date dataUtil = new java.util.Date();
        String data = formato.format(dataUtil);
        try{
          return new Timestamp(formato.parse(data).getTime());
        } catch (ParseException e){
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }
    
    public static void mensagemErro(Component owner, String mensagem){
        System.out.println("Erro: " + mensagem);
    }
    
    public static void mensagem(Component owner, String msg){
        System.out.println("Aviso: " + msg);
    }
    
    public static Boolean getStringNula(String s){
        if (s == null)
            return true;
        if (s.equals("null")) return true;
        return s.trim().equals("");
    }
    
    public static Boolean getIntegerNulo(Integer i){
        return i.equals(0);
    }
    
    public static Boolean ehNulo(Object obj){
        if (obj == null){
            return true;
        }
        
        if (obj instanceof Integer){
            return getIntegerNulo(Integer.parseInt(String.valueOf(obj)));
        } else { //String
            return getStringNula(String.valueOf(obj));
        }
    }
    
    public static String whereSQL(String where){
        return seSenao(getStringNula(where), where, " WHERE " + where);
    }
    
    public static String seSenao(Boolean condicao, String valorVerdadeiro, String valorFalso){
        if (condicao){
            return valorVerdadeiro;
        } else {
            return valorFalso;
        }
    }
    
    public static Integer seSenao(Boolean condicao, Integer valorVerdadeiro, Integer valorFalso){
        if (condicao){
            return valorVerdadeiro;
        } else {
            return valorFalso;
        }
    }
    
    public static String dateToString(Date data){
        if (data == null)
            return "";
        SimpleDateFormat df = new SimpleDateFormat(FORMATO_DATA);
        return df.format(data);
    }     
    
    public static String TimestampToString(Timestamp data){
        if (data == null)
            return "";
        SimpleDateFormat df = new SimpleDateFormat(FORMATO_DATA);
        return df.format(data);
    }     
    
    public static String dateToStringSQL(Date data){        
        SimpleDateFormat df = new SimpleDateFormat(FORMATO_DATA_SQL);        
        return df.format(data);
    }   
    
    public static String TimestampToStringSQL(Timestamp data){        
        SimpleDateFormat df = new SimpleDateFormat(FORMATO_DATA_SQL);        
        return df.format(data);
    }
    
    public static Date stringToDate(String data){
        if (getStringNula(data))
            return null;        
        SimpleDateFormat format = new SimpleDateFormat(getFormatoData(data));
        try{
            Date dt = new Date(format.parse(data).getTime());
            return dt;
        } catch (ParseException ex){
            return null;
        }
    }
    
    public static Timestamp stringToTimestamp(String data){
        if (getStringNula(data))
            return null;        
        SimpleDateFormat format = new SimpleDateFormat(getFormatoData(data));
        try{
            Timestamp dt = new Timestamp(format.parse(data).getTime());
            return dt;
        } catch (ParseException ex){
            return null;
        }
    }
    
    public static String getFormatoData(String dataStr){
        if (dataStr.charAt(2) == '/' && dataStr.charAt(5) == '/'){
            return FORMATO_DATA;
        } else if (dataStr.charAt(4) == '-' && dataStr.charAt(7) == '-') {
            if (dataStr.length() > 11){
                return FORMATO_DATA_SQL;
            } else{
                return FORMATO_DATA_SQL_CURTO;
            }
        }
        return "";
    }
    
    public static Integer StringToInteger(String s){
        if (s == null || s.equals("") || s.equals("null")){
            return null;
        }
        return Integer.parseInt(s);
    }
    
    public static Integer StringToInteger(String s, Integer retornoSeNulo){
        if (s == null || s.equals("") || s.equals("null")){
            return retornoSeNulo;
        }
        return Integer.parseInt(s);
    }
    
    public static Float StringToFloat(String s){
        if (s == null || s.equals("") || s.equals("null")){
            return null;
        }
        return Float.parseFloat(s);
    }
    
    public static Float StringToFloat(String s, Float retornoSeNulo){
        if (s == null || s.equals("")){
            return retornoSeNulo;
        }
        return Float.parseFloat(s);
    }
    
    public static Double StringToDouble(String s){
        if (s == null || s.equals("") || s.equals("null")){
            return null;
        }
        return Double.parseDouble(s);
    }
    
    public static String IntegerToString(Integer i){        
        if (i == null)
            return "";
        return String.valueOf(i);        
    }
    
    public static String FloatToString (Float i){
        if(i == null)
            return "";
        return String.valueOf(i);
    }
    
    public static String DoubleToString (Double i){
        if(i == null)
            return "";
        return String.valueOf(i);
    }
    
    public static String primeiraMaiscula(String s){
        return s.substring(0,1).toUpperCase().concat(s.substring(1));
    }
    
    public static String repeteString(String s, int vezes){
        String aux = "";
        for (int i = 0; i < vezes; i++){
            aux += s;
        } 
        return aux;
    }
    
    public static String deletaCharFinal(String s, Integer quantChar){
        return s.substring(0, s.length() - quantChar);
    }
    
    public static String BoolToString(Boolean b){
        return EfsUtil.seSenao(b, "S", "N");
    }
    
    public static Boolean StringToBool(String s){
        if (s == null)
            return false;
        return s.equals("S");
    }
        
    public static String getNomeCampoBD(String coluna) {        
        return primeiraMaiscula(coluna.substring(coluna.indexOf("_") + 1));
    }
    
    public static String completeToLeft(String value, String c, int size) {
	String result = value;

        while (result.length() < size) {
            result = c + result;
	}
	return result;
    }
    
    public static String completeToRight(String value, String c, int size){
        String result = value;
        
        while (result.length() < size){
            result = result + c;
        }
        return result;
    }

    public static String DoubleToString(String valor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
