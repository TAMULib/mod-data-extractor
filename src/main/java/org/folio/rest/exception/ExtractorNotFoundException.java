package org.folio.rest.exception;

public class ExtractorNotFoundException extends Exception {

  private static final long serialVersionUID = -3176459322684173792L;

  private final static String WORKFLOW_NOT_FOUND_MESSAGE = "The Extractor, %s, cannot be found.";

  public ExtractorNotFoundException(String id) {
    super(String.format(WORKFLOW_NOT_FOUND_MESSAGE, id));
  }

}