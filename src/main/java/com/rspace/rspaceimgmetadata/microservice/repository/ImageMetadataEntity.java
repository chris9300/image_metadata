package com.rspace.rspaceimgmetadata.microservice.repository;

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

    @Column(name = "image_version_id")
    private Integer imageVersionId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(columnDefinition = "JSON", name = "metadata")
    private String jsonMetadata;

    public ImageMetadataEntity() {
    }

    public ImageMetadataEntity(Long rspaceImageId, String customerId) {
        this.rspaceImageId = rspaceImageId;
        this.customerId = customerId;
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

    public Integer getImageVersionId() {
        return imageVersionId;
    }

    public void setImageVersionId(Integer imageVersionId) {
        this.imageVersionId = imageVersionId;
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
