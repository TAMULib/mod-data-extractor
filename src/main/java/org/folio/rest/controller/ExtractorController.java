package org.folio.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.folio.rest.model.Extractor;
import org.folio.rest.resolver.annotation.Extract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RepositoryRestController
public class ExtractorController implements RepresentationModelProcessor<EntityModel<Extractor>> {

  private final static Logger logger = LoggerFactory.getLogger(ExtractorController.class);

  @PostMapping("/extractors/{id}/stream")
  public ResponseEntity<StreamingResponseBody> stream(@PathVariable String id, @Extract(streaming = true) StreamingResponseBody stream) {
    logger.info(String.format("Streaming from extractor %s", id));
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(stream);
  }
  
  @PostMapping("/extractors/{id}/list")
  public ResponseEntity<List<Map<String, Object>>> run(@PathVariable String id, @Extract List<Map<String, Object>> list) {
    logger.info(String.format("Running extractor %s", id));
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(list);
  }

  @Override
  public EntityModel<Extractor> process(EntityModel<Extractor> resource) {
    resource.add(WebMvcLinkBuilder
        .linkTo(WebMvcLinkBuilder
            .methodOn(ExtractorController.class)
            .stream(resource.getContent().getId(), out -> {}))
        .withRel("stream"));
    resource.add(WebMvcLinkBuilder
        .linkTo(WebMvcLinkBuilder
            .methodOn(ExtractorController.class)
            .run(resource.getContent().getId(), new ArrayList<Map<String, Object>>()))
        .withRel("run"));
    return resource;
  }

}