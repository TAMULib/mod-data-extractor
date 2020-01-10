package org.folio.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "org.folio.rest", "org.folio.spring" })
public class ModDataExtractor extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ModDataExtractor.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(ModDataExtractor.class, args);
  }

}
