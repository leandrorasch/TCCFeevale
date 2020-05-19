/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.mqtt;

import EFS.estruturas.Registros;
import EFS.uteis.EfsException;
import EFS.uteis.EfsUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 *
 * @author Leandro Rasch
 */
public class clienteMQTT implements MqttCallbackExtended{
    
    private final String serverURI;
    private MqttClient   cliente;
    private final MqttConnectOptions mqttOptions;
    
    public clienteMQTT(String serverURI, String usuario, String senha){
        this.serverURI = serverURI;
        
        mqttOptions = new MqttConnectOptions();
        mqttOptions.setMaxInflight(200);
        mqttOptions.setConnectionTimeout(3);
        mqttOptions.setKeepAliveInterval(10);
        mqttOptions.setAutomaticReconnect(true);
        mqttOptions.setCleanSession(false);
        
        if (usuario != null && senha != null){
            mqttOptions.setUserName(usuario);
            mqttOptions.setPassword(senha.toCharArray());
        }
    }
    
    public ArrayList<Ouvinte> criaOuvintes(){
        ArrayList<Ouvinte> ouvintes = new ArrayList();
        String comando = "select t.ds_topico, d.id_dispositivo, td.qos "
                       + "from comunica.topico t, comunica.dispositivo d, comunica.topicodispositivo td "
                       + "where t.id_topico = td.id_topico and d.id_dispositivo = td.id_dispositivo and d.fl_ativo = 'S' and d.fl_ativo = 'S' and td.fl_ativo = 'S' "
                       + "union "
                       + "select t.ds_topico, -1 as id_dispositivo, 2 as qos "
                       + "from comunica.topico t "
                       + "where t.tp_topico = 'C' and t.fl_ativo = 'S'";
        
        try{
            ArrayList<Map<String, Object>> retorno = new Registros().executaComando(comando);
            
            for (int i = 0; i < retorno.size(); i++){
                String topico = "";
                if (!String.valueOf(retorno.get(i).get("id_dispositivo")).equals("-1")){
                    topico = String.valueOf(retorno.get(i).get("id_dispositivo"))+PadraoMQTT.getSeparador();
                }
                topico = topico + String.valueOf(retorno.get(i).get("ds_topico")).toLowerCase();
                
                Integer qos = EfsUtil.StringToInteger(String.valueOf(retorno.get(i).get("qos")));
                
                System.out.println("INSCRICAO EM TOPICO: " + topico + " COM QOS " + qos.toString());
                
                Ouvinte o = new Ouvinte(this, topico, qos);
                ouvintes.add(o);
            }
        } catch (SQLException e){
            System.out.println("Erro ao criar ouvintes: "+ e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro ao criar ouvintes: "+ e.getMessage());
        }
        return ouvintes;
    }
    
    public IMqttToken subscribe(int qos, IMqttMessageListener gestorMensagemMQTT, String... topicos){
        if (cliente == null || topicos.length == 0){
            return null;
        }
        int tamanho = topicos.length;
        int[] qoss = new int[tamanho];
        IMqttMessageListener[] listners = new IMqttMessageListener[tamanho];
        
        for (int i = 0; i < tamanho; i++){
            qoss[i] = qos;
            listners[i] = gestorMensagemMQTT;
        }
        try{
            return cliente.subscribeWithResponse(topicos, qoss, listners);
        } catch (MqttException ex){
            System.out.println(String.format("Erro ao se inscrever nos tópicos %s - %s", Arrays.asList(topicos), ex));
            return null;
        }
    }
    
    public void unsubscribe(String... topicos){
        if (cliente == null || !cliente.isConnected() || topicos.length == 0){
            return;
        }
        try{
            cliente.unsubscribe(topicos);
        } catch (MqttException e){
            System.out.println(String.format("Erro ao se desinscrever no tópico %s - %s", Arrays.asList(topicos), e));
        }
    }
    
    public void iniciar(){
        try {
            System.out.println("Conectando no broker MQTT em " + serverURI);
            cliente = new MqttClient(serverURI, String.format("cj_", System.currentTimeMillis(), new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir"))));
            cliente.setCallback(this);
            cliente.connect(mqttOptions);
        } catch (MqttException e){
            System.out.println("Erro ao se conectar ao broker mqtt " + serverURI + " - " + e);
        }
    }
    
    public void finalizar(){
        if (cliente == null || !cliente.isConnected()){
            return;
        }
        try{
            cliente.disconnect();
            cliente.close();
        } catch (MqttException e){
            System.out.println("Erro ao desconectar do broker mqtt - " + e);
        }
    }
    
    public synchronized void publicar(String topico, byte[] payload, int qos, boolean retained){
        try{
            if (!cliente.isConnected()){
                cliente.connect();
            }
            
            if (cliente.isConnected()){
                cliente.publish(topico.toLowerCase(), payload, qos, retained);
                System.out.println(String.format("Tópico %s publicado. %dB", topico, payload.length));
            }else{
                System.out.println("Cliente desconectado, não foi possível publicar o tópico " + topico);
            }
        } catch (MqttException e){
            System.out.println("Erro ao publicar " + topico + " - " + e);
        }
    }
    
    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("Conexão com o broker perdida -" + thrwbl);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        System.out.println("Cliente MQTT " + (reconnect ? "reconectado" : "conectado") + " com o broker " + serverURI);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
    }
    
}
