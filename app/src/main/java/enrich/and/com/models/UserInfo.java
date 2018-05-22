package enrich.and.com.models;


import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserInfo implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_affiliate")
    @Expose
    private String isAffiliate;
    @SerializedName("last_login_date")
    @Expose
    private String lastLoginDate;
    @SerializedName("activation_key")
    @Expose
    private String activationKey;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("login_count")
    @Expose
    private int loginCount;
    @SerializedName("ipaddress")
    @Expose
    private String ipaddress;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("signup_date")
    @Expose
    private String signupDate;
    @SerializedName("paypal_email")
    @Expose
    private String paypalEmail;
    @SerializedName("last_update_date")
    @Expose
    private String lastUpdateDate;
    @SerializedName("aff_nick")
    @Expose
    private String affNick;
    @SerializedName("opted_out")
    @Expose
    private String optedOut;
    @SerializedName("self_service_status")
    @Expose
    private String selfServiceStatus;
    @SerializedName("credits_available")
    @Expose
    private int creditsAvailable;
    @SerializedName("exclude_iplogging")
    @Expose
    private String excludeIplogging;
    private final static long serialVersionUID = 2326427430815069605L;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName==null?"":firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName==null?"":lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName==null?"":userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email==null?"":email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1==null?"":address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2==null?"":address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public String getPhone() {
        return phone==null?"":phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax==null?"":fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCompany() {
        return company==null?"":company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title==null?"":title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsAffiliate() {
        return isAffiliate==null?"":isAffiliate;
    }

    public void setIsAffiliate(String isAffiliate) {
        this.isAffiliate = isAffiliate;
    }

    public String getLastLoginDate() {
        return lastLoginDate==null?"":lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getActivationKey() {
        return activationKey==null?"":activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getStatus() {
        return status==null?"":status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getIpaddress() {
        return ipaddress==null?"":ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getAccountType() {
        return accountType==null?"":accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSignupDate() {
        return signupDate==null?"":signupDate;
    }

    public void setSignupDate(String signupDate) {
        this.signupDate = signupDate;
    }

    public String getPaypalEmail() {
        return paypalEmail==null?"":paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate==null?"":lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getAffNick() {
        return affNick==null?"":affNick;
    }

    public void setAffNick(String affNick) {
        this.affNick = affNick;
    }

    public String getOptedOut() {
        return optedOut==null?"":optedOut;
    }

    public void setOptedOut(String optedOut) {
        this.optedOut = optedOut;
    }

    public String getSelfServiceStatus() {
        return selfServiceStatus==null?"":selfServiceStatus;
    }

    public void setSelfServiceStatus(String selfServiceStatus) {
        this.selfServiceStatus = selfServiceStatus;
    }

    public int getCreditsAvailable() {
        return creditsAvailable;
    }

    public void setCreditsAvailable(int creditsAvailable) {
        this.creditsAvailable = creditsAvailable;
    }

    public String getExcludeIplogging() {
        return excludeIplogging==null?"":excludeIplogging;
    }

    public void setExcludeIplogging(String excludeIplogging) {
        this.excludeIplogging = excludeIplogging;
    }


}
