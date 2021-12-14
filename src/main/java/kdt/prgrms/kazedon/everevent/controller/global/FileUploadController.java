package kdt.prgrms.kazedon.everevent.controller.global;

import kdt.prgrms.kazedon.everevent.service.global.FileService;
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

  private final FileService fileService;

  @PostMapping("/upload")
  public String uploadImage(@RequestPart MultipartFile file) {
    return fileService.uploadImage(file);
  }
}
