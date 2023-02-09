This repository contains an AWS-Lambda integration (SAM template.yaml + AWS Lambda Handler) for Spring Boot Application: REST API + CRUD PostgreSQL. DB has to be provisioned with the required credentials (see application.properties).



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
