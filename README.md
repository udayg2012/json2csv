# Json2Csv

Parses Json String and converts it to CSV output. This API supports nested JSON objects and JSON arrays.

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

#### Request Payload:

```java
[{
    "key1": "value1a",
    "key2": ["value2a", "value3a", "value4a"],
    "key3": {
        "subkey1": "subvalue1a",
        "subkey2": "subvalue2a",
        "subkey3": {
            "subsubkey1": ["subsubvalue1a", "subsubvalue2a"]
        },
        "subkey4": "subvalue4a"
    },
    "key4": "value4a",
    "key5": {
        "subkey5": "subvalue5a"
    }
},
{
    "key1": "value1b",
    "key2": ["value2b", "value3b", "value4b"],
    "key3": {
        "subkey1": "subvalue1b",
        "subkey2": "subvalue2b",
        "subkey3": {
            "subsubkey1": ["subsubvalue1b", "subsubvalue2b"]
        },
        "subkey4": "subvalue4b"
    },
    "key4": "value4b",
    "key5": {
        "subkey5": "subvalue5b"
    }
}]
```

#### Response

```csv
key1,key2[0],key2[1],key2[2],key3.subkey1,key3.subkey2,key3.subkey3.subsubkey1[0],key3.subkey3.subsubkey1[1],key3.subkey4,key4,key5.subkey5
value1a,value2a,value3a,value4a,subvalue1a,subvalue2a,subsubvalue1a,subsubvalue2a,subvalue4a,value4a,subvalue5a
value1b,value2b,value3b,value4b,subvalue1b,subvalue2b,subsubvalue1b,subsubvalue2b,subvalue4b,value4b,subvalue5b
```

![Screenshot 2026-01-12 at 9.34.03 PM](assets/Screenshot%202026-01-12%20at%209.34.03%E2%80%AFPM.png)
--