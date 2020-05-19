//includes de bibliotecas
#include <DHT.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include "heltec.h"

//Configuração GPIO
#define MASTER
#define DHTPIN         13 //umidade_temperatura
#define fator_microseg 1000000
#define potencia_lora  20 
#define BAND           915E6

#define INTERVALO 2000
long ultimo_envio = 0;

//basicas
int oled_x = 0;
int oled_y = 0;

//Wi-FI
const char * wifi_rede = "Vip Telecom - Bianor";
const char * wifi_senha = "35230045";

//LoRa
const String GETDATA = "getdata";
const String SETDATA = "setdata";
#define SCK 5
#define MISO 19
#define MOSI 27
#define SS 18
#define RST 14
#define DI00 26
#define DEVICELENGTH 5

//MQTT
const String  SEPARADOR     = ";";
const String  SEPARADOR_TOP = "!";
const char *  mqtt_servidor = "192.168.2.116";
const int     mqtt_porta    = 1883;
const int     mqtt_qos      = 2;
const char *  mqtt_usuario;
const char *  mqtt_senha;
WiFiClient    cliente_esp;
PubSubClient  mqtt_cliente(cliente_esp);
const String  mqtt_topico_config_esp = "configu";
const String  mqtt_topico_alarme     = "alarme";
const String  mqtt_topico_acao       = "acao";
const String  mqtt_sufixo            = "";

//Controle
String dispositivos[20][5];

void oled(boolean plimpa, String ptexto);
String float_para_string(float f1);
String int_para_string(int f1);
//void controla_dispositivos(String device);
void conecta_wifi();
void mqtt_inicializa(void);
void mqtt_recebe(char* topico, byte* payload, unsigned int length);
void mqtt_publica (String ptopico, String pmensagem);
void envia_lora (String pmensagem);
void recebe_lora ();

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

  Heltec.display->drawString(oled_x, oled_y, String(ptexto));
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

String int_para_string(int f1) {
  char numero[25] = {0};
  sprintf(numero, "%d", f1);
  String retorno = "";
  for (int i = 0; i < 25; i++){
    retorno += numero[i];
  }
  return retorno;
}

/*void controla_dispositivos(String device){
  boolean existe = false;
  int vazio = 0;

  String disp[5];
  for (int i = 0; i<5; i++){
    disp = disp+device[i];
  }
  
  for (int i = 0; i < 20; i++){
    if (dispositivos[i] == disp){
      existe = true;
    }
    if (dispositivos[i] == "" && vazio == 0){
      int vazio = i;
    }
  }

  if (existe == false){
    mqtt_inscreve(device + SEPARADOR + "alarme" );
    mqtt_inscreve(device + SEPARADOR + "configu");
    dispositivos[vazio] = device;
  }
}*/

void conecta_wifi()
{
  WiFi.begin(wifi_rede, wifi_senha);
  int count = 1;
  String str = "";

  while (WiFi.status() != WL_CONNECTED && count < 15) {
    str = "Conectando ao Wifi..(" + int_para_string(count) + ")";
    oled(true, str);
    delay (1000);
    count++;
  }

  if (WiFi.status() == WL_CONNECTED) {
    oled(false, "Conectado a rede Wifi!");
  }
  else {
    oled(false, "Nao e possivel conectar!");
  }
  delay(2000);
}

void mqtt_inicializa()
{
    mqtt_cliente.setServer(mqtt_servidor, mqtt_porta);
    /* Informa que todo dado que chegar do broker pelo tópico definido em MQTT_SUB_TOPIC
       Irá fazer a função mqtt_callback ser chamada (e essa, por sua vez, irá escrever
       os dados recebidos no Serial Monitor */
    mqtt_cliente.setCallback(mqtt_recebe);
    mqtt_inscreve(mqtt_topico_alarme);
    mqtt_inscreve(mqtt_topico_config_esp);
    mqtt_inscreve(mqtt_topico_acao);
}

