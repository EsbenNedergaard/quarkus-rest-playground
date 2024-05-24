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

Finally, you can run your application in dev mode that enables live coding by going back out into the root folder and then calling:

```bash
$ ./mvnw quarkus:dev
```

## Interacting with APIs

The app runs on `localhost:8080`. You can interact with the APIs via Swagger http://localhost:8080/swagger-ui.html/

Example user:
 - Username: leo
 - Password: 1234

Alternatively you can use CURL as follows,

```bash
# get list of users (secured, accessible to users with 'ADMIN' or 'USER' role)
$ curl --anyauth --user leo:1234 localhost:8080/v1/users/

# get a specific user (secured, accessible to users with 'ADMIN' or 'USER' role)
$ curl --anyauth --user leo:1234 localhost:8080/v1/users/2

# create a user (open)
$ curl --request POST 'localhost:8080/v1/users' --header 'Content-Type: application/json' \
--data-raw '{
	"firstName": "Tom",
	"lastName": "Cruise",
	"age": 57
}'

# edit a user (secured, accessible to users with 'ADMIN' role only)
$ curl --anyauth --user admin:admin --request PUT 'localhost:8080/v1/users/1' --header 'Content-Type: application/json' \
--data-raw '{
	"firstName": "Leonardo",
	"lastName": "DiCaprio",
	"age": 46
}'

# delete a user (secured, accessible to users with 'ADMIN' role only)
$ curl --anyauth --user admin:admin --request DELETE 'localhost:8080/v1/users/2'
```

## Debugging the application

To debug the app, run the following command first,

```bash
$ ./mvnw quarkus:dev -Ddebug
```

This activates debug on port `5005`. Then use your IDE to connect to `localhost:5005` to debug the application.


## Some relevant Design Decisions

# Used Quarkos Rest API example:
Choose to find existing get started repo on github: https://github.com/kasramp/quarkus-rest-example. 
However, this meant I had to go back to Java 11 to get it up & running.

Could perhaps have spent a bit more time looking for example with newer versions 
but choose this as it seemed the simplest to get up and running and had what I needed.