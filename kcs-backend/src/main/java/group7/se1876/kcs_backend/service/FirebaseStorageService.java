package group7.se1876.kcs_backend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile file) throws IOException {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream("src/main/resources/serviceAccountKey.json")))
                .build()
                .getService();


        String bucketName = "koicare-d7f6c.appspot.com";
        String objectName = "pond-images/" + file.getOriginalFilename();
        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8.toString());

        System.out.println("Uploading to bucket: " + bucketName);
        System.out.println("File path: " + objectName);

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        // Log the object name and file size
//        log.info("File uploaded to Firebase: {}, size: {}", objectName, file.getSize());
        String imageUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, encodedObjectName);
        System.out.println("Generated image URL: " + imageUrl);

        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, encodedObjectName);
    }
}
