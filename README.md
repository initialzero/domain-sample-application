# Sample Application to Demonstrate Domain Metadata and QueryExecution API 
===========================================================

Table of Contents
------------------
1. [Description](#description)
2. [Building the Application](#building-the-application)
3. [Running the Application](#running-the-application)
4. [Configuration](#configuration)


Description
-----------

The purpose of sample application is demonstration of connection to a compatible instance of JasperReports Server, reading the metadata (schema) of a selected Domain or Ad hoc view that is already defined in the repository, building various types of queries and execution of the queries. 

Building the Application
------------------------

Sofware requirements:
- Oracle/Sun Java JDK  1.6 or 1.7
- Apache Maven 3.x
- Git (or anther tool that allowS you clone the project from GitHub)

To build the sample app:
1. Clone the repository to your local computer using Git command: 
```bash
git clone https://github.com/Jaspersoft/domain-sample-application.git
```
Or download the source code directly from the main page of repository: `https://github.com/Jaspersoft/domain-sample-application`
2. Switch to the `develop-domainQueryExecutionSampleApp` branch:

```bash
git checkout develop-domainQueryExecutionSampleApp
```
The next 3 steps are temporary, until the Metadata REST API is released on JRS. Until then, you must also have already built `JasperReportsServer-CE` and `JasperReportsServer-PRO` version `6.2.0-adhoc-rest-api-SNAPSHOT` (from  `amber-ce-adhoc-rest-api` SVN branch). 
3. Clone the JRSRestClient repository from GitHub:
```bash
cd ..
git clone https://github.com/Jaspersoft/jrs-rest-java-client.git
```
4. Switch to its `develop-domainQuery` branch:
```bash
git checkout branch develop-queryExecution
```
5. In the JRSRestClient project, run the Maven `install` target:
```bash
mvn install
```
6. In the sample application project, build with the Maven `package` target:
```bash
cd ../domain-sample-application
mvn package
```

Running the Application
-----------------------

Maven creates the executable jar file in the `target` folder. To run the sample application, use the following commands:

```bash
cd target
java -jar domain-query-sample-application-1.0.jar
```

Configuration
-------------

Semple application must be properly configured in two ways: 

1. from `properties` file. To use this way of configuration choose `f` for `file` when prompted by the application and enter the path to your local file. For example:
```
[INFO] Initialization of application
[INFO] Choose way of configuration (file or manual) [f/m]: f
[INFO] The application is configured from file
[INFO] Enter path to configuration file: D:\config.properties
[INFO] The application is configured from D:\config.properties file
[INFO] Authentication on JasperReportsServer http://build-master.jaspersoft.com:6380/jrs-pro-feature-domain-topic-metadata-api
...
```
Previously create a Java properties file with the following properties:
 ```
 url=http://build-master.jaspersoft.com:9080/jrs-pro-feature-domain-designer-schema-conversion
 username=superuser
 password=superuser
 domainUri=/public/Samples/Domains/supermartDomain
 resultDirectory=resultData
 responseFormat=json
 ```
 Where:
 - `url` - URL of JasperReports Server
- `username` and `password` - login and password of your account on JasperReports Server
- `domainUri` - Repository URI of the target Domain
- `resultDirectory` - Local folder where files with result datasets are saved
- `responseFormat` - Whether to use `json` or `xml`

2. configuration of applicatopn interactively on the command line. 
To enter the parameters interactively, choose `m` for `manual` when prompted for the configuration, then type in each parameter. For example:

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
