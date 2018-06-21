# WebSocket Gateway (WSGW)

WebSocket (WS) Gateway.

Rather simple pub/sub gateway that bridges UDP and WebSockets (WS).
Producers share data through a UDP/WS connection, it only supports JSON data.
Clients request data through a ws/upd connection.

It has been used to showcase small IoT scenarios in school demonstrations.

## Prerequisites

Most of my code requires a library named [utils](https://github.com/mariolpantunes/utils).
This library is no exception, it also requires Jetty libraries (from WS support).
Currently, it requires Java 8.
In the future, the code will be converted to Java 10 and the Jetty requirement may/will be dropped.

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

We provide some samples and examples.
Several Arduino sketches, web pages and a systemd service can be found [here](samples).

## Authors

* **[Mário Antunes](https://github.com/mariolpantunes)**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
