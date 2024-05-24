# Quarkus RESt API example

This project uses Quarkus to build REST APIs.

Note I tested this project using Java 11 (so remember to change your JAVA_HOME to use java 11)

## Running the application in `dev` mode

First start the docker:

```bash
$ cd docker && docker-compose up -d
```

Then create tables with some predefined data,

```bash
$ ./db_initializer.sh
```

Finally, you can run your application in dev mode that enables live coding by going back out into the root folder and
then calling:

```bash
$ ./mvnw quarkus:dev
```

## Interacting with APIs

The app runs on `localhost:8080`. You can interact with the APIs via Swagger http://localhost:8080/swagger-ui.html/

Example account that you can use for authentication:

- Username: admin
- Password: admin

Alternatively you can use CURL as follows,

```bash
# get list of accounts (secured, accessible to accounts with 'ADMIN' or 'USER' role)
curl -X GET "localhost:8080/v1/accounts" -H "accept: application/json" -u leo:1234

# create a account (open)
curl -X POST "localhost:8080/v1/accounts" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"firstName\":\"Tom\",\"lastName\":\"Cruise\",\"password\":\"password\",\"role\":\"USER\",\"username\":\"tomC\"}" -u admin:admin

# deposit money on an account 
curl -X POST "localhost:8080/v1/accounts/1/deposit" -H "accept: application/json" -H "Content-Type: application/json" -d "1" -u leo:1234
```

## Debugging the application

To debug the app, run the following command first,

```bash
$ ./mvnw quarkus:dev -Ddebug
```

This activates debug on port `5005`. Then use your IDE to connect to `localhost:5005` to debug the application.

# Some relevant Design Decisions

### Based code on external project

For a quicker start I based my code on https://github.com/kasramp/quarkus-rest-example since I have not worked with
quarkus before.

### Architecture: Controller - Service - Repository

The Architecture I went with was a simple Controller - Service - Repository (CSR) pattern. Where:
Controller: is responsible for handling incoming requests.
Service: is responsible for encapsulating the business logic.
Repository: is responsible for data access logic.

### Balance property is not part of DTO

Reason for this is that balance should only be manipulated through transfers & deposits.

### Using POST instead of PUT for the deposit/transfer methods

We use POST instead of PUT as this is only a partial update and not a FULL-UPDATE of the resource.

### Mockito should be used for better/cleaner testing

Decided not to bother using mocking framework such as Mockito due to it taking some time to get up and running.
So creating test data through calls to REST ATM which is not ideal.