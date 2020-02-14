package org.folio.rest.service;

import javax.persistence.EntityManager;

import org.folio.rest.model.ExtractorType;

public interface ExtractionService {

  public EntityManager getEntityManager();

  public ExtractorType getType();

}