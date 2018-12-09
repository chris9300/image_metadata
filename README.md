# rspace-img-metadata

###Setup:

Before running tests or launching the application, you have to config the application.properties and to setup the MySql Database.

**Application.propteries:**
    
There are two files: One for testing (test/resources) and one for productive use (main/resources).
Do not use the same database for both, otherwise the tests will reset the productiv database!

In the application.properties files are the database and server configs. Ensure that they are fit with your server and that your server is available.

**Database**

You have to create the database on your MySql server previously (for productive as well as for test cases). You don't need to create any tables in the database.

The Mysql version has to be 8.0 or higher! 

###Run

To run tests:

    mvn clean test
    
To launch application in dev environment

    mvn spring-boot:run
    
To build and run from target folder

    mvn clean package && java -jar target/rspace-img-metadata-0.0.1-SNAPSHOT.jar
    
    
###API Functions
**Variables:**

- {server}: The route to the server, e.g. http://localhost:8080

- {user_id}: String between 1 and 255 characters

- {customer_id}: String between 1 and 255 characters

- {image_id}: Positive integer with up to 20 digits

- {version_no}: Positive integer with up to 11 digits  

The {customer_id}, {image_id} and {version_no} together have to be a unique key


**Insert**

Request:

    Http-Type: PUT
    Request Body: Form-data, image File (as MultipartFile)
   
        curl -F "file=@{local_filename}" /img_metadata/insert/{customerId}/{userId}/{rspaceImageId}/{version} 
   
Response:
    
    Positive:
        ResponseCode: NO_CONTENT (204)
        Body: -
    Errors:
        - Entry already exists: CONFLICT (409)
        - Wrong file: UNSUPPORTED_MEDIA_TYPE (415) 
        - Invalid Url-Arguments: BAD_REQUEST (400) 

**Update**

Request:

    Http-Type: POST
    Request Body: Form-data, image File (as MultipartFile)
   
        curl -F "file=@{local_filename}" server/img_metadata/update/{customer_id}/{user_id}/{rspaceImage_id}/{version_no} 
   
Response:
    
    Positive:
        ResponseCode: NO_CONTENT (204)
        Body: -
    Errors:
        - Wrong file: UNSUPPORTED_MEDIA_TYPE (415) 
        - Invalid Url-Arguments: BAD_REQUEST (400) 

**Get**

Request:

    Http-Type: GET
    Request Body: -
   
        curl server/img_metadata/{customer_id}/{image_id}/{version_no}
   
Response:
    
    Positive:
        ResponseCode: OK (200)
        Body: jsonObject as String
    Errors:
        - No image metadata found for the Key: NO_FOUND (404) 
        - Invalid Url-Arguments: BAD_REQUEST (400) 
**Delete**

Request:

    Http-Type: DELETE
    Request Body: -
   
        curl -X DELETE server/img_metadata/delete/{customer_id}/{image_id}/{version_no}
   
Response:
    
    Positive:
        ResponseCode: NO_CONTENT (204)
        Body: -
    Errors:
        No image metadata found for the Key: NO_FOUND (404) 

**Get Keys**
    
Requst:

    Https-Type: GET
    Request Body: -
    
        curl server/img_metadata/topKeys
        curl server/img_metadata/allKeys


####**Search functions**

The Search functions are separated into two parts:

- Prefix search 
- Exact term search

In both searches can include wildcards. These two Wildcards are allowed:

 - '%' allows any number of any character
 - '_' allows exactly one character
 
 Furthermore two restrictions are possible.
 
  - UserIDs:
    Restrict the search on the images of one (or more) given userIDs
  - Target Keys:
    Restrict the search on one (or more) keys in the metadata. 
    
    Consider that keys have to match exactly. It is not possible to search directly for a lower level key. In this case you need to use the complete key path.
    
**Search in all datasets:**

Request:

    Http-Type: GET
    Request Body: -
   
        curl server/img_metadata/search/{searchTerm}
        curl server/img_metadata/search/prefix/{searchTerm}
   
Response:
    
    Positive:
        ResponseCode: NO_CONTENT (204)
        Body: jsonArray with jsonObjects (String) 
    Errors:
        Searchpath Arg cannot be parsed to String: BAD_REQUEST (400) 

**Search in certain keys:**

Request:

    Http-Type: POST
    Request Body: Json array: ["key1", "key2", … ]
   
        curl -H "Content-Type: application/json" -d '{body}' server/img_metadata/search/inKeys/{searchTerm} TODO 
        curl -H "Content-Type: application/json" -d '{body}' server/img_metadata/search/prefix/inKeys/{SearchPrefix} TODO
   
Response:
    
    Positive:
        ResponseCode: NO_CONTENT (204)
        Body: jsonArray with jsonObjects (String) 
    Errors:
        - Searchpath Arg cannot be parsed to String: BAD_REQUEST (400) 
        - Invalid Json Keys Array: UNPROCESSABLE_ENTITY (422)
        
**Search in datasets of certain users:**

Request:

    Http-Type: POST
    Request Body: Json array: ["user_id1", "user_id2", … ]
   
        curl -H "Content-Type: application/json" -d '{body}' server/img_metadata/search/ofUsers/{searchTerm}
        curl -H "Content-Type: application/json" -d '{body}' server/img_metadata/search/prefix/ofUsers/{SearchPrefix}
   
Response:
    
    Positive:
        ResponseCode: NO_CONTENT (204)
        Body: jsonArray with jsonObjects (String) 
    Errors:
        - Searchpath Arg cannot be parsed to String: BAD_REQUEST (400) 
        - Invalid Json Users Array: UNPROCESSABLE_ENTITY (422)

**Search in certain keys of datasets of certain users:**

Request:

    Http-Type: POST
    Request Body: Json Object: {"keys":["key1", "key2", … ], "users":["user_id1", "user_id2", … ]}
   
        curl -H "Content-Type: application/json" -d '{body}' server/img_metadata/search/inKeys/ofUsers/{searchTerm}
        curl -H "Content-Type: application/json" -d '{body}' server/img_metadata/search/prefix/inKeys/ofUsers/{SearchPrefix}
   
Response:
    
    Positive:
        ResponseCode: NO_CONTENT (204)
        Body: jsonArray with jsonObjects (String) 
    Errors:
        - Searchpath Arg cannot be parsed to String: BAD_REQUEST (400) 
        - Invalid Json Parameter Object: UNPROCESSABLE_ENTITY (422)