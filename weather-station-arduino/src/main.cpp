#include <Arduino.h>
#include <WiFiS3.h>
#include <WiFiSSLClient.h>

#include <Adafruit_BME280.h>
#include "../include/cactus_io_DS18B20.h"
#include "../include/ca_cert.h"
#include "../include/wifi_credentials.h"
#include "../include/basic_auth.h"

Adafruit_BME280 bme;
DS18B20 ds(9);

IPAddress ipAddr(192, 168, 86, 19);
IPAddress dns1 (192, 168, 86, 14);
IPAddress router(192, 168, 86,1);
IPAddress subnet(255, 255, 255, 0);

const char *server = "carpenter.home.remgant.net";

int serverPort = 8091;
// WiFiClient client;
WiFiSSLClient client;
bool connectToWifi();
void setup() {
    Serial.begin(9600);

    pinMode(2, OUTPUT);
    // int counter = 0;

    if (WiFi.status() == WL_NO_MODULE) {
        Serial.println("WiFi module failed");
        // don't continue:
        while (true);
    }

    while (!connectToWifi())
    {
        Serial.print("could not connect to: ");
        Serial.println(ssid);
        delay(10000);
    }

    int status = bme.begin(0x76);
    if (!status) {
    Serial.println("Could not find a valid BME280 sensor, check wiring, address, sensor ID!");
    Serial.print("SensorID was: 0x"); Serial.println(bme.sensorID(),16);
    Serial.print("        ID of 0xFF probably means a bad address, a BMP 180 or BMP 085\n");
    Serial.print("   ID of 0x56-0x58 represents a BMP 280,\n");
    Serial.print("        ID of 0x60 represents a BME 280.\n");
    Serial.print("        ID of 0x61 represents a BME 680.\n");
    while (1) delay(10);
    }
}

bool connectToWifi()
{
    digitalWrite(2, LOW);
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print("Connecting to ");
        Serial.println(ssid);
        WiFi.config(ipAddr, dns1, router, subnet);
        WiFi.begin(ssid, passwd);
        delay(10000);
    }
    if (WiFi.status() != WL_CONNECTED)
    {
        Serial.print("Failed to connect: ");
        Serial.println(String(WiFi.status()));
        return false;
    }

    Serial.println("Connected.");
    // print the SSID of the network you're attached to:
    Serial.print("SSID: ");
    Serial.println(WiFi.SSID());

    // print your board's IP address:
    IPAddress ip = WiFi.localIP();
    Serial.print("IP Address: ");
    Serial.println(ip);


    // print the received signal strength:
    long rssi = WiFi.RSSI();
    Serial.print("signal strength (RSSI):");
    Serial.print(rssi);
    Serial.println(" dBm");
    digitalWrite(2, HIGH);
    return true;
}

void loop() {
    while (WiFi.status() != WL_CONNECTED)
    {
        connectToWifi();
    }

    float pressure = bme.readPressure();
    float temperature = bme.readTemperature();
    float humidity = bme.readHumidity();

    ds.readSensor();
    temperature = ds.getTemperature_C();

    String s = "{\"pressure\":";
    s += String(pressure, 1);
    s += ",\"temperature\":";
    s += String(temperature, 1);
    s += ",\"humidity\":";
    s += String(humidity, 1);
    s += "}";

    Serial.println(s);
    int contentLength = s.length();
    client.stop();
    client.setCACert(ca_cert);

    if (client.connect(server, 8091))
    {
        Serial.println("Connected to server");
        client.println("POST /api/weather-update HTTP/1.0");
        client.println("Host: carpenter.home.remgant.net");
        client.print("Authorization: Basic ");
        client.println(basicAuth);
        client.println("Content-Type: application/json");
        client.print("Content-Length: ");
        client.println(String(contentLength));
        client.println("Connection: close");
        client.println();
        client.print(s);
        client.flush();
    } else
    {
        Serial.println("can\'t connect to server");
    }
    delay(6000);
    while (client.available())
    {
        char c = client.read();
        Serial.print(c);

        /*
        HTTP/1.1 204
        Date: Thu, 24 Jul 2025 12:08:18 GMT
        Connection: close
         */
    }
    delay(30000);
}