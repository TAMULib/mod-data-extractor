package org.folio.rest.resolver;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExtractorStreamingResponseArgumentResolver implements HandlerMethodArgumentResolver {

  private final static Logger logger = LoggerFactory.getLogger(ExtractorStreamingResponseArgumentResolver.class);

  @Autowired
  private ExtractorRepo extractorRepo;

  @Autowired
  private List<ExtractionService> extractionServices;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return StreamingResponseBody.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

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
      return (StreamingResponseBody) out -> {
        try (Stream<Map<String, Object>> resultStream = extractionService.get().run(extractor.get())) {
          resultStream.forEach(r -> {
            try {
              String row = objectMapper.writeValueAsString(r) + "\n";
              logger.debug("Result: " + row);
              out.write(row.getBytes());
            } catch (Exception e) {
              if (logger.isDebugEnabled()) {
                e.printStackTrace();
              }
              logger.error(e.getMessage());
            }
          });
        } catch (Exception e) {
          if (logger.isDebugEnabled()) {
            e.printStackTrace();
          }
          logger.error(e.getMessage());
        } finally {
          out.close();
        }
      };
    }
    throw new ExtractorServiceNotFoundException(extractorType);
  }

}
