#Prerequisites JDK
FROM maven:3.6.1-jdk-8-alpine

#Settings
ENV ARTIFACT_VERSION='1.3.0-SNAPSHOT'
ENV MODULE_VERSION='sprint5-staging'
ENV LOGGING_LEVEL_FOLIO='INFO'
ENV SERVER_PORT='8081'
ENV SPRING_DATASOURCE_PLATFORM='h2'
ENV SPRING_DATASOURCE_URL='jdbc:h2:./target/mod-data-extractor;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE'
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

#Mvn
RUN apk add --no-cache curl git

#mod-data-extractor clone and MVN build, includes Oracle ODBC driver
RUN mkdir -p /usr/local/bin/folio/
WORKDIR /usr/local/bin/folio
RUN git clone -b ${MODULE_VERSION} https://github.com/TAMULib/mod-data-extractor.git
WORKDIR /usr/local/bin/folio/mod-data-extractor
RUN mvn install:install-file -Dfile="lib/ojdbc8.jar" -DgroupId="com.oracle" -DartifactId="ojdbc8" -Dversion="12.2.0.1" -Dpackaging="jar" -DgeneratePom=true
RUN mvn package -DskipTests

#run java command
CMD java -jar /usr/local/bin/folio/mod-data-extractor/target/mod-data-extractor-${ARTIFACT_VERSION}.jar \
    --logging.level.org.folio=${LOGGING_LEVEL_FOLIO} --server.port=${SERVER_PORT} --spring.datasource.platform=${SPRING_DATASOURCE_PLATFORM} \
    --spring.datasource.url=${SPRING_DATASOURCE_URL} --spring.datasource.driverClassName=${SPRING_DATASOURCE_DRIVERCLASSNAME} \
    --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
    --spring.h2.console.enabled=${SPRING_H2_CONSOLE_ENABLED} --spring.jpa.database-platform=${SPRING_JPA_DATABASE_PLATFORM} \
    --tenant.default-tenant=${TENANT_DEFAULT_TENANT} --extraction.datasource.url=${EXTRACTION_DATASOURCE_URL} \
    --extraction.datasource.username=${EXTRACTION_DATASOURCE_USERNAME} --extraction.datasource.password=${EXTRACTION_DATASOURCE_PASSWORD} \
    --extraction.datasource.driverClassName=${EXTRACTION_DATASOURCE_DRIVERCLASSNAME} --extraction.datasource.validation-query=${EXTRACTION_DATASOURCE_VALIDATION_QUERY} \
    --extraction.jpa.database-platform=${EXTRACTION_JPA_DATABASE_PLATFORM} --extraction.schema.voyager.tableTypes=${EXTRACTION_SCHEMA_VOYAGER_TABLETYPES} \
    --extraction.schema.voyager.selection=${EXTRACTION_SCHEMA_VOYAGER_SELECTION}