package org.folio.rest.serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class ClobSerializer extends JsonSerializer<Clob> {

  private final static Logger logger = LoggerFactory.getLogger(ClobSerializer.class);

  @Override
  public void serialize(Clob clob, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    try {
      if (clob == null) {
        gen.writeNull();
      } else {
        gen.writeString(IOUtils.toString(clob.getAsciiStream(), StandardCharsets.UTF_8));
      }
    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        e.printStackTrace();
      }
      logger.error(e.getMessage());
    }
  }

}