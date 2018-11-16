# rspace-img-metadata

###Setup:

Before running tests or launching the application, you have to config the application.properties and to setup the MySql Database.

**Application.propteries:**
    
There are two files: One for testing (test/resources) and one for productive use (main/resources).
Do not use the same database for both, otherwise the tests will reset the productiv database!

In the application.properties files are the database and server configs. Ensure that they are fit with your server and that your server is available.

**Database**

You have to create the database on your MySql server previously (for productive as well as for test cases). You don't need to create any tables in the database. 

###Run

To run tests:

    mvn clean test
    
To launch application in dev environment

    mvn spring-boot:run
    
To build and run from target folder

    mvn clean package && java -jar target/rspace-img-metadata-0.0.1-SNAPSHOT.jar
   