FROM maven:3.5.2-jdk-8-alpine
WORKDIR /app
ADD . .
CMD ["mvn", "spring-boot:run"]
