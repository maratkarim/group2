package datalake.minio.serviceMinIO;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioSerivces {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public String upload(MultipartFile file){
        try{
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );return "successfully";
        }catch (Exception e){
            e.printStackTrace();
        }return "errors";
    }
    public ByteArrayResource download(String fileName){
        try{
            GetObjectArgs getObjectArgs = GetObjectArgs
                    .builder()
                    .bucket(bucket) // fbucket
                    .object(fileName) // miniopicture.png
                    .build();
            InputStream stream = minioClient.getObject(getObjectArgs);
            byte[] bytes = IOUtils.toByteArray(stream);
            stream.close();
            return new ByteArrayResource(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
