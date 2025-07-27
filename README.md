# Weather Station

This is the web and backend code for a weather station something like this [Arduino Weather Station Project](http://cactus.io/projects/weather/arduino-weather-station)
[\(Wayback Machine\)](https://web.archive.org/web/20230328075917/http://www.cactus.io/projects/weather/arduino-weather-station)

## Configuring Certs for WeatherUploader

### Certifcates and Security

We need these certs set up for the application:
* An SSL cert and key singed by a legit CA for the webserver and app server (fullchain.pem, privkey.pem)
* The SSL cert as a PKCS12 key for the app server and place in a keystore (ssl.p12, keystore.jks)
* A self signed CA cert and key (rootCA.crt, rootCA.key)
* A trustore with the CA cert and key (trustore.jks)
* A cert for rabbitmq signed by the self-signed CA (rabbitmq.key)
* A cert for the updater app (server.p12)
* Certs signed by the self-signed CA for the app and updated to connect to RabbitMQ (weather-station.p12, weather-updater.p12)
* Certs signed by the self-signed CA for any clients of the updater (alice.\[crt|p12\], charlie.\[crt|p12\])
                       
#### Certificate for public HTTPS 

Get a signed certificate from Lets Encrypt for the servers hostname (or a wild card). Copy over fullchain.pem and privkey.pem. The pem files
can be directly used by Apache httpd.

We need to create a PKCS12 key from the pem files and make a keystore for the app server: 

```bash
openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out ssl.p12 -name  "*.example.com"
keytool -importkeystore -destkeystore keystore.jks -srckeystore ssl.p12 -srcstoretype PKCS12
```
                       
#### Certificate Authority for all other certs

Create the certificate authority certificate that we can use to sign all other certifificates.

```bash
openssl req -x509 -sha256 -newkey rsa:4096 -keyout rootCA.key -out rootCA.crt
```

### Create Server Certificate
We need self-signed certs for RabbitMQ and the updater app. 

First, create the certificate signing request and the private key:
```bash
openssl req -new -newkey rsa:4096 -keyout rabbitmq.key -out rabbitmq.csr
```
Create a file called rabbitmq.ext for the RabbitMQ service with the contents:
```text
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
subjectAltName = @alt_names
[alt_names]
DNS.1 = localhost
DNS.2 = RABBITMQ
```
Sign the CSR with the CA's certificate:
```bash
openssl x509 -req -in rabbitmq.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial 
-out rabbitmq.crt -days 365 -sha256 -extfile rabbitmq.ext
```
Now do the same for the updater.

Create csr and key:

```bash
openssl req -new -newkey rsa:4096 -keyout weather-updater.key -out weather-updater.csr
```
Create a file called weather-updater.ext for with the contents:
```text
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
subjectAltName = @alt_names
[alt_names]
DNS.1 = localhost
DNS.2 = *.example.com
```

Sign the CSR with the CA's certificate:
```bash
openssl x509 -req -in rabbitmq.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial 
-out weather-updater.crt -days 365 -sha256 -extfile weather-updater.ext
```
Create a PKCS12 type certificate from existing PEM certificate:
```bash
openssl pkcs12 -export -out weather-updater.p12 -name "localhost" -inkey weather-updater.key -in weather-updater.csr
```
We need a trust store with CA's key. Create a trustore:
```bash
keytool -import -trustcacerts -noprompt -alias ca -ext san=dns:localhost,ip:127.0.01 -file rootCA.crt -keystore truststore.jks
````

Add to the servers application.properties:
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:server.p12
server.ssl.key-store-password=${KEYSTORE_PASSWORD}
server.ssl.keyStoreType=PKCS12
# Should match CN in certificate
server.ssl.keyAlias=localhost
# Trust store that holds SSL certificates.
server.ssl.trust-store=classpath:truststore.jks
# Password used to access the trust store.
server.ssl.trust-store-password=${TRUSTSTORE_PASSWORD}
# Type of the trust store.
server.ssl.trust-store-type=JKS
# Whether client authentication is wanted ("want") or needed ("need").
server.ssl.client-auth=need
```

### Create Client Certificate
This is the cert and key a client will use.

Create a new certificate signing request wiht CN=alice:
```bash
openssl req -new -newkey rsa:4096 -nodes -keyout alice.key -out alice.csr
```
Sign it with CA's certificate:
```bash
 openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in alice.csr -out alice.crt
 -days 365 -CAcreateserial
```

To test the security, you can create a self-signed certificat which should fail to connect:
```bash
openssl x509 -req -signkey bob.key  -in bob.csr -out bob.crt  -days 365
```
    
### Test Connection
Start the server. 

Access the server with:
```bash
 curl -k --cert ./alice.crt --key ./alice.key https://localhost:8083/user
 ```

### Using a Spring RestTemplate
To programatically access the server using Spring's RestTempalte, set a bean like this:
```java
    @Bean
    public RestTemplate restTemplate() throws Exception {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new ClassPathResource(certName).getInputStream(), keystorePassword.toCharArray());
        SSLContext sslContext = new SSLContextBuilder()
                .setProtocol("TLS")
                .loadKeyMaterial(clientStore, keystorePassword.toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(10000); // 10 seconds
        requestFactory.setReadTimeout(10000); // 10 seconds
        return new RestTemplate(requestFactory);
    }
```
## Configuring Certs for Weather Station API Server
This uses properly signed certs from a trusted cetificate authority (like Let's Encrypt).

```bash
openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out ssl.p12 -name  example.com
keytool -importkeystore -destkeystore keystore.jks -srckeystore ssl.p12 -srcstoretype PKCS12
```

An alternate way of doing this:
```bash
openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out ssl.p12 -name "*.home.remgant.net" -CAfile chain.pem  -caname root
```

## Issues Resolved
      
### Teamcity SSH not working

Whatever Teamcity does with SSH wasn't working with Fedora 33.  
It was rejecting the ssh-rsa key the Teamcity user was presenting.  It
worked fine with the command line ssh, but not whatever Teamcity was using.

By using this command:
```bash
sudo journalctl _COMM=sshd
```
There would be an error like this:

```
userauth_pubkey: key type ssh-rsa not in PubkeyAcceptedKeyTypes [preauth]
```
                            
The solutionis to edit /etc/crypto-policies/back-ends/opensshserv
er.config
and add *rsa-ssh* to the end of the *PubkeyAcceptedKeyTypes* line.

### No Passwd for sudo
In order for the script to run dnf, we need to run as super user but
we can't use a prompt for the password.  We need to edit the sudoers
file using visudo.  First, uncomment the line with *Cmnd_Alias SOFTWARE* and
add */usr/bin/dnf* to the end of it.  Then near the bottom of the file, 
add this line:
```
%wheel  ALL=    NOPASSWD: SOFTWARE
```
