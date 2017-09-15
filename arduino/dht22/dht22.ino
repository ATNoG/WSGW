#include <Wire.h>
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

#include "DHT.h"

#define DHTTYPE DHT22
#define DHTPIN  5

const char* ssid     = "TP-LINK_1262";
const char* password = "24398022";
//const char* ssid     = "TNDS_2.4GHZ";
//const char* password = "leavethekidsalone";


// Initialize DHT sensor
DHT dht(DHTPIN, DHTTYPE);

// UDP Client
WiFiUDP Udp;
unsigned int localUdpPort = 4210;
const char* wsgw = "192.168.0.101";

String pret = "{\"type\":\"pub\",\"topic\":\"temperature\",\"value\":";
String preh = "{\"type\":\"pub\",\"topic\":\"humidity\",\"value\":";
String prep = "{\"type\":\"pub\",\"topic\":\"pressure\",\"value\":";

// PIR movement sensor
int state, reading;

// count to cycle between sensors
int count = 0;

void setup() {
  Serial.begin(115200);
  delay(10);
  dht.begin();
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

  pinMode(D3, INPUT);
  state = LOW;         // We'll assume no motion is detected in the begining
  reading = LOW;
  
  //Serial.print("\nMotion detection starting at ");
  //Serial.print(millis()/(1000*60));
  //Serial.print("m"); Serial.print(millis()/(1000));
  //Serial.println("s."); Serial.println();
}


void loop() {
  delay(50);

  if(count == 0) {
    // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();
  
  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
  }
  else {
    //Serial.print("Humidity: ");
    //Serial.print(h);
    //Serial.print(" %\t Temperature: ");
    //Serial.print(t);
    //Serial.println(" *C ");

    String temp = pret+t+"}";
    String humd = preh+h+"}";
    Serial.println(temp);
    Serial.println(humd);


    Udp.beginPacket(wsgw, 8888);
    Udp.write(temp.c_str());
    Udp.endPacket();

    Udp.beginPacket(wsgw, 8888);
    Udp.write(humd.c_str());
    Udp.endPacket();
  }
  }

  reading = digitalRead(D3);
  if (reading != state){  // State has changed, could be movement detection...
    state = reading;
    if (state == HIGH){   // It is movement indeed.
      //Serial.print("Motion detected! Timestamp: ");
      //Serial.print(millis()/(1000*60));
      //Serial.print("m"); Serial.print((millis()%60000)/1000);
      //Serial.println("s");

      String mov = prem+"1}";
      Serial.println(mov);

      Udp.beginPacket(wsgw, 8888);
      Udp.write(mov.c_str());
      Udp.endPacket();
      
    } else {              // Previous motion stopped
      //Serial.print("Motion stopped. Timestamp: ");
      //Serial.print(millis()/(1000*60));
      //Serial.print("m"); Serial.print((millis()%60000)/1000);
      //Serial.println("s");

      String mov = prem+"0}";
      Serial.println(mov);

      Udp.beginPacket(wsgw, 8888);
      Udp.write(mov.c_str());
      Udp.endPacket();
    }
  }
  
  count = (count + 1) % 20;
}
