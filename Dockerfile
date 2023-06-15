FROM node:20-alpine3.17 AS uibuild
COPY ui/ /app/ui/
WORKDIR /app/ui
RUN npm install -g @angular/cli@16.0.2
RUN npm run build --production

FROM maven:3.9-eclipse-temurin-17-alpine AS bebuild
COPY backend/ /app/backend/
COPY --from=uibuild /app/ui/ /app/ui/

WORKDIR /app/backend
RUN mvn clean install

FROM eclipse-temurin:20.0.1_9-jre-jammy
WORKDIR /app
COPY --from=bebuild /app/backend/target/tubecc-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080

CMD ["java", "-jar", "tubecc-0.0.1-SNAPSHOT.jar"]

