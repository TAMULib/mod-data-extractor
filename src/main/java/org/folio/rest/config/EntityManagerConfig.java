package org.folio.rest.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

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
    em.setJpaProperties(properties);
    em.afterPropertiesSet();
    return em;
  }

  private DataSource extractionDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(EXTRACTION_DRIVERCLASSNAME);
    dataSource.setUrl(EXTRACTION_URL);
    dataSource.setUsername(EXTRACTION_USERNAME);
    dataSource.setPassword(EXTRACTION_PASSWORD);
    return dataSource;
  }

}