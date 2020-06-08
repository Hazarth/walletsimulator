# wallet simulator

Live demo of the API at:
https://wallet.styxcorp.com/

Documentation is generated using Swagger2:
https://wallet.styxcorp.com/swagger-ui.html

# Build Instructions
Project is built using Maven:
```shell script
mvn clean package -DSkipTests
```

# Running the application
Project can be ran using Java. For a production environment use the Spring profile Master by either
setting the environment variable `SPRING_PROFILES_ACTIVE=master` or by using the java argument `-Dspring.profiles.active=master`
when running the application.

```shell script
java -jar ./target/walletsim-0.0.1-SNAPSHOT.jar
```