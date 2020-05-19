/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS;

import EFS.estruturas.Dispositivo;
import EFS.estruturas.Topico;
import EFS.uteis.EfsException;
import java.sql.SQLException;
import EFS.mqtt.clienteMQTT;
import EFS.mqtt.Ouvinte;
/**
 *
 * @author Leandro Rasch
 */
public class EFS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*Topico d1 = new Topico(1, "TEMPERA");
        Topico d2 = new Topico(2, "UMIDADE");
        try {
          System.out.println(d1.getSQLInsert().toString());
          d1.getSQLInsert().execute();
          System.out.println(d2.getSQLInsert().toString());
          d2.getSQLInsert().execute();
        } catch (EfsException e){
            System.out.println("Erro: " + e.getMensagem());
        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
        }*/
        clienteMQTT cliente = new clienteMQTT("tcp://192.168.2.108:1883", "", "");
        cliente.iniciar();
        Ouvinte ouvinte = new Ouvinte(cliente, "ESP01tempera", 2);
        Ouvinte ouvinte2 = new Ouvinte(cliente, "ESP01umidade", 2);
    }
    
}
