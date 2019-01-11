FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/hello-micronaut*.jar hello-micronaut.jar
CMD java ${JAVA_OPTS} -jar hello-micronaut.jar