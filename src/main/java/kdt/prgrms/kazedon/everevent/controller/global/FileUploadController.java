package kdt.prgrms.kazedon.everevent.controller.global;

import kdt.prgrms.kazedon.everevent.service.global.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class FileUploadController {

  private final FileUploadService fileUploadService;

  @PostMapping("/upload")
  public String uploadImage(@RequestPart MultipartFile file) {
    return fileUploadService.uploadImage(file);
  }
}
