package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.uniprojecao.fabrica.gprojuridico.domains.ResponseFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    static String bucketName = "gprojuridico.appspot.com";

    public byte[] download(String fileName) {
        var storage = getStorage();
        var blob = storage.get(BlobId.of(bucketName, fileName));
        return blob.getContent();
    }

    public List<String> upload(MultipartFile[] files) throws IOException {
        List<String> filesName = new ArrayList<>();

        for (MultipartFile file : files) {
            var storage = getStorage();
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType(contentType)
                    .setContentLanguage("pt")
                    .build();
            storage.create(blobInfo, file.getBytes());

            filesName.add(fileName);
        }

        return filesName;
    }

    public void list() {
        Page<Blob> blobs = getStorage().list(bucketName);

        for (Blob blob : blobs.iterateAll()) {
            var file = new ResponseFile(blob.getName(), blob.getContentType(), blob.getSize());
            System.out.println(file);
        }
    }

    static Storage getStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
