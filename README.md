# WireMock server

This project consists on a standalone java application that loads previously generated WireMock-stubs and run them in a WireMock server.

### How to

The first thing to do is to import your stubs as dependency:
```xml
<dependency>
    <groupId>com.binary.mindset</groupId>
    <artifactId>api-contracts</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <classifier>stubs</classifier>
    <exclusions>
        <exclusion>
            <groupId>*</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

Then you may be able to run the main class 'WiremockRunner' and the result will be a Mock-based API whose behaviour corresponds to your stubs.

If you prefer it, you can build an executable jar file:
```
mvn clean package
```
and afterwards run it with the command:
```
java -jar wiremock-server-0.0.1-SNAPSHOT.jar
```