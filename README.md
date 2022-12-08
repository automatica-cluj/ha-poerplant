


# ABB & Fimer Power Plant Data Collector

## Introduction
The project is a draft\WIP small app which is using Aurora Vision API to collect telemetry data from ABB\Fimer inverters and save it in a MySQL database.

Refference Fimer API documentation can be accessed here: https://documentation.auroravision.net/index.html%3Fp=74.html

Used frameworks & libraries:
- Spring Rest Data and Spring Data JPA
- Spring Boot
- Lombok - to generate boilerplate code
- MapStruct - for mapping between DTOs and entities
- Unirest Java - lightweight library for communicating with a REST API
- Swagger - to generate REST API documentation

## Running project 

### Before run
Fimer api key and base auth (user+password) must be set as system environment variables:
- fimer.api.key - will store api key (ex: 1111111-068c-1234-3456-fff2222c2539-0c81)
- fimer.api.basicauth - will store base aut (ex: Basic bf1paGspOk1saDI3azYrK0g=)

Pland id and Device id (inverter) must be set in application.properties file:
- fimer.plant.id=99222238
- fimer.device.id=11222248

Empty MySQL database should be created and connection parameters must be set in application.properties:
- spring.datasource.url=jdbc:mysql://localhost:3306/demoha1
- spring.datasource.username=root
- spring.datasource.password=root

### Run 
As a maven based Spring Boot project this can be run with following command: 
- mvn spring-boot:run 

After successful start application exposes a Swagger API documentation on: http://localhost:8080/swagger-ui/index.html# 

The application expose a single RPC HTTP GET which allow user to request getting measurements data and populating database. 

## Limitations 

In current draft version the application works with a single device. Is not possible to configure and handle multiple inverters at the same time. 


