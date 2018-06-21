# Samples

Several examples and samples.

The WSGW was developed to be a simple gateway with WebSocket capability,
easy to use, low memory footprint, and more important ease the development of demos inside the department.
As such, the samples here serve as a repository for simple IoT demos.

## Arduino

We have some examples using two IoT boards NodeMCU ESP-12E (ESP8622) and NodeMCU 32s (ESP32).
The list of peripherals/sensors used is the following:
* Simple LEDs
* DHT22 (Temperature and Humidity)
* BME280 (Temperature, Humidity and Atmospheric Pressure)
* BH1750 (Light)
* SSD1306 (OLED 128x64 disply)

For more information about these samples, click [here](arduino/)

## Installation

The whole project was done in Maven, as such to package the application just run:
```
mvn package
```

To execute the application just run:
```
java -Djava.util.logging.config.file=logging.properties -jar target/wsgw-1.0.jar wsgw.properties 
```
or (without the suggested logger configuration)
```
java -jar target/wsgw-1.0.jar wsgw.properties 
```

## Samples and Examples

We provide examples of two webpages that subscribe data from sensors (DTH22 and BME280),
and the ESP8266 (arduino sketch) code to publish the data.
The code can be found in samples folder: arduino and webpages respectively.

## Authors

* **[Mário Antunes](https://github.com/mariolpantunes)**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
