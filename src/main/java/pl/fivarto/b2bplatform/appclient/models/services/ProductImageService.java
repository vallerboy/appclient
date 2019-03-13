package pl.fivarto.b2bplatform.appclient.models.services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

@Service
public class ProductImageService {
    public void uploadOrChangeImage(int id, MultipartFile file) throws IOException {
         File image = getImageFile(id);
         createFileIfNotExits(image);

         insertBytesIntoImage(image, file.getBytes());
    }

    private void insertBytesIntoImage(File file, byte[] bytes) throws IOException {
        Files.write(file.toPath(), bytes, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private File getImageFile(int id) {
         return new File("photos/" + id + ".png");
    }

    private void createFileIfNotExits(File file) throws IOException {
        if(!file.exists())
        file.createNewFile();
    }
}
