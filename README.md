# Sample application for demonstration Domain API(in development)

===========================================================

Table of Contents
------------------
1. [Description](#description)
2. [Building the Application](#building-the-application)
3. [Running the Application](#running-the-application)
4. [Configuration](#configuration)


Description
-----------

The purpose of sample application is demonstration of functionality of New Domain Designer API of JasperReports server. 
New Domain Designer, New Domain Schema and Domain API are in development at the present time and aren't supported by Jasperperreports Server 6.3.0. This funtionality will be included in subsequent releases.

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

2. If you use command line you should switch to `your_path/domain-sample-application` directory:
```bash
cd ../domain-sample-application
```

3. In the sample application project, build with the Maven `package` target:
```bash
mvn package
```

Running the Application
-----------------------

Maven creates the executable jar file in the `target` folder. To run the sample application, use the following commands:

```bash
cd target
java -jar domain-sample-application-1.1.jar
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
[INFO] Authentication on JasperReportsServer http://build-master.jaspersoft.com:5580/jrs-pro-feature-full-domain-api
```

Previously create a Java properties file with the following properties:
 ```
 url=http://build-master.jaspersoft.com:5580/jrs-pro-feature-full-domain-api
 username=superuser
 password=superuser
 baseFolder=/public/DomainDemo
 ```
 Where:
- `url` - URL of JasperReports Server
- `username` and `password` - login and password of your account on JasperReports Server
- `baseFolder` - base directory, where samples will be located.`


2. configuration of applicatopn interactively on the command line. 
To enter the parameters interactively, choose `m` for `manual` when prompted for the configuration, then type in each parameter. For example:

```
[INFO] Initialization of application
[INFO] Choose way of configuration (file or manual) [f/m]: m
[INFO] The application is configured manually
[INFO] Enter url: http://build-master.jaspersoft.com:9080/jrs-pro-feature-domain-designer-schema-conversion
[INFO] Enter username: superuser
[INFO] Enter password: superuser
[INFO] Enter baseFolder: /public/demo
[INFO] Authentication on JasperReportsServer http://build-master.jaspersoft.com:5580/jrs-pro-feature-full-domain-api
```
