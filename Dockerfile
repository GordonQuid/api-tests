FROM maven:3.9.16-amazoncorretto-17-alpine

WORKDIR /allure
COPY . .

ENTRYPOINT ["mvn", "test"]