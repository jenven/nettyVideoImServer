### 1. 在ubuntu18.04下安装kms服务
##### 1. Make sure that GnuPG is installed.
 ```shell script
     sudo apt-get update && sudo apt-get install --no-install-recommends --yes \
         gnupg
 ```
##### 2. Add the Kurento repository to your system configuration.Run these commands:
 ```shell script
# Import the Kurento repository signing key
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 5AFA7A83

# Get Ubuntu version definitions
source /etc/upstream-release/lsb-release 2>/dev/null || source /etc/lsb-release

# Add the repository to Apt
sudo tee "/etc/apt/sources.list.d/kurento.list" >/dev/null <<EOF
# Kurento Media Server - Release packages
deb [arch=amd64] http://ubuntu.openvidu.io/6.16.0 $DISTRIB_CODENAME kms6
EOF
```
##### 3. Install KMS:
  > Note!
  >
  > This step applies only for a first time installation. If you already have installed Kurento and want to upgrade it, follow instead the steps described here: Local Upgrade. 
  ```shell script
   sudo apt-get update && sudo apt-get install --no-install-recommends --yes \
       kurento-media-server
  ```
  > This will install the release version of Kurento Media Server.
 
##### 4. 更新kms
   >
   > To upgrade a local installation of Kurento Media Server, you have to write the new version number
   > into the file /etc/apt/sources.list.d/kurento.list, which was created during Local Installation. After
   > editing that file, you can choose between 2 options to actually apply the upgrade:
   >
   > A. Upgrade all system packages.
   >
   > > This is the standard procedure expected by Debian & Ubuntu maintainer methodology. 
   > > Upgrading all system packages is a way to ensure that everything is set to the latest version, and
   > > all bug fixes & security updates are applied too, so this is the most recommended method:
   ```shell script
      sudo apt-get update && sudo apt-get dist-upgrade
   ```
   > > However, don’t do this inside a Docker container. Running apt-get upgrade or apt-get dist-
   > > upgrade is frowned upon by the Docker best practices; instead, you should just move to a newer
   > > version of the Kurento Docker images.
   >
   > B. Uninstall the old Kurento version, before installing the new one. 
   > 
   > > Note however that apt-get is not good enough to remove all of Kurento packages. We
   > > recommend that you use aptitude for this, which works much better than apt-get:
   ```shell script
      sudo aptitude remove '?installed?version(kurento)'
      
      sudo apt-get update && sudo apt-get install --no-install-recommends --yes \
          kurento-media-server
   ```
   > 

### 2. 启动>kms服务器

 >
 > The server includes service files which integrate with the Ubuntu init system, so you can use the following commands to start and stop it:
 >
 ```shell script
  sudo service kurento-media-server start

  sudo service kurento-media-server stop
 ```
* Check your installation
 > To verify that the Kurento process is up and running, use this command and look for the kurento-media-server process:
 >
 ```shell script
   ps -fC kurento-media-server
   UID        PID  PPID  C STIME TTY          TIME CMD
   kurento   7688     1  0 13:36 ?        00:00:00 /usr/bin/kurento-media-server
 ```
 > Unless configured otherwise, KMS will listen on the port TCP 8888, to receive RPC Requests and
 > send RPC Responses by means of the Kurento Protocol. Use this command to verify that this port is
 > open and listening for incoming packets:
 ```shell script
  sudo netstat -tupln | grep -e kurento -e 8888
  tcp6  0  0  :::8888  :::*  LISTEN  7688/kurento-media-
 ```
 > You can change these parameters in the file /etc/kurento/kurento.conf.json.
 > 


### 3. STUN/TURN server install

These links contain the information needed to finish configuring your Kurento Media Server with a STUN/TURN server:

* [How to install Coturn](https://doc-kurento.readthedocs.io/en/stable/user/faq.html#faq-coturn-install)
* [How to test my STUN/TURN server](https://doc-kurento.readthedocs.io/en/stable/user/faq.html#faq-stun-test)
* [How to configure STUN/TURN in Kurento](https://doc-kurento.readthedocs.io/en/stable/user/faq.html#faq-stun-configure)


### 4. 运行工程
sudo mvn -U clean spring-boot:run     -Dspring-boot.run.jvmArguments="-Dkms.url=ws://127.0.0.1:8888/kurento"

### 5. 在ubuntu18.0.4 上运行
 
##### 5.1 服务器打包并上传到指定位置，运行：
```shell script
 java -jar  nettyChatServer.jar
```
##### 5.2 停掉php和nginx：
```shell script
 sudo service nginx stop

 sudo service php7.2-fpm stop 

```
##### 5.3 运行页面服务器：
```shell script
cd /home/vagrant/code/webrtc/httpsServer

node server.js  

或
 
forever start server.js

```

##### 5.4 Chrome浏览器打开： https://192.168.10.10