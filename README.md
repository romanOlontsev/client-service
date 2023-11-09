## Сlient-service

Web service for working with customer accounts.

### Stack

- Java 17
- Spring Boot
- PostgreSQL 
- JPA 
- Swagger 
- JUnit
- Mockito

### Requirements

The application can be run locally, the requirements for setup are listed below.

#### Local

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.3/apache-maven-3.9.3-bin.zip)

#### Docker

- [Docker](https://www.docker.com/get-docker)

### Quick Start

#### Package project

```sh
$ mvn package
```

#### Run Docker

```sh
$ docker-compose up
```

Application will run by default on port `8080`

Configure the port by changing services.service.ports in **docker-compose.yml**. 
Port 8080 was used by default so the value is easy to identify and change in the configuration file.

### API

The list of endpoints can be viewed via the link: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

- `GET` `/api/account`

Getting accounts according to parameters.

Params:
  
`lastname` `firstname` `middlename` `phonenumber` `email`

Response:

```json
[
  {
    "id": 0,
    "bank_id": 0,
    "last_name": "string",
    "first_name": "string",
    "middle_name": "string",
    "birth_date": "2000-01-01",
    "passport": "1234 123456",
    "birthplace": "string",
    "phone_number": "71112223344",
    "email": "string",
    "registration_address": "string",
    "residential_address": "string"
  }
]
```

- `POST` `/api/account`

Adding a client account based on header. Header name `x-Source`.

Supported headers:

`mail` `mobile` `bank` `gosuslugi`

Body:

```json
{
  "bank_id": 0,
  "last_name": "string",
  "first_name": "string",
  "middle_name": "string",
  "birth_date": "2023-11-09",
  "passport": "",
  "birthplace": "string",
  "phone_number": "72990511425",
  "email": "string",
  "registration_address": "string",
  "residential_address": "string"
}
```

- `GET` `/api/account/{id}`

Getting an account by id.

Response:

```json
{
  "id": 0,
  "bank_id": 0,
  "last_name": "string",
  "first_name": "string",
  "middle_name": "string",
  "birth_date": "2000-01-01",
  "passport": "string",
  "birthplace": "string",
  "phone_number": "71112223344",
  "email": "string",
  "registration_address": "string",
  "residential_address": "string"
}
```
