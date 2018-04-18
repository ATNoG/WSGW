#include <Wire.h>
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>

#define BME_SCK 13
#define BME_MISO 12
#define BME_MOSI 11
#define BME_CS 10

#define SEALEVELPRESSURE_HPA (1013.25)

//const char* ssid     = "IoT-Privacy";
//const char* password = "deadpool";

const char* ssid     = "TP-LINK_1262";
const char* password = "24398022";

Adafruit_BME280 bme; // I2C
//Adafruit_BME280 bme(BME_CS); // hardware SPI
//Adafruit_BME280 bme(BME_CS, BME_MOSI, BME_MISO, BME_SCK); // software SPI

unsigned long delayTime;

// UDP Client
WiFiUDP Udp;
unsigned int localUdpPort = 4210;
const char* ip = "192.168.0.102";
int port = 8888;

String pret = "{\"type\":\"pub\",\"topic\":\"temperature\",\"value\":";
String preh = "{\"type\":\"pub\",\"topic\":\"humidity\",\"value\":";
String prep = "{\"type\":\"pub\",\"topic\":\"pressure\",\"value\":";

void setup() {
  Serial.begin(115200);
  delay(10);

  bool status;
  
  // default settings
  status = bme.begin();
  if (!status) {
      Serial.println("Could not find a valid BME280 sensor, check wiring!");
      while (1);
  }
    
  Serial.println("-- Default Test --");
  delayTime = 1000;

  Serial.println();

  WiFi.begin(ssid, password);
  Serial.print("\n\r \n\rWorking to connect");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("NodeMCU(ESP8266)");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  Udp.begin(localUdpPort); 
}

void sendValues(float t, float h, float p, const char* ip, int port) {
  String temp = pret+t+"}";
  String humd = preh+h+"}";
  String pres = prep+p+"}";

  Serial.println(temp);
  Serial.println(humd);
  Serial.println(pres);
  Serial.println();
  
  Udp.beginPacket(ip, port);
  Udp.write(temp.c_str());
  Udp.endPacket();

  Udp.beginPacket(ip, port);
  Udp.write(humd.c_str());
  Udp.endPacket();

  Udp.beginPacket(ip, port);
  Udp.write(pres.c_str());
  Udp.endPacket();
}

void loop() { 
  float t = bme.readTemperature();
  float h = bme.readHumidity();
  float p = bme.readPressure() / 100.0F;
  
  sendValues(t,h,p, ip, port);
  delay(delayTime);
}
