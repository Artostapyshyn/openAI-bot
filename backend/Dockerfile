FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
COPY --from=build /target/openAI-bot-0.0.1-SNAPSHOT.jar app.jar
COPY /src/main/resources/application.properties /usr/app/

ENV DB_URL=${DB_URL} \
    DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD} \
    DDL_AUTO=${DDL_AUTO} \
    BOT_USERNAME=${BOT_USERNAME} \
    BOT_TOKEN=${BOT_TOKEN} \
    OPEN_AI_KEY=${OPENAI_API_KEY} \
    PORT=8080

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar", "--spring.config.location=file:/usr/app/application.properties", "--spring.datasource.url=${DB_URL}", "--spring.datasource.username=${DB_USERNAME}", "--spring.datasource.password=${DB_PASSWORD}", "--spring.jpa.hibernate.ddl-auto=${DDL_AUTO}", "--bot.username=${BOT_USERNAME}", "--bot.token=${BOT_TOKEN}", "--openai.api.key=${OPEN_AI_KEY}"]