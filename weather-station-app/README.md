# Running
Create a docker network
```bash
docker network create \
--driver=bridge \
--subnet=172.29.0.0/16 \
--ip-range=172.29.1.0/24 \
--gateway=172.29.1.1 \
weather-net

```
Start up an MySQL database and connect it to the network.
```bash
docker run -idt \
--network=weather-net \
--ip=172.29.1.2 \
--hostname=DB \
-p 3307:3306 \              
-v /home/user/mysql/weather:/var/lib/mysql \    
-e MYSQL_ROOT_PASSWORD=rootpassword \
-e MYSQL_USER=wuser \
-e MYSQL_PASSWORD=wpasswd \
-e MYSQL_DATABASE=weather \  
--name mysql-weather \
mysql:latest
```                 
Create a keystore from your certs:
```bash
openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out ssl.p12 -name  example.com
keytool -importkeystore -destkeystore keystore.jks -srckeystore ssl.p12 -srcstoretype PKCS12
```

Start a hardened instance of tomcat and attach it that network: 
```bash
docker run -idt \   
--network=weather-net \
--ip=172.29.1.3 \
--hostname=WEBAPP \ 
--add-host=DB:172.29.1.2 \
-p 8443:8443 \
-v /usr/share/tomcat/weather-station-app-1.0-SNAPSHOT.war:/usr/local/tomcat/webapps/ROOT.war \
-v /usr/share/tomcat/context.xml:/usr/local/tomcat/conf/context.xml \
-v /usr/share/tomcat/server.xml:/usr/local/tomcat/conf/server.xml \
-v /usr/share/tomcat/catalina.policy:/usr/local/tomcat/conf/catalina.policy \
-v /home/user/tomcat/logs/:/usr/local/tomcat/logs \
-v /home/user/certs/keystore.jks:/usr/local/tomcat/conf/keystore.jks \
-e TOMCAT_USER_ID=`id -u` \
-e TOMCAT_GROUP_ID=`getent group $USER | cut -d':' -f3` \
-e "JAVA_OPTS=\
-Ddb.url=jdbc:mysql://DB:3306/weather \
-Ddb.user=wuser \
-Ddb.pwd=wpasswd \
-Drest.user=user \
-Drest.password=password \
-Drest.admin.user=admin \
-Drest.admin.password=apassword" \
--name weather-app \
unidata/tomcat-docker:8.5
```