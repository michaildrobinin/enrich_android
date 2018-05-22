package enrich.and.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import enrich.and.com.app.AWSConstants;

public class CardImage implements Serializable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("ht_job_id")
    @Expose
    private int htJobId = 0;
    @SerializedName("side")
    @Expose
    private int side = 0;
    @SerializedName("is_edited")
    @Expose
    private int isEdited = 0;
    @SerializedName("image_type")
    @Expose
    private String imageType = "";
    @SerializedName("bucket_name")
    @Expose
    private String bucketName = "";
    @SerializedName("key_name")
    @Expose
    private String keyName = "";
    @SerializedName("image_url")
    @Expose
    private String imageUrl = "";
    @SerializedName("image_height")
    @Expose
    private int imageHeight = 0;
    @SerializedName("image_width")
    @Expose
    private int imageWidth = 0;
    @SerializedName("modified_at")
    @Expose
    private int modifiedAt;
    @SerializedName("created_at")
    @Expose
    private int createdAt;

    public CardImage(boolean isFrontCard)
    {
        setId(0);
        setHtJobId(0);
        if(isFrontCard)
            setSide(0);
        else
            setSide(1);
        setImageType("jpg");
        setBucketName(AWSConstants.SCANNER_BUCKET_NAME);
    }
    public CardImage(CardImage image)
    {
        setId(image.getId());
        setHtJobId(image.getHtJobId());
        setSide(image.getSide());
        setIsEdited(image.getIsEdited());
        setImageType(image.getImageType());
        setBucketName(image.getBucketName());
        setImageUrl(image.getImageUrl());
        setKeyName(image.getKeyName());
        setImageHeight(image.getImageHeight());
        setImageWidth(image.getImageWidth());
        setModifiedAt(image.getModifiedAt());
        setCreatedAt(image.getCreatedAt());

    }

    public void setInfo(CardImage image)
    {
        setId(image.getId());
        setHtJobId(image.getHtJobId());
        setSide(image.getSide());
        setIsEdited(image.getIsEdited());
        setImageType(image.getImageType());
        setBucketName(image.getBucketName());
        setImageUrl(image.getImageUrl());
        setKeyName(image.getKeyName());
        setImageHeight(image.getImageHeight());
        setImageWidth(image.getImageWidth());
        setModifiedAt(image.getModifiedAt());
        setCreatedAt(image.getCreatedAt());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHtJobId() {
        return htJobId;
    }

    public void setHtJobId(int htJobId) {
        this.htJobId = htJobId;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(int isEdited) {
        this.isEdited = isEdited;
    }

    public String getImageType() {
        return imageType==null?"":imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getBucketName() {
        return bucketName==null?"":bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKeyName() {
        return keyName==null?"":keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getImageUrl() {
        return imageUrl==null?"":imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(int modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

}