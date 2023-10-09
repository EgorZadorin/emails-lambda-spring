# Email Service Backend Component: Secure & Serverless 

## Overview

Designed by Egor Zadorin, this backend component is part of a larger [Email Service](https://emails.egorzadorin.com). It is built to be secure, confidential, and serverless. Hosted on AWS Lambda and implemented with Java Spring Boot, it capitalizes on the scalability and cost-efficiency of serverless architecture.

## Features

- **End-to-End Encryption**: Safeguards email addresses to prevent public exposure.
- **Immediate Notifications**: Triggered to a private email upon new email entry.
- **Secure Storage**: Emails are securely stored in a PostgreSQL database.
- **Scalability**: Built on AWS Lambda for automatic scaling.
- **RESTful API**: Adheres to RESTful guidelines for HTTP API operations.

## Architecture

This application is hosted on AWS Lambda and interfaces with a PostgreSQL database. It exposes a RESTful API via AWS API Gateway.

## Prerequisites

- Java JDK
- Maven
- AWS SAM CLI
- PostgreSQL database

## Getting Started

### Pre-setup

1. Provision your PostgreSQL database.
2. Customize the SAM template to fit your specific needs.

### Build the project

```
mvn clean package
```

### Run Locally
```
mvn spring-boot:run
```

### Deploy to AWS 
```
sam build 
sam deploy --guided 
```

## Configuration

Make sure to provision a PostgreSQL database before running the application. Update the database credentials in the `src/main/resources/application.properties` file.

## Contribution

If you find any issues or have suggestions for improvements, feel free to open an issue or make a pull request.

## License

MIT

