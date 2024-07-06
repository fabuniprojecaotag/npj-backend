package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.uniprojecao.fabrica.gprojuridico.domains.ResponseFile;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    static String bucketName = "gprojuridico.appspot.com";

    public byte[] download(String fileName, String directory) {

        if (directory == null || !directory.startsWith("ATE")) {
            throw new RuntimeException("Directory cannot be null or needs to have ATE prefix");
        }

        var storage = getStorage();
        var blob = storage.get(BlobId.of(bucketName, directory + "/" + fileName));
        return blob.getContent();
    }

    public List<String> upload(MultipartFile[] files, String directory) throws IOException {
        List<String> filesName = new ArrayList<>();

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

            filesName.add(fileName);
        }

        return filesName;
    }

    private static String checkDirectory(String directory) {
        if (directory == null || !directory.startsWith("ATE")) {
            var repository = new AtendimentoRepository();
            var doc = repository.findLast();
            directory = doc.getId();
        }
        return directory;
    }

    public List<ResponseFile> list(String directory) {
        Page<Blob> blobs = getStorage().list(bucketName, Storage.BlobListOption.prefix(directory));
        List<ResponseFile> files = new ArrayList<>();

        for (Blob blob : blobs.iterateAll()) {
            var file = new ResponseFile(blob.getName(), blob.getContentType(), blob.getSize());
            files.add(file);
        }

        return files;
    }

    public void delete(String fileName, String directory) {
        if (directory == null || !directory.startsWith("ATE")) {
            throw new RuntimeException("Directory cannot be null or needs to have ATE prefix");
        }

        var storage = getStorage();
        storage.delete(BlobId.of(bucketName, directory + "/" + fileName));
    }

    static Storage getStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
