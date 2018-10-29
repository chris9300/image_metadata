package com.rspace.rspaceimgmetadata.microservice.service;

import javax.persistence.*;

@Entity
@Table(name = "imageMetadata", catalog = "rspace_metadata")
public class ImageMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer imageId;

    @Column(name = "rspace_image_id")
    private Long rspaceImageId;

    @Column(name = "image_version")
    private Integer imageVersion;

    @Column(name = "customer_id")
    private String customerId;

    @Column(columnDefinition = "JSON", name = "metadata")
    private String jsonMetadata;

    public ImageMetadataEntity() {
    }

    public ImageMetadataEntity(String customerId, Long rspaceImageId, Integer imageVersionId) {
        this.rspaceImageId = rspaceImageId;
        this.imageVersion = imageVersionId;
        this.customerId = customerId;
    }

    public ImageMetadataEntity(String customerId, Long rspaceImageId, Integer imageVersionId, String jsonMetadata) {
        this.rspaceImageId = rspaceImageId;
        this.imageVersion = imageVersionId;
        this.customerId = customerId;
        this.jsonMetadata = jsonMetadata;
    }


    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Long getRspaceImageId() {
        return rspaceImageId;
    }

    public void setRspaceImageId(Long rspaceImageId) {
        this.rspaceImageId = rspaceImageId;
    }

    public Integer getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(Integer imageVersion) {
        this.imageVersion = imageVersion;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
    }
}

