package com.rspace.rspaceimgmetadata.microservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "imageMetadata")
public class ImageMetadataEntity {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "db_id", columnDefinition = "integer not null auto_increment")
    @Column(name = "id")
    @JsonIgnore
    private Integer imageId;

    @EmbeddedId
    @JsonIgnore // should ignore because otherwise would be duplicated in the json
    private ImageMetadataEmbeddedKey embeddedKey;

    @Column(name = "user_id")
    @Size(min = 1, max = 255)
    private String userId;

    @Column(columnDefinition = "JSON", name = "metadata")
    private String jsonMetadata;

    public ImageMetadataEntity() {
    }

    public ImageMetadataEntity(ImageMetadataEmbeddedKey embeddedKey) {
        this.embeddedKey = embeddedKey;
    }

    public ImageMetadataEntity(String customerId, Long rspaceImageId, Integer imageVersionId) {
        this.embeddedKey = new ImageMetadataEmbeddedKey(customerId, rspaceImageId, imageVersionId);
    }

    public ImageMetadataEntity(String customerId, Long rspaceImageId, Integer imageVersionId, String jsonMetadata) {
        this.embeddedKey = new ImageMetadataEmbeddedKey(customerId, rspaceImageId, imageVersionId);
        this.jsonMetadata = jsonMetadata;
    }


    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Long getRspaceImageId() {
        return embeddedKey.getRspaceImageId();
    }

    public void setRspaceImageId(Long rspaceImageId) {
        this.embeddedKey.setRspaceImageId(rspaceImageId);
    }

    public Integer getImageVersion() {
        return embeddedKey.getImageVersion();
    }

    public void setImageVersion(Integer imageVersion) {
        this.embeddedKey.setImageVersion(imageVersion);
    }

    public String getCustomerId() {
        return embeddedKey.getCustomerId();
    }

    public void setCustomerId(String customerId) {
        this.embeddedKey.setCustomerId(customerId);
    }

    public ImageMetadataEmbeddedKey getEmbeddedKey() {
        return embeddedKey;
    }

    public void setEmbeddedKey(ImageMetadataEmbeddedKey embeddedKey) {
        this.embeddedKey = embeddedKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
    }
}

