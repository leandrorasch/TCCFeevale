/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EFS.server;

import EFS.estruturas.Acao_esp;
import EFS.estruturas.Alarme;
import EFS.estruturas.Config_Dashboard;
import EFS.estruturas.Dispositivo;
import EFS.estruturas.EstruturaBase;
import EFS.estruturas.Registros;
import EFS.estruturas.Topico;
import EFS.estruturas.Usuario;
import EFS.mqtt.Ouvinte;
import EFS.mqtt.PadraoMQTT;
import EFS.mqtt.clienteMQTT;
import EFS.uteis.EfsException;
import EFS.uteis.EfsUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Leandro Rasch
 */
@WebService(serviceName = "EfsService")
public class EfsService {
    clienteMQTT cliente;
    /**
     * This is a sample web service operation
     */
    public EfsService (){
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
    
        System.out.println("Iniciando MQTT");
        cliente = new clienteMQTT("tcp://192.168.2.116:1883", "", "");
        cliente.iniciar();
        //Ouvinte ouvinte = new Ouvinte(cliente, "ESP01tempera", 2);
        //Ouvinte ouvinte2 = new Ouvinte(cliente, "ESP01umidade", 2);
        
        //Ouvinte o1 = new Ouvinte(cliente, "1;alarme", 2);
        //Ouvinte o2 = new Ouvinte(cliente, "2;alarme", 2);
        //Ouvinte o3 = new Ouvinte(cliente, "1;configu", 2);
        //Ouvinte o4 = new Ouvinte(cliente, "2;configu", 2);
        //Ouvinte o5 = new Ouvinte(cliente, "1;push", 2);
        //Ouvinte o6 = new Ouvinte(cliente, "1;tempera", 2);
        //Ouvinte o7 = new Ouvinte(cliente, "1;umidade", 2);
        
        ArrayList<Ouvinte> ouvintes = cliente.criaOuvintes();
    }
    
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "buscaUsuario")
    public Usuario buscaUsuario(@WebParam(name = "email") String email, @WebParam(name = "senha") String senha){
        Usuario usuario = new Usuario(email, senha);
        return usuario;
    }
    
    @WebMethod(operationName = "cadastraUsuario")
    public Usuario cadastraUsuario(@WebParam(name = "nome") String nome, @WebParam(name = "email") String email, @WebParam(name = "senha") String senha){
        Usuario usuario = new Usuario(nome, email, senha);
        try{
            usuario.getSQLInsert().execute();
        }catch(EfsException e){
            System.out.println(e.getMessage());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return usuario;
    }
    
    @WebMethod(operationName = "buscaDispositivo")
    public Dispositivo buscaDispositivo(@WebParam(name = "id_dispositivo") Integer id_dispositivo){
        Dispositivo dispositivo = new Dispositivo(id_dispositivo);
        return dispositivo;
    }
    
    @WebMethod(operationName = "listaDispositivos")
    public ArrayList<Dispositivo> listaDispositivos(@WebParam(name = "id_usuario") Integer id_usuario){
        ArrayList<Dispositivo> retorno = new ArrayList();
        Registros r = new Registros();
        try{
            retorno = r.buscaDispositivoUsuario(id_usuario);
        } catch (SQLException e){
            System.out.println("Erro Busca Dispositivos por Usuario: " + e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro Busca Dispositivos por Usuario: " + e.getMessage());
        }
        
        return retorno;
    }
    
    @WebMethod(operationName = "buscaTopico")
    public Topico buscaTopico(@WebParam(name = "id_topico") Integer id_topico){
        Topico topico = new Topico(id_topico);
        return topico;
    }
    
    @WebMethod(operationName = "listaTopicosdoDispositivo")
    public ArrayList<Topico> listaTopicosdoDispositivo(@WebParam(name = "id_dispositivo") Integer id_dispositivo){
        ArrayList<Topico> retorno = new ArrayList();
        Registros r = new Registros();
        try{
            retorno = r.buscaTopicos(id_dispositivo);
        } catch (SQLException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMensagem());
        }
        return retorno;
    }
    
    @WebMethod(operationName = "listaTopicos")
    public ArrayList<Topico> listaTopicos(){
        ArrayList<Topico> retorno = new ArrayList();
        Registros r = new Registros();
        try{
            retorno = r.buscaTopicos(-1);
        } catch (SQLException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMensagem());
        }
        return retorno;
    }
    
    @WebMethod(operationName = "listaConfigDashboard")
    public ArrayList<Config_Dashboard> buscaConfigDashboard( @WebParam(name = "id_usuario")Integer id_usuario
                                                           , @WebParam(name = "id_topico") Integer id_topico
                                                           , @WebParam(name = "id_dispositivo") Integer id_dispositivo){
        ArrayList<Config_Dashboard> retorno = new ArrayList();
        Registros r = new Registros();
        try{
            retorno = r.buscaConfigDashboard(id_usuario, id_topico, id_dispositivo);
        } catch (SQLException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMensagem());
        }
        return retorno;
    }
    
    @WebMethod(operationName = "executaConsulta")
    public ArrayList<String> executaConsulta (@WebParam(name = "ds_comando") String ds_comando){
        ArrayList<String> retorno = new ArrayList();
        ArrayList<Map<String, Object>> lista = new ArrayList();
        try{
            lista = new Registros().executaComando(ds_comando);
        } catch (SQLException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro Busca Tópicos por Dispositivo: " + e.getMensagem());
        }
        
        for (int i = 0; i < lista.size(); i++){
            Map<String, Object> m = lista.get(i);
            
            for (String s : m.keySet()){
                retorno.add(String.valueOf(m.get(s)));
            }
        }
        
        return retorno;
    }
    
    @WebMethod(operationName = "enviaacao")
    public Acao_esp enviaAcao ( @WebParam(name = "id_dispositivo") Integer id_dispositivo
                              , @WebParam(name = "id_usuario"    ) Integer id_usuario
                              , @WebParam(name = "id_topico"     ) Integer id_topico
                              , @WebParam(name = "valor"         ) String valor){
        Acao_esp acao = null;
        try{
            acao = new Acao_esp(id_dispositivo, id_usuario, id_topico, valor);
            acao.getSQLInsert().execute();
            
            String topico = "acao";
            String mensagem = PadraoMQTT.getSTATUS_ENVIO()+PadraoMQTT.getSeparador()
                            + acao.getDispositivo().getId_dispositivo()+PadraoMQTT.getSeparador()
                            + acao.getId_acao_esp()+PadraoMQTT.getSeparador()
                            + acao.getTopico().getDs_topico().toLowerCase()+PadraoMQTT.getSeparador()
                            + acao.getValor();
            
            cliente.publicar(topico, mensagem.getBytes(), 2, true);
        }catch(EfsException e){
            System.out.println(e.getMessage());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        //gera retorno
        return acao;
    }
    
    @WebMethod(operationName = "buscaalarmes")
    public ArrayList<Alarme> buscaAlarmes (@WebParam(name = "id_usuario") Integer id_usuario){
        ArrayList<Alarme> alarmes = new ArrayList();
        
        Registros r = new Registros();
        try{
            alarmes = r.buscaAlarmesPorUsuario(id_usuario);
        } catch (SQLException e){
            System.out.println("Erro Busca Alarmes por Usuario: " + e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro Busca Alarmes por Usuario: " + e.getMessage());
        }
        return alarmes;
    }
    
    @WebMethod(operationName = "setaalarme")
    public Alarme setaAlarmes ( @WebParam(name = "id_alarme") Integer id_alarme
                              , @WebParam(name = "fl_ativo" ) String fl_ativo){
        Alarme alarme = null;
        try{
            alarme = new Alarme(id_alarme);
            alarme.setFl_ativo(fl_ativo);
            alarme.setTp_confirmacao("P");
            
            String topico   = "alarme";
            String mensagem = PadraoMQTT.getSTATUS_ENVIO()+PadraoMQTT.getSeparador()
                            + alarme.getDispositivo().getId_dispositivo()+PadraoMQTT.getSeparador()
                            + id_alarme+PadraoMQTT.getSeparador()
                            + alarme.getTopico().getDs_topico().toLowerCase()+PadraoMQTT.getSeparador()
                            + alarme.getVlr_minimo()+PadraoMQTT.getSeparador()
                            + alarme.getVlr_maximo()+PadraoMQTT.getSeparador()
                            + alarme.getFl_ativo()+PadraoMQTT.getSeparador()
                            + alarme.getTopico_acao().getDs_topico().toLowerCase() + PadraoMQTT.getSeparador()
                            + alarme.getValor();
            cliente.publicar(topico, mensagem.getBytes(), 2, true);
            
            System.out.println(alarme.getSQLUpdate().toString());
            alarme.getSQLUpdate().execute();
        } catch (SQLException e){
            System.out.println("Erro Busca Alarmes por Usuario: " + e.getMessage());
        } catch (EfsException e){
            System.out.println("Erro Busca Alarmes por Usuario: " + e.getMessage());
        }
        return alarme;
    }
    
}
