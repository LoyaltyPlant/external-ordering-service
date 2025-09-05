FROM dckr.loyaltyplant.com/java-21-microservice:2024.11.21-1

ARG JAR_FILE=toast-instore-validator.jar
ENV JAR_FILE=$JAR_FILE

ADD ./target/$JAR_FILE .