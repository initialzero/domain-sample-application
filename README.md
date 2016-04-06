# Sample application for demonstration Domain API
===========================================================

The application demonstrates functionality of doamin designer of JasperReports servers. Also it shows how to use schema of domain.

Table of Contents
------------------
1. [Running application](#running-application).
2. [Configuration](#configuration).

Running application
-------------
To start working with the test case you should have installed `Oracle/Sun Java JDK  1.6 or 1.7`, `Apache Maven 3.x` and `Git` (or anther tool that allow you clone the project from GitHub). Follow next steps to build application:
1. Clone the repository to your local computer using Git command: 
```java
git clone https://github.com/Jaspersoft/domain-sample-application.git
```
or download source code directly from the main page of repository `https://github.com/Jaspersoft/domain-sample-application`
2. Run biuld of application form command line with goal:
```java
mvn package
```
You can find executable jar file in `target` folder.
To run application enter in command line:
```java
java -jar domain-sample-application-1.0.jar
```
Configuration
-------------
To use the application you should specify  set of parameters such as:
- `uri` -  URI of JasperReportsServer;
- `username` and `password` - login and password of your account on server;
- `baseFolder` - base directory, where samples will be located.

Domain sample application allow you set this parameters from command line or enter path to `properties` file.

In case of manual configuration the application suggest to enter each parameter separate, here is example of work the application in this case:
```
[INFO] Initialization of application
[INFO] Choose way of configuration (file or manual) [f/m]: m
[INFO] The application is configured manually
[INFO] Enter url: http://build-master.jaspersoft.com:9080/jrs-pro-feature-domain-designer-schema-conversion
[INFO] Enter username: superuser
[INFO] Enter password: superuser
[INFO] Enter baseFolder: /public/demo
[INFO] Authentication on JasperReportsServer http://build-master.jaspersoft.com:9080/jrs-pro-feature-domain-designer-schema-conversion

...
```
You can specify init parameters in `property` file and set path to this file for application:
```
[INFO] Initialization of application
[INFO] Choose way of configuration (file or manual) [f/m]: f

[INFO] The application is configured from file
[INFO] Enter path to configuration file: D:\config.properties

[INFO] The application is configured from D:\config.properties file
[INFO] Authentication on JasperReportsServer http://build-master.jaspersoft.com:9080/jrs-pro-feature-domain-designer-schema-conversion

...

```
 Here is example of `config.property` file:
 ```
 url=http://build-master.jaspersoft.com:9080/jrs-pro-feature-domain-designer-schema-conversion
 username=superuser
 password=superuser
 baseFolder=/public/DomainDemo

 ```