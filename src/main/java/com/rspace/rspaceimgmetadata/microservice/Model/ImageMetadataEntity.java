package com.rspace.rspaceimgmetadata.microservice.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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
    private ImageMetadataEmbeddedKey custRspaceImageVersion;

    @Column(name = "user_id")
    private String userId;

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

