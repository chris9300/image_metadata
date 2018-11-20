package com.rspace.rspaceimgmetadata.microservice.repository;

import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadataEntity, ImageMetadataEmbeddedKey> {
}

