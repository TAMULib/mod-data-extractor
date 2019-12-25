package org.folio.rest.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.folio.rest.type.StringArrayUserType;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.TypeContributorList;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
    properties.setProperty("hibernate.show_sql", "true");

    properties.put(EntityManagerFactoryBuilderImpl.TYPE_CONTRIBUTORS, new TypeContributorList() {
      @Override
      public List<TypeContributor> getTypeContributors() {
        List<TypeContributor> typeContributors = new ArrayList<TypeContributor>();
        typeContributors.add(new TypeContributor() {
          @Override
          @SuppressWarnings("deprecation")
          public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
            // https://docs.jboss.org/hibernate/orm/5.4/javadocs/org/hibernate/boot/model/TypeContributions.html#contributeType-org.hibernate.type.BasicType-java.lang.String...-
            typeContributions.contributeType(StringArrayUserType.INSTANCE, "odcivarchar2list");
          }
        });
        return typeContributors;
      }
    });

    em.setJpaProperties(properties);
    em.afterPropertiesSet();
    return em;
  }

  private DataSource extractionDataSource() {
    OracleDataSource ods = DataSourceBuilder.create()
      .driverClassName(EXTRACTION_DRIVERCLASSNAME)
      .url(EXTRACTION_URL)
      .username(EXTRACTION_USERNAME)
      .password(EXTRACTION_PASSWORD)
      .type(OracleDataSource.class)
      .build();
    HikariConfig hkConfig = new HikariConfig();
    hkConfig.setDataSource(ods);
    hkConfig.setDataSourceClassName("oracle.jdbc.pool.OracleDataSource");
    hkConfig.setPoolName("oracle-hikara-1");
    hkConfig.setMaximumPoolSize(15);
    hkConfig.setMinimumIdle(3);
    hkConfig.setConnectionTimeout(172800000);
    hkConfig.setIdleTimeout(3600000);
    hkConfig.setMaxLifetime(172800000);
    return new HikariDataSource(hkConfig);
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
    properties.setProperty("hibernate.show_sql", "true");

    properties.put(EntityManagerFactoryBuilderImpl.TYPE_CONTRIBUTORS, new TypeContributorList() {
      @Override
      public List<TypeContributor> getTypeContributors() {
        List<TypeContributor> typeContributors = new ArrayList<TypeContributor>();
        typeContributors.add(new TypeContributor() {
          @Override
          @SuppressWarnings("deprecation")
          public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
            // https://docs.jboss.org/hibernate/orm/5.4/javadocs/org/hibernate/boot/model/TypeContributions.html#contributeType-org.hibernate.type.BasicType-java.lang.String...-
            typeContributions.contributeType(StringArrayUserType.INSTANCE, "odcivarchar2list");
          }
        });
        return typeContributors;
      }
    });

    em.setJpaProperties(properties);
    em.afterPropertiesSet();
    return em;
  }

  private DataSource oracleExtractionDataSource() {
    OracleDataSource ods = DataSourceBuilder.create()
      .driverClassName(ORACLE_EXTRACTION_DRIVERCLASSNAME)
      .url(ORACLE_EXTRACTION_URL)
      .username(ORACLE_EXTRACTION_USERNAME)
      .password(ORACLE_EXTRACTION_PASSWORD)
      .type(OracleDataSource.class)
      .build();
    HikariConfig hkConfig = new HikariConfig();
    hkConfig.setDataSource(ods);
    hkConfig.setDataSourceClassName("oracle.jdbc.pool.OracleDataSource");
    hkConfig.setPoolName("oracle-hikara-2");
    hkConfig.setMaximumPoolSize(15);
    hkConfig.setMinimumIdle(3);
    hkConfig.setConnectionTimeout(172800000);
    hkConfig.setIdleTimeout(3600000);
    hkConfig.setMaxLifetime(172800000);
    return new HikariDataSource(hkConfig);
  }

}