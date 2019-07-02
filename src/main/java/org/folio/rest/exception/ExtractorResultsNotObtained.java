package org.folio.rest.exception;

public class ExtractorResultsNotObtained extends Exception {

  private static final long serialVersionUID = 1L;

  public ExtractorResultsNotObtained() {
    super("The Extractor did not produce results.");
  }

}