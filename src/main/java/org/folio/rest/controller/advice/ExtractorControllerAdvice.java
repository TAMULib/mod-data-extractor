package org.folio.rest.controller.advice;

import org.folio.rest.exception.ExtractorNotFoundException;
import org.folio.rest.exception.ExtractorResultsNotObtained;
import org.folio.rest.exception.ExtractorServiceNotFoundException;
import org.folio.rest.model.response.Errors;
import org.folio.rest.utility.ErrorUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExtractorControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(ExtractorControllerAdvice.class);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ExtractorNotFoundException.class)
  public Errors handleWorkflowNotFoundException(ExtractorNotFoundException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ExtractorServiceNotFoundException.class)
  public Errors handleWorkflowNotFoundException(ExtractorServiceNotFoundException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ExtractorResultsNotObtained.class)
  public Errors handleWorkflowNotFoundException(ExtractorResultsNotObtained exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.NOT_FOUND);
  }

}
