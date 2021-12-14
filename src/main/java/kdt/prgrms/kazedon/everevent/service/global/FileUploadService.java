package kdt.prgrms.kazedon.everevent.service.global;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import kdt.prgrms.kazedon.everevent.exception.ErrorMessage;
import kdt.prgrms.kazedon.everevent.exception.FileUploadException;
import kdt.prgrms.kazedon.everevent.exception.InvalidFileTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

  private static final String DIRECTORY = "static/";

  private final S3UploadService s3UploadService;

  public String uploadImage(MultipartFile file) {
    String fileName = DIRECTORY + createFileName(file.getOriginalFilename());
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());
    objectMetadata.setContentLength(file.getSize());

    try (InputStream inputStream = file.getInputStream()) {
      s3UploadService.uploadFile(fileName, inputStream, objectMetadata);
    } catch (IOException e) {
      throw new FileUploadException(ErrorMessage.FILE_UPLOAD_ERROR);
    }

    return s3UploadService.getFileUrl(fileName);
  }

  private String createFileName(String originalFileName) {
    return UUID
        .randomUUID()
        .toString()
        .concat(getFileExtension(originalFileName));
  }

  private String getFileExtension(String fileName) {
    try {
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e) {
      throw new InvalidFileTypeException(ErrorMessage.INVALID_FILE_TYPE);
    }
  }
}