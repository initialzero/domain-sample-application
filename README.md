# Sample application for demonstration Domain metadata and QueryExecution API 
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

and switch to `develop-domainQueryExecutionSampleApp` branch
```java
git checkout develop-domainQuerySampleApp
```

Also you need to do temporary additional steps to build Sample application:

1. clone JRSRestClient form GitHub
```java
git clone https://github.com/Jaspersoft/jrs-rest-java-client.git
```
2. checkout develop-domainQuery branch
```java
git checkout branch develop-domainQuery
```
3. run Maven goal "install"
```java
mvn install
```
These steps mean that you have built `JasperReportsServer-CE` and `JasperReportsServer-PRO` version `6.2.0-adhoc-rest-api-SNAPSHOT` (from  `amber-ce-adhoc-rest-api` SVN branch) 
These steps should be removed after Metadata API on JRS will be released. 

2. Run build of application form command line with goal:
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
- `resultDirectory` - local directory where files with result datasets will be saved
- `responseFormat` - 

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
 resultDirectory=resultData
 responseFormat=json
 ```
