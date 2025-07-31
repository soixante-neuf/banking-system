# Simple Banking System

## Running the project

This is a Maven project 
so you'll need to install it to work with this project.
After that everything should work like any other Maven project.
The main code file is already specified in project configuration
therefore there is no need to specify it when running the project.

**To build and start the service:**
```shell
mvn clean compile exec:java
```

**To run tests:**
```shell
mvn test
```

**Note:** Since this is a Maven project
it should also work out-of-the-box
(or with minimal configuration)
with most modern IDEs and code editors
that support Java.
If you need the main class it is located at:

`src/main/java/com/bank/banking_system/BankingSystemApplication.java`

## Accessing the service

**Note:** In the root directory you can find `transactionsExample.csv`
which contains some dummy information to work with.

### SwaggerUI
**SwaggerUI was configured for the project**
so once the service is running you should be able to reach the service
by typing in `http://localhost:8080/swagger-ui/index.html` in to your browser bar.

### Other
If you want to access the service with tools like `curl`
it can be done at `localhost:8080/transactions`.
More specifically

**Import a file:** 

`localhost:8080/transactions/import`

**Export a file:**

`localhost:8080/transactions/export?dateFrom={yyyy-MM-dd}&dateTo={yyyy-MM-dd}`

`dateFrom` and `dateTo` are optional.

**Calculating balance of transactions over certain interval:**

`localhost:8080/transactions/balance?dateFrom={yyyy-MM-dd}&dateTo={yyyy-MM-dd}`

`dateFrom` and `dateTo` are optional. 
**You need to specify account number in the body of the request
with the name `accountIban`!**
