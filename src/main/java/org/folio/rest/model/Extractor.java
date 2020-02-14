package org.folio.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.folio.spring.domain.model.AbstractBaseEntity;

@Entity
public class Extractor extends AbstractBaseEntity {

  @NotNull
  @Column(nullable = false)
  @Size(min = 5, max = 100)
  private String name;

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ExtractorType type;

  @NotNull
  @Column(columnDefinition = "TEXT", nullable = false)
  private String queryTemplate;

  public Extractor() {
    super();
  }

  public Extractor(String name) {
    this();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ExtractorType getType() {
    return type;
  }

  public void setType(ExtractorType type) {
    this.type = type;
  }

  public String getQueryTemplate() {
    return queryTemplate;
  }

  public void setQueryTemplate(String queryTemplate) {
    this.queryTemplate = queryTemplate;
  }

}
