#
# Build stage
#
FROM maven AS build
COPY src /Users/raul/Documents/search/src
COPY pom.xml /Users/raul/Documents/search/
RUN mvn -f /Users/raul/Documents/search/ clean package

#
# Package stage
#
FROM openjdk
COPY --from=build /Users/raul/Documents/search/target/demo_search-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]