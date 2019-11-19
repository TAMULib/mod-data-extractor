package org.folio.rest.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import oracle.jdbc.pool.OracleDataSource;

@Configuration
public class EntityManagerConfig {

  @Value("${extraction.datasource.url}")
  private String EXTRACTION_URL;

  @Value("${extraction.datasource.driverClassName}")
  private String EXTRACTION_DRIVERCLASSNAME;

  @Value("${extraction.jpa.database-platform}")
  private String EXTRACTION_DATABASE_PLATFORM;

  @Value("${extraction.datasource.username}")
  private String EXTRACTION_USERNAME;

  @Value("${extraction.datasource.password}")
  private String EXTRACTION_PASSWORD;

  @Value("${extraction.datasource.validation-query}")
  private String EXTRACTION_VALIDATION_QUERY;

  @Value("${extraction.jpa.hibernate.ddl-auto}")
  private String EXTRACTION_HIBERNATE_DDLAUTO;

  @Value("${extraction2.datasource.url}")
  private String ORACLE_EXTRACTION_URL;

  @Value("${extraction2.datasource.driverClassName}")
  private String ORACLE_EXTRACTION_DRIVERCLASSNAME;

  @Value("${extraction2.jpa.database-platform}")
  private String ORACLE_EXTRACTION_DATABASE_PLATFORM;

  @Value("${extraction2.datasource.username}")
  private String ORACLE_EXTRACTION_USERNAME;

  @Value("${extraction2.datasource.password}")
  private String ORACLE_EXTRACTION_PASSWORD;

  @Value("${extraction2.datasource.validation-query}")
  private String ORACLE_EXTRACTION_VALIDATION_QUERY;

  @Value("${extraction2.jpa.hibernate.ddl-auto}")
  private String ORACLE_EXTRACTION_HIBERNATE_DDLAUTO;

  @Bean
  public LocalContainerEntityManagerFactoryBean extractionEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(extractionDataSource());
    em.setPackagesToScan(new String[] {});
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setPersistenceUnitName("extractor-database");
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", EXTRACTION_HIBERNATE_DDLAUTO);
    properties.setProperty("hibernate.dialect", EXTRACTION_DATABASE_PLATFORM);
    properties.setProperty("hibernate.jdbc.batch_size", "0");
    em.setJpaProperties(properties);
    em.afterPropertiesSet();
    return em;
  }

  private DataSource extractionDataSource() {
    OracleDataSource ds = DataSourceBuilder.create()
      .driverClassName(EXTRACTION_DRIVERCLASSNAME)
      .url(EXTRACTION_URL)
      .username(EXTRACTION_USERNAME)
      .password(EXTRACTION_PASSWORD)
      .type(OracleDataSource.class)
      .build();
    return ds;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean oracleExtractionEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(oracleExtractionDataSource());
    em.setPackagesToScan(new String[] {});
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setPersistenceUnitName("oracle-extractor-database");
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", ORACLE_EXTRACTION_HIBERNATE_DDLAUTO);
    properties.setProperty("hibernate.dialect", ORACLE_EXTRACTION_DATABASE_PLATFORM);
    properties.setProperty("hibernate.jdbc.batch_size", "0");
    em.setJpaProperties(properties);
    em.afterPropertiesSet();
    return em;
  }

  private DataSource oracleExtractionDataSource() {
    OracleDataSource ds = DataSourceBuilder.create()
      .driverClassName(ORACLE_EXTRACTION_DRIVERCLASSNAME)
      .url(ORACLE_EXTRACTION_URL)
      .username(ORACLE_EXTRACTION_USERNAME)
      .password(ORACLE_EXTRACTION_PASSWORD)
      .type(OracleDataSource.class)
      .build();
    return ds;
  }

}