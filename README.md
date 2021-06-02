# Rest-Assured Automation Example

### Endpoints
- `/users` - all users

## Framework
Supports RestAssured library with JAva, TestNG, Maven and Extent Reports

### Structure
This project is your standard Maven Java project with `src` folders and `POM.xml`.

### models
`src/main/java/models` represent API entities with class properties equal to JSON response fields. This lets us serialize and deserialize  requests and responses easily.
In order for the serialization to work properly, names of the fields must match JSON convention thus they violate Java camel case convention (it can be overriden using `@JsonProperty` annotations in real-life projects).

### Properties
`src/test/resources/config.properties` is a simple properties file to store various configurations
src/test/resources/message.properties` is a simple properties file to store various message that are used 
while logging assertions in Extent reports

### Tests
src/main/java/utils contains custom implementations
`src/test/java/DemoTest.Java` holds sample test classes (TestNG) 