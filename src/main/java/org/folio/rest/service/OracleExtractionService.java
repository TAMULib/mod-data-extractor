package org.folio.rest.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.folio.rest.model.ExtractorType;
import org.springframework.stereotype.Service;

@Service
public class OracleExtractionService implements ExtractionService {

  @PersistenceContext(unitName = "oracle-extractor-database")
  private EntityManager em;

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

  @Override
  public ExtractorType getType() {
    return ExtractorType.ORACLE;
  }

}