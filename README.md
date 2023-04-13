This repository contains an AWS-Lambda integration (SAM template.yaml + AWS Lambda Handler) for Spring Boot Application: REST API + CRUD PostgreSQL. For now embedded H2 DB is being used. Product DB can be later provisioned with the required credentials in the src/main/resources/application.properties file.



## Build the project and create the package
```
mvn clean package
```

## Run Spring Boot application
```
mvn spring-boot:run
```
## Deploy AWS Lambda + API Gateway with SAM 
```
sam build 
sam deploy --guided 
```
