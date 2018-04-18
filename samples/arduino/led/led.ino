#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <ArduinoJson.h>

//const char* ssid     = "IoT-Privacy";
//const char* password = "deadpool2";
const char* ssid     = "TP-LINK_1262";
const char* password = "24398022";

unsigned long delayTime = 10;
unsigned int ledPin = 13;
unsigned int humidity = 85;

// UDP Client
WiFiUDP Udp;
unsigned int localUdpPort = 4210;
const char* ip = "192.168.0.102";
//const char* ip = "192.168.1.249";
int port = 8888;
char incomingPacket[255];

void sendUDP(const char* msg, const char* ip, int port) {
  Udp.beginPacket(ip, port);
  Udp.write(msg);
  Udp.endPacket();
}

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
  Udp.begin(localUdpPort);

  // JSON Encoder
  const size_t bufferSize = JSON_OBJECT_SIZE(2);
  DynamicJsonBuffer jsonBuffer(bufferSize);
  
  JsonObject& root = jsonBuffer.createObject();
  root["type"] = "sub";
  root["topic"] = "humidity";

  char jsonMsg[100];
  root.printTo((char*)jsonMsg, root.measureLength() + 1);

  // Subscrive to temperature
  sendUDP(jsonMsg, ip, port);
}


void loop() {
  // Receive UDP Package
  int packetSize = Udp.parsePacket();
  if (packetSize)
  {
    // receive incoming UDP packets
    Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
    int len = Udp.read(incomingPacket, 255);
    if (len > 0)
    {
      incomingPacket[len] = 0;
    }
    Serial.printf("UDP packet contents: %s\n", incomingPacket);

    // send back a reply, to the IP address and port we got the packet from
    //Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    //Udp.write(replyPacket);
    //Udp.endPacket();
    
    // JSON Parser
    const size_t bufferSize = JSON_OBJECT_SIZE(3) + 40;
    DynamicJsonBuffer jsonBuffer(bufferSize);
    JsonObject& root = jsonBuffer.parseObject(incomingPacket);
    const char* type = root["type"]; // "pub"
    const char* topic = root["topic"]; // "temperature"
    int value = root["value"];

    if(value > humidity) {
      digitalWrite(ledPin, HIGH);
    } else {
      digitalWrite(ledPin, LOW);
    }
  }
  delay(delayTime);
}
