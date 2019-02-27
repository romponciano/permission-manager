# Gerenciador de permissões para Plug-Ins
A Java project demonstrating the use of RMI, Client / Server architecture, multi-module management with maven and CRUD with Oracle database. Two interfaces developed for demonstration of code sharing: one with Java Swing and one with JavaFX

1. [About](#1-About)
2. [How to run](#2-How-to-run)
3. [Troubleshooting](#3-Troubleshooting)
4. [License](#4-License)

## 1. About

Your work is to develop a Java application with Swing and JavaFX graphical interface, based on a "client / server" architecture using RMI. This application must be able to create, edit and consult the data of the plug-ins, their features and permissions of the users. In addition, it is desirable to create a minimum set of unit tests.

1) Use Oracle XE (http://www.oracle.com/) as SGBD;

2) Model a database solution to store user data, plug-ins, functionalities, and permissions.

Insert a ".sql" script into the "User" entity data:
2.1) Login (4 caracteres);
2.2) Name;
2.3) Status (active or inative);
2.4) Current Manager (it is not necessary to create a dictionary table for this information);

3) Based on the "exam.zip" project (link), develop the Permissions Management Module (PMM) to assign and remove plug-in functionality to registered users.

Obs.: create the "scripts" directory in your project, and add the database physical template creation scripts and the user registration script.

This PMM should have the following features:

3.1) Query, create, edit and delete plug-ins. The Plug-In entity has the following attributes:
3.1.1) Name;
3.1.2) Description;
3.1.3) Creation date;

3.2) Query, create, edit and delete plug-in functionality. The Functionality feature has the following attributes:
3.2.1) Name;
3.2.2) Description;
3.2.3) Creation date;

3.3) Query, create, edit and delete users. It should be possible to filter and/or sort users by:
3.3.1) Login;
3.3.2) Name;
3.3.3) Status;
3.3.4) Current manager;
3.3.5) Plug-Ins;
3.3.6) Functionalities;

3.4) Assign and remove plug-in functionalities to registered users.

## 2. How to run

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
1. JDK 8 161 (https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
```
java version "1.8.0_161"
Java(TM) SE Runtime Environment (build 1.8.0_161-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)
```

2. Apache Maven 3.5.0 (https://maven.apache.org/docs/3.5.0/release-notes.html)

3. Install an Oracle Database (https://www.oracle.com/technetwork/database/database-technologies/express-edition/downloads/index.html) with minVersion >= 12g. 
If you prefer, you could use some docker image with oracle, like https://github.com/fuzziebrain/docker-oracle-xe

4. After install and execute Oracle Database properly, run the scripts inside scripts folder. It will create all tables.

### Getting started

1. Download or clone this repo.

2. Open common/Const.java and change 
```
public static final String DB_URL = "jdbc:oracle:thin:@localhost:32118/XE"; to your DB_URL
public static final String USER = "ROMULOPONCIANO"; to the user created in you DB
public static final String PASS = "root"; to the password of your user
```
Save it.

2.1 (Optional) Choose the interface you want to start: open client/src/main/java/client/Client.java, then comment/uncomment your choice.
```
initSwingGUI();
initFXGUI();
```
Save it.

3. Inside root folder, execute 
```
mvn clean package install
```
And you should see:
```
Reactor Summary:
[INFO] 
[INFO] root ............................................... SUCCESS 
[INFO] common ............................................. SUCCESS 
[INFO] server ............................................. SUCCESS 
[INFO] client ............................................. SUCCESS 
```
This means that all modules (common, server and client) have been compiled and built successfully and in the corret order.

4. Run the server's jar file
```
java -jar server/target/server.....-with-dependencies.jar
```
If you see 
```
Server Ready!
```
Then you are good to go!

5. Run the client's jar file
```
java -jar client/target/client.....-with-dependencies.jar
```

You should see an interface and done! 

## 3. Troubleshooting

### Server build failure after mvn clean package install
```
[INFO] root ............................................... SUCCESS 
[INFO] common ............................................. SUCCESS 
[INFO] server ............................................. FAILURE 
[INFO] client ............................................. SKIPPED
[ERROR] Failed to execute goal on project server: Could not resolve dependencies for project tecgraf-exam:server:jar:0.0.1-SNAPSHOT: Could not find artifact com.oracle:ojdbc6:jar:11.2.0.3 in central (https://repo.maven.apache.org/maven2)
```
If you see this error, it means that maven had a problem downloading the jar from ojdbc6. To resolve it, execute the following command:
```
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=server/lib/ojdbc6-11.2.0.3.jar -DgeneratePom=true
```
It will install the dependency based on a jar already inside the project. 
Now go back to step 3, inside How to Run, and try again. :)

## 4. License

The code is under the [Common Creative License BY-NonCommercial](https://creativecommons.org/licenses/by-nc/4.0/legalcode)
