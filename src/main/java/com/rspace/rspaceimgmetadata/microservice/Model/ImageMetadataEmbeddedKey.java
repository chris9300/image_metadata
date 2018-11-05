package com.rspace.rspaceimgmetadata.microservice.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ImageMetadataEmbeddedKey implements Serializable {
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "rspace_image_id")
    private Long rspaceImageId;

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
