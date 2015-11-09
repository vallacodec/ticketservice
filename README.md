Basic information about the application

The application uses the spring boot which is powered by Tomcat container and uses H2 in-memory database

Steps to start the application
=================================
 1) mvn clean install(at the project level)
 2) java -jar target/ticket-service.jar

Testing
=======
Integration test and unit tests are under test package

dev environment:
================
Apache Maven 3.0.5
Java version: 1.8.0_51
IDE: intellj

Assumptions:
============

01) There is no maximum ticket per user
02) User selection seats cannot be random. it is given in the available level in the available order as per his minimum and maximum level input
03) If the minimum level is not present then the application assumes the minimum level as balcony2
04) If the maximum level is not present in the request then the application assumes the maximum level as Orchestra
05) The total number of seats requested by the user can be kept on hold and booked in one level only. It cannot spread across multiple level
06) The maximum number of seats the user can book si 25 which corresponds to balcony2
07) Reservation is not limited by total cost of that reservation