package kdt.prgrms.kazedon.everevent.service.global;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public void uploadFile(String fileName,
                         InputStream inputStream,
                         ObjectMetadata objectMetadata) {
    s3Client.putObject(
        new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead)
    );
  }

  public String getFileUrl(String fileName) {
    return s3Client
        .getUrl(bucket, fileName)
        .toString();
  }

}