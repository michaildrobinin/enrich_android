package enrich.and.com.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enrich.and.com.app.MainApplication;

public class ContactModel implements Serializable{

    @SerializedName("id")
    @Expose
    private int id = 0;
    @SerializedName("client_id")
    @Expose
    private String clientId = "0";
    @SerializedName("profile_id")
    @Expose
    private String profileId = "1";
    @SerializedName("is_initialized")
    @Expose
    private int isInitialized = 0;
    @SerializedName("error_message")
    @Expose
    private String errorMessage = "";
    @SerializedName("transcriber_user_id")
    @Expose
    private int transcriberUserId = 0;
    @SerializedName("checker_user_id")
    @Expose
    private int checkerUserId = 0;
    @SerializedName("uploaded_at")
    @Expose
    private String uploadedAt = "";
    @SerializedName("created_at")
    @Expose
    private String createdAt = "";
    @SerializedName("modified_at")
    @Expose
    private String modifiedAt = "";
    @SerializedName("transcribed_at")
    @Expose
    private String transcribedAt = "";
    @SerializedName("checked_at")
    @Expose
    private String checkedAt = "";
    @SerializedName("uploader_email")
    @Expose
    private String uploaderEmail = "";
    @SerializedName("uploader_lname")
    @Expose
    private String uploaderLname = "";
    @SerializedName("uploader_fname")
    @Expose
    private String uploaderFname = "";
    @SerializedName("fname")
    @Expose
    private String fname = "";
    @SerializedName("mname")
    @Expose
    private String mname = "";
    @SerializedName("lname")
    @Expose
    private String lname = "";
    @SerializedName("suffix")
    @Expose
    private String suffix = "";
    @SerializedName("title")
    @Expose
    private String title = "";
    @SerializedName("address")
    @Expose
    private String address = "";
    @SerializedName("city")
    @Expose
    private String city = "";
    @SerializedName("state")
    @Expose
    private String state = "";
    @SerializedName("zip")
    @Expose
    private String zip = "";
    @SerializedName("country")
    @Expose
    private String country = "";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("website")
    @Expose
    private String website = "";
    @SerializedName("company")
    @Expose
    private String company = "";
    @SerializedName("work_phone")
    @Expose
    private String workPhone = "";
    @SerializedName("work_phone_extension")
    @Expose
    private String workPhoneExtension = "";
    @SerializedName("cell_phone")
    @Expose
    private String cellPhone = "";
    @SerializedName("home_phone")
    @Expose
    private String homePhone = "";
    @SerializedName("other_phone")
    @Expose
    private String otherPhone = "";
    @SerializedName("fax")
    @Expose
    private String fax = "";
    @SerializedName("skype")
    @Expose
    private String skype = "";
    @SerializedName("twitter")
    @Expose
    private String twitter = "";
    @SerializedName("viewing_user_id")
    @Expose
    private String viewingUserId = "";
    @SerializedName("viewing_user_at")
    @Expose
    private String viewingUserAt = "";
    @SerializedName("notes")
    @Expose
    private String notes = "";
    @SerializedName("tags")
    @Expose
    private String tags = "";
    @SerializedName("images")
    @Expose
    private List<CardImage> images = new ArrayList<CardImage>();

