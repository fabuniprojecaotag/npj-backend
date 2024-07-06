package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.domains.ResponseFile;
import com.uniprojecao.fabrica.gprojuridico.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/storage")
public class FileController {

    @Autowired
    private FileService service;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> upload(@RequestParam("files") MultipartFile[] files,
                                         @RequestParam("directory") String directory) {
        try {
            List<String> filesName = service.upload(files, directory);
            return ResponseEntity.ok("File uploaded successfully: " + filesName);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String fileName,
                                                      @RequestParam("directory") String directory) {
        byte[] data = service.download(fileName, directory);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentLength(data.length)
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<List<ResponseFile>> getAll(@RequestParam("directory") String directory) {
        var files = service.list(directory);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> delete(@PathVariable String fileName,
                                         @RequestParam("directory") String directory) {
        try {
            service.delete(fileName, directory);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete file: " + e.getMessage());
        }
    }
}
