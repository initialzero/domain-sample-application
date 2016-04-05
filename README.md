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
To run application enter in comman line:
```java
java -jar -Dlog4j.configuration=file:"./path/to/log4j.properties" domain-sample-application-1.0-jar-with-dependencies.jar
```
Configuration
-------------
To use the application you should specify  set of parameters such as:
- URI of JasperReportsServer;
- username;
- password;
- base foder, where samples will be located.

Domain sample application allow you set this parameters from command line or enter path to `properties` file.
