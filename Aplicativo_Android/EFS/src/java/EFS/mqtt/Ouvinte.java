/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.mqtt;

import EFS.estruturas.Acao_esp;
import EFS.estruturas.Alarme;
import EFS.estruturas.Dispositivo;
import EFS.estruturas.EventoSensor;
import EFS.estruturas.Registros;
import EFS.estruturas.Topico;
import EFS.uteis.EfsException;
import EFS.uteis.EfsUtil;
import EFS.mqtt.PadraoMQTT;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
/**
 *
 * @author Leandro Rasch
 */
public class Ouvinte implements IMqttMessageListener{
    
    Integer id = 1;
    String topico;
    
    public Ouvinte(clienteMQTT cliente, String topico, int qos){
        cliente.subscribe(qos, this, topico);
        this.topico = topico;
    }
    /*
    public void mensagemantiga(String topico, MqttMessage mensagem) throws Exception{
        
        //System.out.print("\nMensagem recebida: ");
        String device = topico.substring(0,5);
        String topic  = topico.substring(5);
        String mensagemFinal = new String(mensagem.getPayload());
        mensagemFinal.replace(".", ",");
        //System.out.print(" - Device: " + device);
        //System.out.print(" - Tópico: " + topic);
        //System.out.print(" - Mensagem: " + mensagemFinal);
        
        Dispositivo dispositivo = new Dispositivo(device);
        //System.out.print(" - Local: " + dispositivo.getLocal());
        
        Topico topi = new Topico(topic.toUpperCase());
        //System.out.print(" - Topico: " + topi.getDs_topico());
        
        EventoSensor evento = new EventoSensor(dispositivo.getId_dispositivo(), topi.getId_topico(), EfsUtil.StringToDouble(mensagemFinal));
        try{
            evento.getSQLInsert().execute();
        //    System.out.println(evento.getSQLInsert().toString());
        //    System.out.print("Evento inserido");
        }catch (SQLException e){
            System.out.println(evento.getSQLInsert().toString());
            System.out.println("Erro ao inserir evento - ID: "          + evento.getId_evento() +
                                                       " Dispositivo: " + evento.getId_dispositivo() +
                                                       " Tópico: "      + evento.getId_topico() + 
                                                       " Valor: "       + evento.getValor() +
                                                       " - "            + e.getMessage());
        }
    }*/

    @Override
    public void messageArrived(String topico, MqttMessage mensagem) throws Exception {
        
        //if (this.topico != null){
        //    mensagemantiga(topico, mensagem);
        //}else{

        try{
            //System.out.print("\nMensagem recebida: ");
            Map<String, String> t = PadraoMQTT.topicoRetorno(topico);
            
            Topico      topic       = new Topico(t.get("topico").toUpperCase());
            
            Map<String, String> m = PadraoMQTT.mensagemRetorno(topic, mensagem);            
            
            //System.out.print(" - Device: " + device);
            //System.out.print(" - Tópico: " + topic.getDs_topico());
            //System.out.print(" - Mensagem: " + mensagemFinal);

            //System.out.print(" - Local: " + dispositivo.getLocal());

            //System.out.print(" - Topico: " + topi.getDs_topico());

            if (topic.getTp_topico().equals("S")){
                Dispositivo dispositivo = new Dispositivo(EfsUtil.StringToInteger(t.get("dispositivo")));
                EventoSensor evento = new EventoSensor(dispositivo.getId_dispositivo(), topic.getId_topico(), EfsUtil.StringToDouble(m.get("mensagem")));
                try{
                    evento.getSQLInsert().execute();
                //    System.out.println(evento.getSQLInsert().toString());
                //    System.out.print("Evento inserido");
                }catch (SQLException e){
                    System.out.println(evento.getSQLInsert().toString());
                    System.out.println("Erro ao inserir evento - ID: "          + evento.getId_evento() +
                                                               " Dispositivo: " + evento.getId_dispositivo() +
                                                               " Tópico: "      + evento.getId_topico() + 
                                                               " Valor: "       + evento.getValor() +
                                                               " - "            + e.getMessage());
                }
            } else {
                if (topic.getDs_topico().equals("ACAO")){
                    //System.out.println("STATUS: "+m.get("status"));
                    if (m.get("status").equals("1")){
                        Acao_esp acao = new Acao_esp(EfsUtil.StringToInteger(m.get("id_acao")));
                        acao.setDt_confirmacao(EfsUtil.getDataAtualTimestamp());
                        acao.setTp_status("C");
                        try{
                            acao.getSQLUpdate().execute();
                        } catch (SQLException e){
                            System.out.println("Erro ao atualizar ação: " + acao.getSQLUpdate().toString() + e.getMessage());
                        }
                    }
                }else if (topic.getDs_topico().equals("ALARME")){
                    if (m.get("status").equals("1")){
                        Alarme alarme = new Alarme(EfsUtil.StringToInteger(m.get("id_alarme")));
                        alarme.setTp_confirmacao("C");
                        alarme.setDt_confirmacao(EfsUtil.getDataAtualTimestamp());
                        try{
                            alarme.getSQLUpdate().execute();
                        }catch (SQLException e){
                            System.out.println("Erro ao atualizar alarme: " + alarme.getSQLUpdate().toString() + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e){
            System.out.println("Erro ao receber mensagem: " + e.getMessage());
        }
    }
}
