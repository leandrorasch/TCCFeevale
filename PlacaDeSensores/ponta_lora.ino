//includes de bibliotecas
#include <DHT.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include "heltec.h"

//Configuração GPIO
#define DHTPIN         13 //umidade_temperatura
#define RELEPIN        17
#define fator_microseg 1000000
#define potencia_lora  20 
#define BAND           915E6

#define INTERVALO 300000
long ultimo_envio = 0;

//basicas
int oled_x = 0;
int oled_y = 0;

//LoRa
const String GETDATA = "getdata";
const String SETDATA = "setdata";
#define SCK 5
#define MISO 19
#define MOSI 27
#define SS 18
#define RST 14
#define DI00 26

//MQTT
const String SEPARADOR     = ";";
const String SEPARADOR_TOP = "!";
#define DEVICE    "1"
#define STATUS_OK "1"

//Tópicos
#define TOP_TEMPERATURA "tempera"
#define TOP_UMIDADE     "umidade"
#define TOP_CONFIGU     "configu"
#define TOP_ALARME      "alarme"
#define TOP_ACAO        "acao"
#define TOP_RELE        "rele"

struct Alarme {
          String topico_alarme;
          String id_alarme = "0";
          float  vlr_minimo;
          float  vlr_maximo;
          String topico_acao;
          String valor;
        } alarmes[20];

//tipos e variáveis para temperatura
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
float temperatura_max;
float temperatura_min;
String acao = "acao";


void inicialilza_Lora();
void oled(boolean plimpa, String ptexto);
String float_para_string(float f1);
float string_para_float(String valor);
String int_para_string(int f1);
void trata_acao(String topico_acao, String valor);
void seta_alarme (String topico_alarme, String id_alarme, float  vlr_minimo, float  vlr_maximo, String topico_acao, String valor, String fl_ativo);
void executa_alarme(String topico, String valor);
void envia_lora (String pmensagem);
void recebe_lora ();
void atualiza_temperatura_max_e_minima(float temp_lida);
void escreve_temperatura_umidade_display(float temp_lida, float umid_lida);

//=====================================================
//=================== PROCESSOS =======================
//=====================================================

void inicialilza_Lora() {
  SPI.begin(SCK, MISO, MOSI, SS);
  LoRa.setPins(SS, RST, DI00);

  if (!LoRa.begin(BAND, true)){
    oled(false, "Erro ao inicializar o LoRa");
    delay(2000);
  }
  else{
    LoRa.enableCrc();
    LoRa.receive();
  }
}
void oled(boolean plimpa, String ptexto){
  if (plimpa == true) {
    Heltec.display-> clear();
    Heltec.display->setFont(ArialMT_Plain_10);
    oled_x = 0;
    oled_y = 0;
  }

  Heltec.display->drawString(oled_x, oled_y, ptexto);
  Heltec.display->display();
  oled_y = oled_y + 12;
}

String float_para_string(float f1) {
  char numero[25] = {0};
  sprintf(numero, "%.2f", f1);
  String retorno = "";
  for (int i = 0; i < 25; i++){
    retorno += numero[i];
  }
  return retorno;
}

float string_para_float(String valor){
  float retorno = atof(valor.c_str());
  return retorno;
}

String int_para_string(int f1) {
  char numero[25] = {0};
  sprintf(numero, "%d", f1);
  String retorno = "";
  for (int i = 0; i < 25; i++){
    retorno += numero[i];
  }
  return retorno;
}

void trata_acao(String topico_acao, String valor){
  if (topico_acao.equals(TOP_RELE)){
    if (valor.equals("D")){
      digitalWrite(RELEPIN, LOW);
    }else if (valor.equals("L")){
      digitalWrite(RELEPIN, HIGH);
    }
  }
}

//manipulação de alarmes
void seta_alarme (String topico_alarme, String id_alarme, float  vlr_minimo, float  vlr_maximo, String topico_acao, String valor, String fl_ativo){
  boolean processou = false;
  for (int i = 0; i < 20; i++){
    if (processou == false){
      if (alarmes[i].id_alarme.equals("0") || (alarmes[i].id_alarme.equals(id_alarme) && fl_ativo.equals("S"))){
        alarmes[i].topico_alarme = topico_alarme;
        alarmes[i].id_alarme     = id_alarme;
        alarmes[i].vlr_minimo    = vlr_minimo;
        alarmes[i].vlr_maximo    = vlr_maximo;
        alarmes[i].topico_acao   = topico_acao;
        alarmes[i].valor         = valor;
        processou = true;
      }else if (alarmes[i].id_alarme.equals(id_alarme)){
        alarmes[i].topico_alarme.clear();
        alarmes[i].id_alarme     = "0";
        alarmes[i].vlr_minimo    = 0;
        alarmes[i].vlr_maximo    = 0;
        alarmes[i].topico_acao.clear();
        alarmes[i].valor.clear();
        processou = true;
      }
    }
  }
}