    public ContactModel()
    {
        setId(0);
        setClientId(String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId()));
        setProfileId(String.valueOf(MainApplication.getInstance().getCurrentProfileIndex()+1));
        setCheckerUserId(1);
        setTranscriberUserId(1);
        images = new ArrayList<CardImage>();
    }

    public ContactModel(ContactModel model)
    {
        setId(model.getId());
        setClientId(model.getClientId());
        setProfileId(model.getProfileId());
        setIsInitialized(model.getIsInitialized());
        setErrorMessage(model.getErrorMessage());
        setTranscriberUserId(model.getTranscriberUserId());
        setCheckerUserId(model.getCheckerUserId());
        setUploadedAt(model.getUploadedAt());
        setCreatedAt(model.getCreatedAt());
        setModifiedAt(model.getModifiedAt());
        setTranscribedAt(model.getTranscribedAt());
        setCheckedAt(model.getCheckedAt());
        setUploaderEmail(model.getUploaderEmail());
        setUploaderLname(model.getUploaderLname());
        setUploaderFname(model.getUploaderFname());
        setFname(model.getFname());
        setMname(model.getMname());
        setLname(model.getLname());
        setSuffix(model.getSuffix());
        setTitle(model.getTitle());
        setAddress(model.getAddress());
        setCity(model.getCity());
        setState(model.getState());
        setZip(model.getZip());
        setCountry(model.getCountry());
        setEmail(model.getEmail());
        setWebsite(model.getWebsite());
        setCompany(model.getCompany());
        setWorkPhone(model.getWorkPhone());
        setWorkPhoneExtension(model.getWorkPhoneExtension());
        setCellPhone(model.getCellPhone());
        setHomePhone(model.getHomePhone());
        setOtherPhone(model.getOtherPhone());
        setFax(model.getFax());
        setSkype(model.getSkype());
        setTwitter(model.getTwitter());
        setViewingUserId(model.getViewingUserId());
        setViewingUserAt(model.getViewingUserAt());
        setNotes(model.getNotes());
        setTags(model.getTags());
        List<CardImage> images = new ArrayList<>();
        for(CardImage image:model.images)
        {
            images.add(new CardImage(image));
        }
        setImages(images);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId==null?"":clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProfileId() {
        return profileId==null?"":profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getIsInitialized() {
        return isInitialized;
    }

    public void setIsInitialized(int isInitialized) {
        this.isInitialized = isInitialized;
    }

    public String getErrorMessage() {
        return errorMessage==null?"":errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getTranscriberUserId() {
        return transcriberUserId;
    }

    public void setTranscriberUserId(int transcriberUserId) {
        this.transcriberUserId = transcriberUserId;
    }

    public int getCheckerUserId() {
        return checkerUserId;
    }

    public void setCheckerUserId(int checkerUserId) {
        this.checkerUserId = checkerUserId;
    }

    public String getUploadedAt() {
        return uploadedAt==null?"":uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getCreatedAt() {
        return createdAt==null?"":createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt==null?"":modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getTranscribedAt() {
        return transcribedAt==null?"":transcribedAt;
    }

    public void setTranscribedAt(String transcribedAt) {
        this.transcribedAt = transcribedAt;
    }

    public String getCheckedAt() {
        return checkedAt==null?"":checkedAt;
    }

    public void setCheckedAt(String checkedAt) {
        this.checkedAt = checkedAt;
    }

    public String getUploaderEmail() {
        return uploaderEmail==null?"":uploaderEmail;
    }

    public void setUploaderEmail(String uploaderEmail) {
        this.uploaderEmail = uploaderEmail;
    }

    public String getUploaderLname() {
        return uploaderLname==null?"":uploaderLname;
    }

    public void setUploaderLname(String uploaderLname) {
        this.uploaderLname = uploaderLname;
    }

    public String getUploaderFname() {
        return uploaderFname==null?"":uploaderFname;
    }

    public void setUploaderFname(String uploaderFname) {
        this.uploaderFname = uploaderFname;
    }

    public String getFname() {
        return fname==null?"":fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname==null?"":mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname==null?"":lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSuffix() {
        return suffix==null?"":suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTitle() {
        return title==null?"":title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address==null?"":address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city==null?"":city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state==null?"":state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip==null?"":zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country==null?"":country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email==null?"":email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website==null?"":website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompany() {
        return company==null?"":company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWorkPhone() {
        return workPhone==null?"":workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getWorkPhoneExtension() {
        return workPhoneExtension==null?"":workPhoneExtension;
    }

    public void setWorkPhoneExtension(String workPhoneExtension) {
        this.workPhoneExtension = workPhoneExtension;
    }

    public String getCellPhone() {
        return cellPhone==null?"":cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone() {
        return homePhone==null?"":homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getOtherPhone() {
        return otherPhone==null?"":otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getFax() {
        return fax==null?"":fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSkype() {
        return skype==null?"":skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getTwitter() {
        return twitter==null?"":twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getViewingUserId() {
        return viewingUserId==null?"":viewingUserId;
    }

    public void setViewingUserId(String viewingUserId) {
        this.viewingUserId = viewingUserId;
    }

    public String getViewingUserAt() {
        return viewingUserAt==null?"":viewingUserAt;
    }

    public void setViewingUserAt(String viewingUserAt) {
        this.viewingUserAt = viewingUserAt;
    }

    public String getNotes() {
        return notes==null?"":notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTags() {
        return tags==null?"":tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<CardImage> getImages() {
        if(images == null)
            images = new ArrayList<>();
        return images;
    }

    public void setImages(List<CardImage> images) {
        this.images = images;
    }
}
