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

  @Column
  @NotNull
  @Size(min = 5, max = 100)
  private String name;

  @Column
  @NotNull
  @Enumerated(EnumType.STRING)
  private ExtractorType type;

  @Column(length = 15000)
  @Size(min = 0, max = 15000)
  private String query;

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

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

}
