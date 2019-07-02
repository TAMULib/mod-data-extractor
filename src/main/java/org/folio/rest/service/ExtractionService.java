package org.folio.rest.service;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Stream;

import org.folio.rest.model.Extractor;
import org.folio.rest.model.ExtractorType;

public interface ExtractionService {

  public Stream<Map<String, Object>> run(Extractor extractor) throws SQLException;

  public ExtractorType getType();

}