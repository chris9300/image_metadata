package com.rspace.rspaceimgmetadata.microservice.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Embeddable
public class ImageMetadataEmbeddedKey implements Serializable {
    @Column(name = "customer_id")
    @Size(min = 1, max = 255)
    private String customerId;

    @Min(1)
    @Digits(integer = 20, fraction = 0) // allow between 1 and 20 digits
    @Column(name = "rspace_image_id")
    private Long rspaceImageId;

    @Min(1)
    @Digits(integer = 11, fraction = 0) // allow between 1 and 11 digits
    @Column(name = "image_version")
    private Integer imageVersion;

    public ImageMetadataEmbeddedKey() {
    }

    public ImageMetadataEmbeddedKey(String customerId, Long rspaceImageId, Integer imageVersion) {
        this.rspaceImageId = rspaceImageId;
        this.imageVersion = imageVersion;
        this.customerId = customerId;
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
}
