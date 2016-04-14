# Sample application for demonstration Domain metadata API
===========================================================

Table of Contents
------------------
1. [Running application](#running-application).
2. [Configuration](#configuration).

Running application
-------------
To start working with application you should have installed `Oracle/Sun Java JDK  1.6 or 1.7`, `Apache Maven 3.x` and `Git` (or anther tool that allow you clone the project from GitHub). Follow next steps to build application:
1. Clone the repository to your local computer using Git command: 
```java
git clone https://github.com/Jaspersoft/domain-sample-application.git
```

or download source code directly from the main page of repository `https://github.com/Jaspersoft/domain-sample-application`

and switch to `develop-domainQuerySampleApp` branch
```java
git checkout develop-domainQuerySampleApp
```
2. Run biuld of application form command line with goal:
```java
mvn package
```
You can find executable jar file in `target` folder.
To run application enter in command line:
```java
java -jar  domain-query-sample-application-1.0.jar
```
Configuration
-------------
To use the application you should specify  set of parameters such as:
- `uri` -  URI of JasperReportsServer;
- `username` and `password` - login and password of your account on server;
- `domainUri` - URI of domain which metadata will be retrieved.

Sample application allow you set this parameters from command line or enter path to `properties` file.

In case of manual configuration the application suggest to enter each parameter separate, here is example of work the application in this case:
```
[INFO] Initialization of application
[INFO] Choose way of configuration (file or manual) [f/m]: m
[INFO] The application is configured manually
[INFO] Enter url: http://build-master.jaspersoft.com:6380/jrs-pro-feature-domain-topic-metadata-api
[INFO] Enter username: superuser
[INFO] Enter password: superuser
[INFO] Enter domainUri: /public/Samples/Domains/supermartDomain
[INFO] Authentication on JasperReportsServer http://build-master.jaspersoft.com:6380/jrs-pro-feature-domain-topic-metadata-api

...
```
You can specify init parameters in `property` file and set path to this file for application:
```
[INFO] Initialization of application
[INFO] Choose way of configuration (file or manual) [f/m]: f

[INFO] The application is configured from file
[INFO] Enter path to configuration file: D:\config.properties

[INFO] The application is configured from D:\config.properties file
[INFO] Authentication on JasperReportsServer http://build-master.jaspersoft.com:6380/jrs-pro-feature-domain-topic-metadata-api

...

```
 Here is example of `config.properties` file:
 ```
 url=http://build-master.jaspersoft.com:9080/jrs-pro-feature-domain-designer-schema-conversion
 username=superuser
 password=superuser
 domainUri=/public/Samples/Domains/supermartDomain

 ```
