package group7.se1876.kcs_backend.service;

import org.springframework.beans.factory.annotation.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class FirebaseStorageService {

    @Value("${path.serviceAccountKey}")
    private String serviceAccountKeyPath;
    public String uploadFile(MultipartFile file,String fileName) throws IOException {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath)))
                .build()
                .getService();


        String bucketName = "koicare-d7f6c.appspot.com";
        String objectName = fileName + file.getOriginalFilename();
        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8.toString());

        System.out.println("Uploading to bucket: " + bucketName);
        System.out.println("File path: " + objectName);

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        String imageUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, encodedObjectName);
        System.out.println("Generated image URL: " + imageUrl);

        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, encodedObjectName);
    }

    public void deleteFile(String imageUrl) throws IOException {
        // Check if the imageUrl is not null or empty
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.out.println("No file to delete.");
            return;
        }

        // Extract the object name from the full Firebase Storage URL
        String bucketName = "koicare-d7f6c.appspot.com";
        String objectName = imageUrl.substring(imageUrl.indexOf("/o/") + 3, imageUrl.indexOf("?alt="));

        // Decode the object name since Firebase encodes special characters like '/'
        objectName = URLDecoder.decode(objectName, StandardCharsets.UTF_8.toString());

        // Initialize Firebase Storage with credentials
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath)))
                .build()
                .getService();

        // Create a BlobId for the object to delete
        BlobId blobId = BlobId.of(bucketName, objectName);

        // Delete the file
        boolean deleted = storage.delete(blobId);

        if (deleted) {
            System.out.println("File deleted successfully: " + objectName);
        } else {
            throw new IOException("Failed to delete file: " + objectName);
        }
    }


}