void executa_alarme(String topico, float valor){
  //oled(true, "ALARME" + topico + valor);
  for (int i = 0; i < 20; i++){
   // oled(false, "ALARME" + alarmes[i].topico_alarme + alarmes[i].vlr_minimo);
    if (alarmes[i].topico_alarme.equals(topico)){
      if (alarmes[i].vlr_minimo <= valor && alarmes[i].vlr_maximo >= valor){
        trata_acao(alarmes[i].topico_acao, alarmes[i].valor);
        //oled(false, "executou: " + alarmes[i].topico_acao + alarmes[i].valor);
        delay(5000);
      }else if (alarmes[i].valor.equals("L")){
        trata_acao(alarmes[i].topico_acao, "D");
      }
    }
  }
  //delay(1000);
}

void envia_lora (String ptopico, String pmensagem){
  oled(false, "Enviando Lora");
  String dispositivo = DEVICE + SEPARADOR;
  if (ptopico == TOP_ALARME || ptopico == TOP_ACAO || ptopico == TOP_CONFIGU){
    dispositivo = "";
  }
  
  LoRa.beginPacket();
  LoRa.print(SETDATA + SEPARADOR + dispositivo + ptopico + SEPARADOR_TOP + pmensagem);
  LoRa.endPacket();
  oled(false, SETDATA + SEPARADOR + dispositivo + ptopico + SEPARADOR_TOP + pmensagem);
  oled(false, "Mensagem enviada");
  delay(500);
}

void recebe_lora (){
  int     tam_pacote = LoRa.parsePacket();
  String  mensagem = "";
  int     contador = 0;

  oled(true, "");
  if (tam_pacote > SETDATA.length()){
    while(LoRa.available()){
      mensagem += (char) LoRa.read();
    }

    int index = mensagem.indexOf(SETDATA);
    if (index >= 0){
      String dado_recebido = mensagem.substring(SETDATA.length());
      oled(true, "Dados Lora");
      oled(false, mensagem);
      delay(1000);

      String topico = dado_recebido.substring(0, dado_recebido.indexOf(SEPARADOR_TOP));

      if (topico.equals(TOP_ACAO)){
        String mensagem_recebida = dado_recebido.substring(dado_recebido.indexOf(SEPARADOR_TOP)+1);

        index = mensagem_recebida.indexOf(SEPARADOR);
        String status = mensagem_recebida.substring(0,index);
        
        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String id_dispositivo = mensagem_recebida.substring(0, index);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String id_acao = mensagem_recebida.substring(0, index);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String topico_acao = mensagem_recebida.substring(0, index);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String valor = mensagem_recebida.substring(0, index);

        oled(false, topico_acao + valor);
        
        if (id_dispositivo == DEVICE){
        oled(false, topico_acao + valor);
          trata_acao(topico_acao, valor);
          delay(1000);

          String mensagem_retorno = STATUS_OK + SEPARADOR + id_acao;
          envia_lora (topico, mensagem_retorno);
        }
      }else if (topico.equals(TOP_ALARME)){
        String mensagem_recebida = dado_recebido.substring(dado_recebido.indexOf(SEPARADOR_TOP)+1);

        index = mensagem_recebida.indexOf(SEPARADOR);
        String status = mensagem_recebida.substring(0,index);
        //oled(true, status);
        //delay(1000);
        
        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String id_dispositivo = mensagem_recebida.substring(0, index);
        //oled(true, id_dispositivo);
        //delay(1000);
        
        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String id_alarme = mensagem_recebida.substring(0, index);
        //oled(true, id_alarme);
        //delay(1000);
        
        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String topico_alarme = mensagem_recebida.substring(0, index);
        //oled(true, topico_alarme);
        //delay(1000);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String vlr_minimo = mensagem_recebida.substring(0, index);
        //oled(true, vlr_minimo);
        //delay(1000);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String vlr_maximo = mensagem_recebida.substring(0, index);
        //oled(true, vlr_maximo);
        //delay(1000);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String fl_ativo = mensagem_recebida.substring(0, index);
        //oled(true, fl_ativo);
        //delay(1000);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String topico_acao = mensagem_recebida.substring(0, index);
        //oled(true, topico_acao);
        //delay(1000);

        mensagem_recebida = mensagem_recebida.substring(index+1);
        index = mensagem_recebida.indexOf(SEPARADOR);
        String valor = mensagem_recebida.substring(0, index);
        //oled(true, valor);
        //delay(1000);
        
        if (id_dispositivo == DEVICE){
          seta_alarme (topico_alarme, id_alarme, string_para_float(vlr_minimo),  string_para_float(vlr_maximo), topico_acao, valor, fl_ativo);

          String mensagem_retorno = STATUS_OK + SEPARADOR + id_alarme;
          envia_lora (topico, mensagem_retorno);
        }
      }
      delay(1000);
    }
  }
}

