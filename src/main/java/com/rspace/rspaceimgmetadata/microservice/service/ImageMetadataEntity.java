package com.rspace.rspaceimgmetadata.microservice.service;

import javax.persistence.*;

@Entity
@Table(name = "imageMetadata")
public class ImageMetadataEntity {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "db_id", columnDefinition = "integer not null auto_increment")
    @Column(name = "id")
    private Integer imageId;

    @EmbeddedId
    private ImageMetadataEmbeddedKey custRspaceImageVersion;

    @Column(columnDefinition = "JSON", name = "metadata")
    private String jsonMetadata;

    public ImageMetadataEntity() {
    }

    public ImageMetadataEntity(String customerId, Long rspaceImageId, Integer imageVersionId) {
        this.custRspaceImageVersion = new ImageMetadataEmbeddedKey(customerId, rspaceImageId, imageVersionId);
    }

    public ImageMetadataEntity(String customerId, Long rspaceImageId, Integer imageVersionId, String jsonMetadata) {
        this.custRspaceImageVersion = new ImageMetadataEmbeddedKey(customerId, rspaceImageId, imageVersionId);
        this.jsonMetadata = jsonMetadata;
    }


    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Long getRspaceImageId() {
        return custRspaceImageVersion.getRspaceImageId();
    }

    public void setRspaceImageId(Long rspaceImageId) {
        this.custRspaceImageVersion.setRspaceImageId(rspaceImageId);
    }

    public Integer getImageVersion() {
        return custRspaceImageVersion.getImageVersion();
    }

    public void setImageVersion(Integer imageVersion) {
        this.custRspaceImageVersion.setImageVersion(imageVersion);
    }

    public String getCustomerId() {
        return custRspaceImageVersion.getCustomerId();
    }

    public void setCustomerId(String customerId) {
        this.custRspaceImageVersion.setCustomerId(customerId);
    }

    public ImageMetadataEmbeddedKey getCustRspaceImageVersion() {
        return custRspaceImageVersion;
    }

    public void setCustRspaceImageVersion(ImageMetadataEmbeddedKey custRspaceImageVersion) {
        this.custRspaceImageVersion = custRspaceImageVersion;
    }

    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
    }
}

