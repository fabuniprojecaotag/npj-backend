package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    static String bucketName = "gprojuridico.appspot.com";

    public byte[] download(String fileName) {
        var storage = getStorage();
        var blob = storage.get(BlobId.of(bucketName, fileName));
        return blob.getContent();
    }

    public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        var storage = getStorage();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(contentType)
                .setContentLanguage("pt")
                .build();
        storage.create(blobInfo, file.getBytes());
        return fileName;
    }

    static Storage getStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
