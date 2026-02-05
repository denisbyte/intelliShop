package com.denis.afoh.intelliShop.services.impl;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.denis.afoh.intelliShop.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadImgServiceImpl implements StorageService {
    private final BlobServiceClient blobServiceClient;
    @Value("${azure.storage.container-name}")
    private String containerName;
    @Value("${azure.storage.public-url}")
    private String publicUrl;
    @Override
    public String upload(MultipartFile file) {
        if(file  == null || file.isEmpty()){
            throw new RuntimeException("Image obligatoire");
        }
        String ext = Objects.requireNonNull(file.getOriginalFilename()).substring(
                file.getOriginalFilename().lastIndexOf(".")

        );
        String blobName = UUID.randomUUID() + ext;
        BlobContainerClient container = blobServiceClient.getBlobContainerClient(containerName);
        container.createIfNotExists();

        BlobClient blobClient = container.getBlobClient(blobName);

        try (InputStream is = file.getInputStream()) {

            blobClient.upload(is, file.getSize(), true);
        }

        catch (IOException e) {
            throw new RuntimeException(" Upload Azure échoué");
        }

        return  publicUrl + "/" + containerName + "/" + blobName;
    }
}
