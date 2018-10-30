package com.rspace.rspaceimgmetadata.microservice.repository;

import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadataEntity, ImageMetadataEmbeddedKey> {
}

