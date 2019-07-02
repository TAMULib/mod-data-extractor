package org.folio.rest.model.projection;

import org.folio.rest.model.Extractor;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "expanded", types = Extractor.class)
public interface ExtractorProjection {

  public String getName();

}
