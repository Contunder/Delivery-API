
# User API

## Feature

- CRUD User
- User Management
- CRUD User
- Github Action
- Docker

## Need for work

- AuthenticationAPI : https://github.com/Contunder/Authentication
- TrackingAPI : https://github.com/Contunder/Tracking

## Coverage 

## Links 
- Github  
  - Back End Java API : https://github.com/Contunder/UserAPI  
  - Front End React : https://github.com/Contunder/SupHotelReact  
  

- Docker
  - Docker Back : docker push valden01/suphotelapi:tagname  
    - Back Port Mapping : 8080
  - Docker Front : docker push valden01/suphotelreact:tagname
    - Front Port Mapping : 3000

## For Use

- Java 21.0.2
- Maven 3.9.2

## Config Database

- In /src/main/resources/application.properties
- Change spring.datasource with your config 
- The Database will be automaticly create at first launch
- Add Role in the table

>ROLE_USER  
>ROLE_ADMIN

- If you have any problems you can't change acces in SecurityConfig 

> .anyRequest().authenticated() -> .anyRequest().permitAll()

## Config project install

- mvn clean install

## SWAGGER AT 

- http://localhost:8080/

## PostMan files

- SupHotel.postman_collection.json

## Comment déployer sur Azure :


Avec le plug-in Maven pour Azure Web Apps.

une seule commande à la racine de votre projet :


>mvn com.microsoft.azure:azure-webapp-maven-plugin:2.11.0:config


puis ensuite déployer l’appli:


>mvn package azure-webapp:deploy


