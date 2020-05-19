/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.mqtt;

import EFS.estruturas.Dispositivo;
import EFS.estruturas.Topico;
import EFS.uteis.EfsUtil;
import static EFS.uteis.EfsUtil.completeToLeft;
import static EFS.uteis.EfsUtil.completeToRight;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Leandro Rasch
 */
public class PadraoMQTT {
    private static final String  STATUS_ENVIO        = "0";
    private static final String  STATUS_OK           = "1";
    private static final String  STATUS_ERRO         = "2";
    private static final String  SEPARADOR           = ";";
    private static final Integer TAMANHO_TOPICO      = 7;
    private static final Integer TAMANHO_DISPOSITIVO = 5;
    private static final Integer TAMANHO_INTEIRO     = 10;
    private static final Integer CASAS_DECIMAIS      = 2;

    public static String getSTATUS_ENVIO() {
        return STATUS_ENVIO;
    }

    public static String getSTATUS_OK() {
        return STATUS_OK;
    }

    public static String getSTATUS_ERRO() {
        return STATUS_ERRO;
    }
    
    public static String getSeparador(){
        return SEPARADOR;
    }
    
    public static String formataTopico(String ds_topico){ return EfsUtil.completeToRight(ds_topico, " ", TAMANHO_TOPICO); }
    public static String formataDispositivo(String id_dispositivo){ return EfsUtil.completeToLeft(id_dispositivo, "0", TAMANHO_DISPOSITIVO);}
    public static String formataDispositivo(Integer id_dispositivo){ return EfsUtil.completeToLeft(id_dispositivo.toString(), "0", TAMANHO_DISPOSITIVO);}
    public static String formataInteger(Integer id){ return EfsUtil.completeToLeft(id.toString(), "0", TAMANHO_INTEIRO);}
    
    public static String formataDouble(Double v){
                
        String valor = String.format("%."+CASAS_DECIMAIS+"f", v).replace(",", ".");
        
        return valor;
    }
    
    public static Integer getTamanhoDouble(){
        return TAMANHO_INTEIRO + CASAS_DECIMAIS + 1;
    }
    
    public static Map<String, String> topicoRetorno (String topico){
        Map<String, String> retorno = new HashMap();
        
        String[] campos = topico.split(getSeparador());
        
        if (campos.length > 1){
            retorno.put("dispositivo", campos[0]);
            retorno.put("topico"     , campos[1]);
        } else {
            retorno.put("topico", campos[0]);
        }
        
        return retorno;
    }
    
    public static Map<String, String> mensagemRetorno(Topico topico, MqttMessage mensagem){
        Map<String, String> retorno = new HashMap();
        
        
        String mensagemFinal = new String(mensagem.getPayload());
        mensagemFinal.replace(".", ",");
        
        String[] campos = mensagemFinal.split(SEPARADOR);
        
        retorno.put("status", campos[0]);
        
        if (topico.getTp_topico().equals("S")){
            retorno.put("mensagem", campos[0]);
        }else if (topico.getDs_topico().toUpperCase().equals("ALARME")){
            retorno.put("id_alarme", campos[1]);
        }else if (topico.getDs_topico().toUpperCase().equals("CONFIG")){
            retorno.put("id_config", campos[1]);
        }else if (topico.getDs_topico().toUpperCase().equals("ACAO")){
            retorno.put("id_acao", campos[1]);
        }
        return retorno;
    }
}
