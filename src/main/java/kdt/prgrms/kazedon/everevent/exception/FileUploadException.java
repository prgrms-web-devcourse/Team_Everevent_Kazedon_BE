package kdt.prgrms.kazedon.everevent.exception;

public class FileUploadException extends RuntimeException{

  public FileUploadException(ErrorMessage errorMessage) {
    super(errorMessage.getMessage());
  }

}
