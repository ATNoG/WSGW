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
* SSD1306 (OLED 128x64 display)

For more information about these samples, click [here](arduino/README.md).

## SystemD

A simple unit configuration file to start the WSGW in linux systems.

For more information about these samples, click [here](systemd/README.md).

## WebPages

Some webpages that connect to the WSGW through websockets and subscribe data from specific sensors.

For more information about these samples, click [here](webpages/README.md).

## Authors

* **[Mário Antunes](https://github.com/mariolpantunes)**

## License

This project is licensed under the MIT License - see the [LICENSE.md](../LICENSE.md) file for details
