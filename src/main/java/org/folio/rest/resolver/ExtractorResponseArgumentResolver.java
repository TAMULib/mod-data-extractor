package org.folio.rest.resolver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.folio.rest.exception.ExtractorNotFoundException;
import org.folio.rest.exception.ExtractorServiceNotFoundException;
import org.folio.rest.model.Extractor;
import org.folio.rest.model.ExtractorType;
import org.folio.rest.model.repo.ExtractorRepo;
import org.folio.rest.resolver.annotation.Extract;
import org.folio.rest.service.ExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExtractorResponseArgumentResolver implements HandlerMethodArgumentResolver {

  private final static Logger logger = LoggerFactory.getLogger(ExtractorResponseArgumentResolver.class);

  @Autowired
  private ExtractorRepo extractorRepo;

  @Autowired
  private List<ExtractionService> extractionServices;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(Extract.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    final Map<String, String> context = getContext(request);

    @SuppressWarnings("unchecked")
    Map<String, String> pathVariables = (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    String extratorId = pathVariables.get("id");

    Optional<Extractor> extractor = extractorRepo.findById(extratorId);

    if (!extractor.isPresent()) {
      throw new ExtractorNotFoundException(extratorId);
    }

    ExtractorType extractorType = extractor.get().getType();

    Optional<ExtractionService> extractionService = extractionServices.stream()
        .filter(es -> es.getType().equals(extractorType)).findAny();

    if (extractionService.isPresent()) {
      Extract extract = parameter.getParameterAnnotation(Extract.class);
      if (extract.streaming()) {
        return (StreamingResponseBody) out -> {
          try (Stream<Map<String, Object>> resultStream = extractionService.get().stream(extractor.get(), context)) {
            resultStream.forEach(r -> {
              try {
                String row = objectMapper.writeValueAsString(r) + "\n";
                if (logger.isDebugEnabled()) {
                  logger.debug("Result: " + row);
                }
                out.write(row.getBytes());
              } catch (IOException e) {
                if (logger.isDebugEnabled()) {
                  e.printStackTrace();
                }
                logger.error(e.getMessage());
              }
            });
          } catch (SQLException e) {
            if (logger.isDebugEnabled()) {
              e.printStackTrace();
            }
            logger.error(e.getMessage());
          } finally {
            out.close();
            logger.info("Finished streaming {}", extractor.get().getName());
          }
        };
      } else {
        return extractionService.get().list(extractor.get(), context);
      }
    }
    throw new ExtractorServiceNotFoundException(extractorType);
  }

  private Map<String, String> getContext(HttpServletRequest request) {
    Map<String, String> context = new HashMap<String, String>();

    try {
      // @formatter:off
      context = objectMapper.readValue(request.getInputStream(),
          new TypeReference<Map<String, String>>() {});
      // @formatter:on
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return context;
  }

}
