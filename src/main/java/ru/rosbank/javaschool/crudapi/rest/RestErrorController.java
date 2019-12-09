package ru.rosbank.javaschool.crudapi.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import ru.rosbank.javaschool.crudapi.constant.StatusConstants;
import ru.rosbank.javaschool.crudapi.dto.ErrorResponseDto;
import ru.rosbank.javaschool.crudapi.exception.BadRequestException;
import ru.rosbank.javaschool.crudapi.exception.UnsupportedFileTypeException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class RestErrorController extends AbstractErrorController {
  private final ErrorAttributes errorAttributes;
  private final String path;

  public RestErrorController(ErrorAttributes errorAttributes, @Value("${server.error.path:${error.path:/error}}") String path) {
    super(errorAttributes);
    this.errorAttributes = errorAttributes;
    this.path = path;
  }

  @RequestMapping
  public ResponseEntity<ErrorResponseDto> error(HttpServletRequest request) {
    ServletWebRequest webRequest = new ServletWebRequest(request);
    Throwable error = errorAttributes.getError(webRequest);
    int status = getStatus(request).value();
    String message = "error.unknown";
    if (error == null) {
      return ResponseEntity.status(status).body(
          new ErrorResponseDto(status, message)
      );
    }

    if (error instanceof BadRequestException) {
      status = StatusConstants.BAD_REQUEST_EXCEPTION;
      message = "error.bad_request";
      return getErrorResponseDtoResponseEntity(error, status, message);
    }
    if (error instanceof UnsupportedFileTypeException) {
      status = StatusConstants.UNSUPPORTED_FILE_TYPE_EXCEPTION;
      message = "error.bad_filetype";
      return getErrorResponseDtoResponseEntity(error, status, message);
    }
    return getErrorResponseDtoResponseEntity(error, status, message);
  }

  private ResponseEntity<ErrorResponseDto> getErrorResponseDtoResponseEntity(Throwable error, int status, String message) {
    error.printStackTrace();
    return ResponseEntity.status(status).body(
        new ErrorResponseDto(status, message)
    );
  }

  @Override
  public String getErrorPath() {
    return path;
  }

}
