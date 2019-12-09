package ru.rosbank.javaschool.crudapi.exception;

public class UnsupportedFileTypeException extends RuntimeException {

  public UnsupportedFileTypeException() {
    super();
  }

  public UnsupportedFileTypeException(String message) {
    super(message);
  }

  public UnsupportedFileTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedFileTypeException(Throwable cause) {
    super(cause);
  }

  protected UnsupportedFileTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