void atualiza_temperatura_max_e_minima(float temp_lida)
{
  if (temp_lida > temperatura_max)
    temperatura_max = temp_lida;
  if (temp_lida < temperatura_min)
    temperatura_min = temp_lida;  
}

//* Função: escreve no display OLED a temperatura e umidade lidas, assim como as temperaturas máxima e mínima * Parâmetros: - Temperatura lida *             - Umidade relativa do ar lida * Retorno: nenhum*/
void escreve_temperatura_umidade_display(float temp_lida, float umid_lida)
{
  char   v_numero[20] = {0};
  String str_temp         = "";
  String str_umid         = "";
  String str_temp_max_min = "";
  /* formata para o display as strings de temperatura e umidade */
  str_temp = "Temperatura: " + float_para_string(temp_lida) + " C";
  str_umid = "Umidade: " + float_para_string(umid_lida) + "%";
  str_temp_max_min = "Min/Max: " + float_para_string(temperatura_min) + "C / "
                   + float_para_string(temperatura_max) + "C";

  oled(true,  str_temp);
  oled(false, str_umid);
  oled(false, str_temp_max_min);
  envia_lora (TOP_TEMPERATURA, float_para_string(temp_lida));
  envia_lora (TOP_UMIDADE    , float_para_string(umid_lida));
  
  executa_alarme(TOP_TEMPERATURA, temp_lida);
  executa_alarme(TOP_UMIDADE    , umid_lida);
}

void setup() {
  dht.begin();

  pinMode(RELEPIN, OUTPUT);
  digitalWrite(RELEPIN, LOW);

  Heltec.begin( true  //display
              , true   //lora
              , true   //serial enable
              , true   //paboost
              , BAND); //frequencia
  Heltec.display->setContrast(1);
  inicialilza_Lora();
  /* inicializa temperaturas máxima e mínima com a leitura inicial do sensor */
  temperatura_max = dht.readTemperature();
  temperatura_min = temperatura_max;

}


/* * Programa principal */
void loop() {
  float temperatura_lida;
  float umidade_lida;
  delay(500);
    /* Faz a leitura de temperatura e umidade do sensor */
    temperatura_lida = dht.readTemperature();
    umidade_lida = dht.readHumidity();
    /* se houve falha na leitura do sensor, escreve mensagem de erro na serial */
    if ( isnan(temperatura_lida) || isnan(umidade_lida) ) {
      oled(true, "Erro ao ler sensor!");
    }
    else
    {
      /*Se a leitura foi bem sucedida, ocorre o seguinte:
        - Os valores mínimos e máximos são verificados e comparados à medição atual de temperatura
        - se a temperatura atual for menor que a mínima ou maior que a máxima até então registrada,os limites máximo ou mínimo são atualizados.
        - As medições (temperatura, umidade, máxima temperatura e mínima temperatura) são escritas no display OLED     */
      atualiza_temperatura_max_e_minima(temperatura_lida);
      escreve_temperatura_umidade_display(temperatura_lida, umidade_lida);
    } 
    
  ultimo_envio = millis(); 
  while (millis() < ultimo_envio + INTERVALO){
    recebe_lora();
  }
  //esp_sleep_enable_timer_wakeup (fator_microseg * 10);
  //esp_deep_sleep_start();
}
