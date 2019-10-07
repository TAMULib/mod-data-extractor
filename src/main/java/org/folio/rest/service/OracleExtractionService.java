package org.folio.rest.service;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.folio.rest.model.Extractor;
import org.folio.rest.model.ExtractorType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Service;

@Service
public class OracleExtractionService implements ExtractionService {

  @PersistenceContext(unitName = "oracle-extractor-database")
  private EntityManager em;

  @Override
  @SuppressWarnings("deprecation")
  public Stream<Map<String, Object>> run(Extractor extractor) throws SQLException {
    String sql = extractor.getQuery();

    Session session = em.unwrap(Session.class);

    @SuppressWarnings("unchecked")
    Query<Map<String, Object>> query = session.createNativeQuery(sql);

    // https://discourse.hibernate.org/t/hibernate-resulttransformer-is-deprecated-what-to-use-instead/232
    query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

    return query.getResultStream();
  }

  @Override
  public ExtractorType getType() {
    return ExtractorType.ORACLE;
  }

}