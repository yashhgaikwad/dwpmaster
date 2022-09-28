# Yash Dwp solution

## Setup and Build

### Java 11

You will need to have the Java 8 JDK (or higher) installed.  Check by typing the following into the command line:

    $ java -version
    java version "11.0.10" 2021-01-19 LTS
    Java(TM) SE Runtime Environment 18.9 (build 11.0.10+8-LTS-162)
    Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.10+8-LTS-162, mixed mode)
   
    $ javac -version
    javac 11.0.10
    Maven 3.6.3.

If these are not found or show a version lower than 1.8 then download and install the latest Java 8 JDK from [Oracle](https://www.oracle.com/technetwork/java/javase/downloads/index.html).

Once installed, the directory containing java should have been added to your PATH environment variable, so that the above `java` and `javac` commands will work from the command line. 

### Building and starting the server from the command line

    cd path/to/this/dir
    mvnw package            (./mvnw - if using bash)
    mvnw spring-boot:run    (Use Control-C or task-manager to stop)
    
Browse to <http://localhost:8080/swagger-ui/> expand the controller and "Try it out".

### Running in an IDE

This project uses Lombok to insert getters, setters and other boiler plate code during runtime.  As a consequence the raw code will show compilation errors in an IDE such as Eclipse, IntelliJ or Netbeans.

To avoid this use the Lombok plugin. Go to <https://projectlombok.org/> and select the installation instructions for your IDE from the "Install" menu.


## Notes on Implementation

### Assumptions

When finding users who are listed as living in London, I have assumed:

* That all users returned by <https://bpdts-test-app.herokuapp.com/city/London/users> are required, even if their coordinates appear to associate them with other places called London outside the UK.
 
When finding users who are currently within 50 miles of London, I have assumed:

* The latitude and longitude fields found in the data returned by the onward ReST service indicate the user's current position.
* That "London" in this case is the capital of Great Britain.
* That users should be within 50 miles of the centre of London (51.507222, -0.1275) as given by [Wikipedia](https://en.wikipedia.org/wiki/London).
* That this is 50 miles as the crow flies calculated using the [Haversine forumula](https://en.wikipedia.org/wiki/Haversine_formula).
* That users that fall into both categories should appear only once in the response.  

If the coordinates are not the user's position, then perhaps the ip\_address could be used to determine an approximate location.  This assumes the ip_address is associated with a current connection to a system by the user, and that a product such as [Maxmind's GeoIP2](https://www.maxmind.com) meets requirements.
 
### Spring-Webflux

I have chosen to use Spring-Webflux and Spring-Boot to solve this test.
In a high volume service, Webflux should stream the results from the onward ReST source without blocking and using less threads and memory. 

A more traditional Spring-MVC application would have required broadly similar code, with occurrences of `Flux<User>` being replaced by `List<User>`.  However, a single server thread would be entirely consumed from the moment the request was received until the entire response was returned.

### JSON Streaming [disabled]

Spring-Webflux supports [JSON streaming](https://en.wikipedia.org/wiki/JSON_streaming) but the Swagger-UI does not.
As the `application/stream+json` was appearing as the default MediaType in the UI and as the default resulted in the Swagger-UI displaying an error, I have disabled JSON streaming.

Streaming can be re-enabled by adding MediaType.APPLICATION\_STREAM\_JSON\_VALUE to the "produces" parameter on the services annotation:

    @GetMapping(path = "/londonusers", produces = {
                APPLICATION_JSON_VALUE,
                APPLICATION_STREAM_JSON_VALUE })
    ResponseEntity<Flux<User>> londonUsers() {
        ...
