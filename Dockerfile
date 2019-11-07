# build base image
FROM maven:3-jdk-8-slim as maven

# copy pom.xml
COPY ./pom.xml ./pom.xml

# copy components files
COPY ./src ./src

# build service
RUN mvn package

# final base image
FROM openjdk:8u171-jre-alpine

# set deployment directory
WORKDIR /mod-data-extractor

# copy over the built artifact from the maven image
COPY --from=maven /target/mod-data-extractor*.jar ./mod-data-extractor.jar

#Settings
ENV LOGGING_LEVEL_FOLIO='INFO'
ENV SERVER_PORT='9002'
ENV SPRING_DATASOURCE_PLATFORM='h2'
ENV SPRING_DATASOURCE_URL='jdbc:h2:./mod-data-extractor;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE'
ENV SPRING_DATASOURCE_DRIVERCLASSNAME='org.h2.Driver'
ENV SPRING_DATASOURCE_USERNAME='folio'
ENV SPRING_DATASOURCE_PASSWORD='folio'
ENV SPRING_H2_CONSOLE_ENABLED='true'
ENV SPRING_JPA_DATABASE_PLATFORM='org.hibernate.dialect.H2Dialect'
ENV TENANT_DEFAULT_TENANT='tern'
ENV EXTRACTION_DATASOURCE_URL='jdbc:oracle:thin:@localhost:1521:VGER'
ENV EXTRACTION_DATASOURCE_USERNAME='admin'
ENV EXTRACTION_DATASOURCE_PASSWORD='admin'
ENV EXTRACTION_DATASOURCE_DRIVERCLASSNAME='oracle.jdbc.OracleDriver'
ENV EXTRACTION_DATASOURCE_VALIDATION_QUERY='select version();'
ENV EXTRACTION_JPA_DATABASE_PLATFORM='org.hibernate.dialect.Oracle10gDialect'
ENV EXTRACTION_SCHEMA_VOYAGER_TABLETYPES='TABLE'
ENV EXTRACTION_SCHEMA_VOYAGER_SELECTION='evans:AMDB,evans:MSDB'

#expose port
EXPOSE ${SERVER_PORT}

#run java command
CMD java -jar ./mod-data-extractor.jar \
    --logging.level.org.folio=${LOGGING_LEVEL_FOLIO} --server.port=${SERVER_PORT} --spring.datasource.platform=${SPRING_DATASOURCE_PLATFORM} \
    --spring.datasource.url=${SPRING_DATASOURCE_URL} --spring.datasource.driverClassName=${SPRING_DATASOURCE_DRIVERCLASSNAME} \
    --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
    --spring.h2.console.enabled=${SPRING_H2_CONSOLE_ENABLED} --spring.jpa.database-platform=${SPRING_JPA_DATABASE_PLATFORM} \
    --tenant.default-tenant=${TENANT_DEFAULT_TENANT} --extraction.datasource.url=${EXTRACTION_DATASOURCE_URL} \
    --extraction.datasource.username=${EXTRACTION_DATASOURCE_USERNAME} --extraction.datasource.password=${EXTRACTION_DATASOURCE_PASSWORD} \
    --extraction.datasource.driverClassName=${EXTRACTION_DATASOURCE_DRIVERCLASSNAME} --extraction.datasource.validation-query=${EXTRACTION_DATASOURCE_VALIDATION_QUERY} \
    --extraction.jpa.database-platform=${EXTRACTION_JPA_DATABASE_PLATFORM} --extraction.schema.voyager.tableTypes=${EXTRACTION_SCHEMA_VOYAGER_TABLETYPES} \
    --extraction.schema.voyager.selection=${EXTRACTION_SCHEMA_VOYAGER_SELECTION}