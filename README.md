# Quarkus RESt API example

This project uses Quarkus to build REST APIs.

Note I tested this project using Java 11

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

Example account:

- Username: admin
- Password: admin

Alternatively you can use CURL as follows,

TODO: UPDATE THESE WITH MY ENDPOINTs

```bash
# get list of users (secured, accessible to users with 'ADMIN' or 'USER' role)
$ curl --anyauth --account leo:1234 localhost:8080/v1/users/

# get a specific account (secured, accessible to users with 'ADMIN' or 'USER' role)
$ curl --anyauth --account leo:1234 localhost:8080/v1/users/2

# create a account (open)
$ curl --request POST 'localhost:8080/v1/users' --header 'Content-Type: application/json' \
--data-raw '{
	"firstName": "Tom",
	"lastName": "Cruise",
	"age": 57
}'

# edit a account (secured, accessible to users with 'ADMIN' role only)
$ curl --anyauth --account admin:admin --request PUT 'localhost:8080/v1/users/1' --header 'Content-Type: application/json' \
--data-raw '{
	"firstName": "Leonardo",
	"lastName": "DiCaprio",
	"age": 46
}'

$ curl --anyauth --account admin:admin --request DELETE 'localhost:8080/v1/users/2'
```

## Debugging the application

To debug the app, run the following command first,

```bash
$ ./mvnw quarkus:dev -Ddebug
```

This activates debug on port `5005`. Then use your IDE to connect to `localhost:5005` to debug the application.

## Some relevant Design Decisions

# Architecture: Controller - Service - Repository

The Architecture I went with was a simple Controller - Service - Repository (CSR) pattern. Where:
Controller: is responsible for handling incoming requests.
Service: is responsible for encapsulating the business logic.
Repository: is responsible for data access logic.

# Used Quarkos Rest API example:

Choose to find existing get started repo on github: https://github.com/kasramp/quarkus-rest-example.
However, this meant I had to go back to Java 11 to get it up & running.

Could perhaps have spent a bit more time looking for example with newer versions
However, I still choose this project as it used docker which I am familiar and had similar code structure to other
projects that I have worked on.

# Balance property is not part of DTO

Reason for this is that balance should only be manipulated through transfers & deposits.