package org.folio.rest.controller;

import org.folio.rest.model.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RepositoryRestController
public class ExtractorController implements ResourceProcessor<Resource<Extractor>> {

  private final static Logger logger = LoggerFactory.getLogger(ExtractorController.class);

  @GetMapping("extractors/{id}/run")
  public ResponseEntity<StreamingResponseBody> runExtractor(@PathVariable String id, StreamingResponseBody stream) {
    logger.info(String.format("Running extractor %s", id));
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(stream);
  }

  @Override
  public Resource<Extractor> process(Resource<Extractor> resource) {
    resource.add(ControllerLinkBuilder
        .linkTo(
            ControllerLinkBuilder.methodOn(ExtractorController.class).runExtractor(resource.getContent().getId(), null))
        .withRel("run"));
    return resource;
  }

}