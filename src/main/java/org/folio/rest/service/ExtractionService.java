package org.folio.rest.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.apache.commons.text.StringSubstitutor;
import org.folio.rest.model.Extractor;
import org.folio.rest.model.ExtractorType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public interface ExtractionService {

  @SuppressWarnings("deprecation")
  default Query<Map<String, Object>> query(Extractor extractor, Map<String, String> context) {
    StringSubstitutor sub = new StringSubstitutor(context);
    String sql = sub.replace(extractor.getQueryTemplate());
    Session session = getEntityManager().unwrap(Session.class);

    @SuppressWarnings("unchecked")
    Query<Map<String, Object>> query = session.createNativeQuery(sql);

    // https://discourse.hibernate.org/t/hibernate-resulttransformer-is-deprecated-what-to-use-instead/232
    query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

    return query;
  }

  default Stream<Map<String, Object>> stream(Extractor extractor, Map<String, String> context) throws SQLException {
    return query(extractor, context).getResultStream();
  }

  default List<Map<String, Object>> list(Extractor extractor, Map<String, String> context) throws SQLException {
    return query(extractor, context).getResultList();
  }

  public EntityManager getEntityManager();

  public ExtractorType getType();

}