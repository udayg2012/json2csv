# Json2Csv

Parses Json String and converts it to CSV output

## Spring Boot Application
Can be deployed to web server (e.g. Tomcat) as a microservice which can be accessible at URL *http://localhost:8080/api/json2csv*. You can submit requests using a client like Postman.

You can clone the application using git.

To build the application, run the following command from the root directory:

> mvn clean install

Defines RESTful API to perform CRUD operations on Sample object

1. **GET /ping** - Aliveness check
2. **POST /convert** - Converts specified (valid) JSON payload into CSV output.

## Examples

### Ping API for to check aliveness

GET localhost:8080/api/json2csv/ping

![Screenshot 2026-01-12 at 9.34.32 PM](assets/Screenshot%202026-01-12%20at%209.34.32%E2%80%AFPM.png)
--
### Convert JSON String to CSV

POST localhost:8080/api/json2csv/convert

![Screenshot 2026-01-12 at 9.34.03 PM](assets/Screenshot%202026-01-12%20at%209.34.03%E2%80%AFPM.png)
--