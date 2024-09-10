package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.uniprojecao.fabrica.gprojuridico.models.ResponseFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    private final static String bucketName = "gprojuridico-dev.appspot.com";

    public byte[] download(String fileName, String directory) {
        directory = checkDirectory(directory);

        var storage = getStorage();
        var blob = storage.get(BlobId.of(bucketName, directory + "/" + fileName));
        return blob.getContent();
    }

    public List<String> upload(MultipartFile[] files, String directory) throws Exception {
        List<String> uploadedFilesNameToDisplay = new ArrayList<>();

        directory = checkDirectory(directory);

        for (MultipartFile file : files) {
            var storage = getStorage();
            String fileName = directory + "/" + file.getOriginalFilename();
            String contentType = file.getContentType();
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType(contentType)
                    .setContentLanguage("pt")
                    .build();
            storage.create(blobInfo, file.getBytes());

            uploadedFilesNameToDisplay.add(fileName);
        }

        return uploadedFilesNameToDisplay;
    }

    private String checkDirectory(String directory) {
        // verifica se o directory é válido
        if (directory == null || !directory.startsWith("ATE")) {
            throw new RuntimeException("Directory cannot be null or needs to have ATE prefix");
        }

        // caso o directory passado foi via form-data, este virá com vírgula, a qual deve ser retirado.
        return directory.split(",")[0];
    }

    public Map<String, Object> list(String directory) {
        Page<Blob> blobs = getStorage().list(bucketName, Storage.BlobListOption.prefix(directory));
        List<ResponseFile> files = new ArrayList<>();

        for (Blob blob : blobs.iterateAll()) {
            var file = new ResponseFile(blob.getName(), blob.getContentType(), blob.getSize());
            var isFile = file.getSize() != 0 && !file.getName().endsWith("/");
            if (isFile) files.add(file);
        }

        var lastFile = files.get(files.size() - 1);
        var pageSize = files.size();

        return Map.of(
                "list", files,
                "lastFile", lastFile,
                "pageSize", pageSize
        );
    }

    public void delete(String fileName, String directory) {
        directory = checkDirectory(directory);

        var storage = getStorage();
        storage.delete(BlobId.of(bucketName, directory + "/" + fileName));
    }

    private static Storage getStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
