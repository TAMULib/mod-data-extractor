package org.folio.rest.exception;

import org.folio.rest.model.ExtractorType;

public class ExtractorServiceNotFoundException extends Exception {

  private static final long serialVersionUID = 5454269308906842195L;

  private final static String WORKFLOW_NOT_FOUND_MESSAGE = "The Extractor Service, of type %s, cannot be found.";

  public ExtractorServiceNotFoundException(ExtractorType type) {
    super(String.format(WORKFLOW_NOT_FOUND_MESSAGE, type));
  }

}