void mqtt_recebe(char* ptopico, byte* payload, unsigned int length) 
{
    String topico;
    String mensagem;
    char c;

    for(int i = 0; i < strlen(ptopico); i++) 
    {
       c = (char)ptopico[i];
       topico += c;
    }
    /* Obtem a string do payload (dados) recebido */
    for(int i = 0; i < length; i++) 
    {
       c = (char)payload[i];
       mensagem += c;
    } 
    topico = topico.substring(mqtt_sufixo.length());
    oled(false, topico);     
    oled(false, mensagem);        
    delay(1000);
    
    if (topico == mqtt_topico_config_esp) {
      String device = mensagem.substring(0, DEVICELENGTH);
      String conf   = mensagem.substring(DEVICELENGTH);
      envia_lora(device + topico + conf);
    }else if (topico == mqtt_topico_alarme || topico == mqtt_topico_acao){
      envia_lora(topico+SEPARADOR_TOP+mensagem);
    }
}

void mqtt_inscreve (String ptopico){
  ptopico = mqtt_sufixo+ptopico;
  if (WiFi.status() != WL_CONNECTED) {
    conecta_wifi();
  }
  while (!mqtt_cliente.connected()) {
    if (mqtt_cliente.connect("tcfevlrESP32Client")) {
      oled(true, "Conectado ao Broker!");
    }
    else{
      oled(true, "Falha de conexão ao broker!");
    }
  }
  mqtt_cliente.subscribe(ptopico.c_str());
  oled(false, "Inscrito no tópico");
  oled(false, ptopico);
  delay(2000);
}

void mqtt_publica (String ptopico, String pmensagem) {
  ptopico = mqtt_sufixo+ptopico;
  if (WiFi.status() != WL_CONNECTED) {
    conecta_wifi();
  }
  while (!mqtt_cliente.connected()) {
    if (mqtt_cliente.connect("tcfevlrESP32Client", mqtt_usuario, mqtt_senha)) {
      oled(true, "Conectado ao Broker!");
    }
    else{
      oled(true, "Falha de conexão ao broker!");
    }
    
  }
  mqtt_cliente.publish(ptopico.c_str(), pmensagem.c_str(), mqtt_qos);
}

void envia_lora (String pmensagem){
  oled(false, "Enviando Lora");
  LoRa.beginPacket();
  LoRa.print(SETDATA + pmensagem);
  LoRa.endPacket();
  oled(false, "Mensagem enviada");
}


void recebe_lora (){
  int     tam_pacote = LoRa.parsePacket();
  String  mensagem = "";
  int     contador = 0;

  oled(true, "Dados Lora");
  if (tam_pacote > SETDATA.length()){
    while(LoRa.available()){
      mensagem += (char) LoRa.read();
    }
    

    int index = mensagem.indexOf(SETDATA);
    if (index >= 0){
      String dado_recebido = mensagem.substring(SETDATA.length());

      int index_msg = mensagem.indexOf(SEPARADOR_TOP);
      
      String device        = mensagem.substring(SETDATA.length()+1, mensagem.indexOf(SEPARADOR));
      String topico        = mensagem.substring(SETDATA.length()+1, index_msg);
      String valor         = mensagem.substring(index_msg+1);
      oled(false, topico);
      oled(false, valor);

      mqtt_publica (topico, valor);

      //controla_dispositivos(device);

    }
  }
}

void setup() {
  //dht.begin();

  Heltec.begin(true, false, true);
  Heltec.display->setContrast(1);
  inicialilza_Lora();
  /* inicializa temperaturas máxima e mínima com a leitura inicial do sensor */
  conecta_wifi();
  mqtt_inicializa();
  //temperatura_max = dht.readTemperature();
  //temperatura_min = temperatura_max;

}


/* * Programa principal */
void loop() {
  recebe_lora ();
  mqtt_cliente.loop();
  //esp_sleep_enable_timer_wakeup (fator_microseg * 20);
  //esp_deep_sleep_start();
}
