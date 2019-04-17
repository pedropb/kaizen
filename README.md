# kaizen - users
This project is showcasing an implementation of a Restful micro service
using [SparkJava](https://sparkjava.com) and [jOOQ](https://jooq.org).

### What is "in the box"

- domain driven design architecture (domain <- api <- infrastructure).
- a single table repository implementation using jOOQ and optimist locking.
- restful CRUD implementation using Java 8 lambdas and SparkJava slim DSL.
- unit tests and integration tests (coded using TDD).

### Getting started

- `git clone https://github.com/pedropb/kaizen`
- Unit tests: `mvn test`
- Integration tests: `mvn verify`
- Packaging and running: `mvn package && java -jar target/users-1.0.jar`
- Browse to `http://localhost:4567`

### Examples

- Create a user
```bash
curl -X POST \
  http://localhost:4567 \
  -H 'Content-Type: application/json' \
  -d '{
	"name": "john",
	"email": "john@gmail.com"
}'
```


- Get a specific user
```bash
curl -X GET http://localhost:4567/:id
```

- Get user matching query
```bash
curl -X GET http://localhost:4567/?[idIn=][&nameStartsWith=][&emailIn=]
```
_`idIn` and `emailIn` accept multi-values  (i.e: `idIn=a&idIn=b&idIn=c`)_


- Get all users
```bash
curl -X GET http://localhost:4567/
```


- Update a specific user
```bash
curl -X PUT \
  http://localhost:4567/:id \
  -H 'Content-Type: application/json' \
  -d '{
	"name": "john",
	"email": "john@gmail.com"
}'
```

- Delete a specific user
```bash
curl -X DELETE http://localhost:4567/:id
```

