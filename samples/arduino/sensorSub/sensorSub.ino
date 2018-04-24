#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <ArduinoJson.h>

const char* ssid     = "TP-LINK_1262";
const char* password = "24398022";

unsigned long delayTime = 10;
unsigned int ledPin = 13;
unsigned int humidity = 85;
unsigned int ledPinLux = 12;

// UDP Client
WiFiUDP udp;
unsigned int localUdpPort = 4210;
const char* ip = "192.168.0.102";
int port = 8888;
char incomingPacket[255];

void setup() {
  Serial.begin(115200);
  delay(10);

  // LED Starts OFF
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);
  
  // Connect to WiFi
  Serial.println();
  WiFi.begin(ssid, password);
  Serial.print("\n\r \n\rWorking to connect");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  // Print Network Information
  Serial.println("");
  Serial.println("NodeMCU(ESP8266)");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  // Start UDP Socket
  udp.begin(localUdpPort);

  // JSON Encoder
  const size_t bufferSize = JSON_OBJECT_SIZE(2);
  DynamicJsonBuffer jsonBuffer(bufferSize);

  // Subscribe Humidity
  JsonObject& root = jsonBuffer.createObject();
  root["type"] = "sub";
  root["topic"] = "humidity";

  udp.beginPacket(ip, port);
  root.printTo(udp);
  udp.println();
  udp.endPacket();

  // Subscribe Light
  root["topic"] = "light";

  udp.beginPacket(ip, port);
  root.printTo(udp);
  udp.println();
  udp.endPacket();
}

void loop() {
  // Receive UDP Package
  int packetSize = udp.parsePacket();
  
  if (packetSize)
  {
    // receive incoming UDP packets
    /*Serial.printf("Received %d bytes from %s, port %d\n", packetSize, udp.remoteIP().toString().c_str(), udp.remotePort());
    int len = udp.read(incomingPacket, 255);
    if (len > 0)
    {
      incomingPacket[len] = 0;
    }
    Serial.printf("UDP packet contents: %s\n", incomingPacket);*/
 
    // JSON Parser
    const size_t bufferSize = JSON_OBJECT_SIZE(3) + 40;
    DynamicJsonBuffer jsonBuffer(bufferSize);
    JsonObject& root = jsonBuffer.parseObject(udp);
    const char* type = root["type"]; 
    const char* topic = root["topic"];
    int value = root["value"];

    if(strcmp(topic,"humidity") == 0) {
      if(value > humidity) {
        digitalWrite(ledPin, HIGH);
      } else {
        digitalWrite(ledPin, LOW);
      }
    } else {
      int i = 0.098*value;
      analogWrite(ledPinLux, i);
    }
  }
  delay(delayTime);
}
