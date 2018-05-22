package enrich.and.com.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SettingInfo implements Serializable {

    @SerializedName("wdt_ID")
    @Expose
    private int wdtID;
    @SerializedName("userid")
    @Expose
    private int userid;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("userid_hash")
    @Expose
    private String useridHash;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;
    @SerializedName("enrich_phone")
    @Expose
    private String enrichPhone;
    @SerializedName("enrich_phone_forward_sms")
    @Expose
    private String enrichPhoneForwardSms;
    @SerializedName("enrich_phone_forward_call")
    @Expose
    private String enrichPhoneForwardCall;
    @SerializedName("fax_service")
    @Expose
    private String faxService;
    @SerializedName("fax_email")
    @Expose
    private String faxEmail;
    @SerializedName("webhook_crm")
    @Expose
    private String webhookCrm;
    @SerializedName("webhook_crm_endpoint")
    @Expose
    private String webhookCrmEndpoint;
    @SerializedName("webhook_share")
    @Expose
    private String webhookShare;
    @SerializedName("webhook_share_endpoint")
    @Expose
    private String webhookShareEndpoint;
    @SerializedName("webhook_drip")
    @Expose
    private String webhookDrip;
    @SerializedName("webhook_drip_p1")
    @Expose
    private String webhookDripP1;
    @SerializedName("webhook_drip_p2")
    @Expose
    private String webhookDripP2;
    @SerializedName("webhook_drip_p3")
    @Expose
    private String webhookDripP3;
    @SerializedName("webhook_drip_endpoint")
    @Expose
    private String webhookDripEndpoint;
    @SerializedName("webhook_drip_endpoint_p1")
    @Expose
    private String webhookDripEndpointP1;
    @SerializedName("webhook_drip_endpoint_p2")
    @Expose
    private String webhookDripEndpointP2;
    @SerializedName("webhook_drip_endpoint_p3")
    @Expose
    private String webhookDripEndpointP3;
    @SerializedName("smsdailyopenp1")
    @Expose
    private String smsdailyopenp1;
    @SerializedName("smsdailyopenp2")
    @Expose
    private String smsdailyopenp2;
    @SerializedName("smsdailyopenp3")
    @Expose
    private String smsdailyopenp3;
    @SerializedName("smsdailyclosep1")
    @Expose
    private String smsdailyclosep1;
    @SerializedName("smsdailyclosep2")
    @Expose
    private String smsdailyclosep2;
    @SerializedName("smsdailyclosep3")
    @Expose
    private String smsdailyclosep3;
    @SerializedName("smssundayopenp1")
    @Expose
    private String smssundayopenp1;
    @SerializedName("smssundayopenp2")
    @Expose
    private String smssundayopenp2;
    @SerializedName("smssundayopenp3")
    @Expose
    private String smssundayopenp3;
    @SerializedName("smssundayclosep1")
    @Expose
    private String smssundayclosep1;
    @SerializedName("smssundayclosep2")
    @Expose
    private String smssundayclosep2;
    @SerializedName("smssundayclosep3")
    @Expose
    private String smssundayclosep3;
    @SerializedName("usermobile")
    @Expose
    private String usermobile;
    @SerializedName("profilenamep1")
    @Expose
    private String profilenamep1;
    @SerializedName("profiledescp1")
    @Expose
    private String profiledescp1;
    @SerializedName("profilenamep2")
    @Expose
    private String profilenamep2;
    @SerializedName("profiledescp2")
    @Expose
    private String profiledescp2;
    @SerializedName("profilenamep3")
    @Expose
    private String profilenamep3;
    @SerializedName("profiledescp3")
    @Expose
    private String profiledescp3;
    @SerializedName("smsnotifications")
    @Expose
    private String smsnotifications;
    @SerializedName("emailnotifications")
    @Expose
    private String emailnotifications;
    @SerializedName("smsp1")
    @Expose
    private String smsp1;
    @SerializedName("smsp2")
    @Expose
    private String smsp2;
    @SerializedName("smsp3")
    @Expose
    private String smsp3;
    @SerializedName("imgsmsp1")
    @Expose
    private String imgsmsp1;
    @SerializedName("imgsmsp2")
    @Expose
    private String imgsmsp2;
    @SerializedName("imgsmsp3")
    @Expose
    private String imgsmsp3;
    @SerializedName("smsimagep1")
    @Expose
    private String smsimagep1;
    @SerializedName("smsimagep2")
    @Expose
    private String smsimagep2;
    @SerializedName("smsimagep3")
    @Expose
    private String smsimagep3;
    @SerializedName("smsmessagep1")
    @Expose
    private String smsmessagep1;
    @SerializedName("smsmessagep2")
    @Expose
    private String smsmessagep2;
    @SerializedName("smsmessagep3")
    @Expose
    private String smsmessagep3;
    @SerializedName("mvmp1")
    @Expose
    private String mvmp1;
    @SerializedName("mvmp2")
    @Expose
    private String mvmp2;
    @SerializedName("mvmp3")
    @Expose
    private String mvmp3;
    @SerializedName("mvmp1_2")
    @Expose
    private String mvmp12;
    @SerializedName("mvmp2_2")
    @Expose
    private String mvmp22;
    @SerializedName("mvmp3_2")
    @Expose
    private String mvmp32;
    @SerializedName("mvmp1_3")
    @Expose
    private String mvmp13;
    @SerializedName("mvmp2_3")
    @Expose
    private String mvmp23;
    @SerializedName("mvmp3_3")
    @Expose
    private String mvmp33;
    @SerializedName("mvmcid")
    @Expose
    private String mvmcid;
    @SerializedName("mvm_callerid_id")
    @Expose
    private String mvmCalleridId;
    @SerializedName("mvm_filename_p1")
    @Expose
    private String mvmFilenameP1;
    @SerializedName("mvm_filename_p2")
    @Expose
    private String mvmFilenameP2;
    @SerializedName("mvm_filename_p3")
    @Expose
    private String mvmFilenameP3;
    @SerializedName("mvm_schedule")
    @Expose
    private String mvmSchedule;
    @SerializedName("mvm_jwt_exp")
    @Expose
    private String mvmJwtExp;
    @SerializedName("mvm_jwt")
    @Expose
    private String mvmJwt;
    @SerializedName("rvm_id_p1_1")
    @Expose
    private String rvmIdP11;
    @SerializedName("rvm_id_p1_2")
    @Expose
    private String rvmIdP12;
    @SerializedName("rvm_id_p1_3")
    @Expose
    private String rvmIdP13;
    @SerializedName("rvm_id_p2_1")
    @Expose
    private String rvmIdP21;
    @SerializedName("rvm_id_p2_2")
    @Expose
    private String rvmIdP22;
    @SerializedName("rvm_id_p2_3")
    @Expose
    private String rvmIdP23;
    @SerializedName("rvm_id_p3_1")
    @Expose
    private String rvmIdP31;
    @SerializedName("rvm_id_p3_2")
    @Expose
    private String rvmIdP32;
    @SerializedName("rvm_id_p3_3")
    @Expose
    private String rvmIdP33;
    @SerializedName("mvm1p1")
    @Expose
    private String mvm1p1;
    @SerializedName("mvm1p3")
    @Expose
    private String mvm1p3;
    @SerializedName("mvm1p2")
    @Expose
    private String mvm1p2;
    @SerializedName("mvmdd1p1")
    @Expose
    private int mvmdd1p1;
    @SerializedName("mvmdd1p2")
    @Expose
    private int mvmdd1p2;
    @SerializedName("mvmdd1p3")
    @Expose
    private int mvmdd1p3;
    @SerializedName("mvmdt1p1")
    @Expose
    private String mvmdt1p1;
    @SerializedName("mvmdt1p2")
    @Expose
    private String mvmdt1p2;
    @SerializedName("mvmdt1p3")
    @Expose
    private String mvmdt1p3;
    @SerializedName("mvm2p1")
    @Expose
    private String mvm2p1;
    @SerializedName("mvm2p2")
    @Expose
    private String mvm2p2;
    @SerializedName("mvm2p3")
    @Expose
    private String mvm2p3;
    @SerializedName("mvmdd2p1")
    @Expose
    private int mvmdd2p1;
    @SerializedName("mvmdd2p2")
    @Expose
    private int mvmdd2p2;
    @SerializedName("mvmdd2p3")
    @Expose
    private int mvmdd2p3;
    @SerializedName("mvmdt2p1")
    @Expose
    private String mvmdt2p1;
    @SerializedName("mvmdt2p2")
    @Expose
    private String mvmdt2p2;
    @SerializedName("mvmdt2p3")
    @Expose
    private String mvmdt2p3;
    @SerializedName("mvm3p1")
    @Expose
    private String mvm3p1;
    @SerializedName("mvm3p2")
    @Expose
    private String mvm3p2;
    @SerializedName("mvm3p3")
    @Expose
    private String mvm3p3;
    @SerializedName("mvmdd3p1")
    @Expose
    private int mvmdd3p1;
    @SerializedName("mvmdd3p2")
    @Expose
    private int mvmdd3p2;
    @SerializedName("mvmdd3p3")
    @Expose
    private int mvmdd3p3;
    @SerializedName("mvmdt3p1")
    @Expose
    private String mvmdt3p1;
    @SerializedName("mvmdt3p2")
    @Expose
    private String mvmdt3p2;
    @SerializedName("mvmdt3p3")
    @Expose
    private String mvmdt3p3;
    @SerializedName("linkedin")
    @Expose
    private String linkedin;
    @SerializedName("linkedin_user")
    @Expose
    private String linkedinUser;
    @SerializedName("linkedin_pass")
    @Expose
    private String linkedinPass;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("facebookuser")
    @Expose
    private String facebookuser;
    @SerializedName("facebookpass")
    @Expose
    private String facebookpass;
    @SerializedName("facebookurl")
    @Expose
    private String facebookurl;
    @SerializedName("googlecontacts")
    @Expose
    private String googlecontacts;
    @SerializedName("googlecontacts_client_id")
    @Expose
    private String googlecontactsClientId;
    @SerializedName("googlecontacts_client_secret")
    @Expose
    private String googlecontactsClientSecret;
    @SerializedName("googlecontacts_redirect_uri")
    @Expose
    private String googlecontactsRedirectUri;
    @SerializedName("googlecontacts_simple_api_key")
    @Expose
    private String googlecontactsSimpleApiKey;
    @SerializedName("googlecontactgroup")
    @Expose
    private String googlecontactgroup;
    @SerializedName("pdfmerged")
    @Expose
    private String pdfmerged;
    @SerializedName("followupreminders")
    @Expose
    private String followupreminders;
    @SerializedName("dbaccess")
    @Expose
    private String dbaccess;
    @SerializedName("emaildripp1")
    @Expose
    private String emaildripp1;
    @SerializedName("emaildripp2")
    @Expose
    private String emaildripp2;
    @SerializedName("emaildripp3")
    @Expose
    private String emaildripp3;
    @SerializedName("crm")
    @Expose
    private String crm;
    @SerializedName("postcardp1")
    @Expose
    private String postcardp1;
    @SerializedName("postcardp2")
    @Expose
    private String postcardp2;
    @SerializedName("postcardp3")
    @Expose
    private String postcardp3;
    @SerializedName("dmm_customeremail")
    @Expose
    private String dmmCustomeremail;
    @SerializedName("dmm_idcust")
    @Expose
    private String dmmIdcust;
    @SerializedName("dmm_apikey")
    @Expose
    private String dmmApikey;
    @SerializedName("dmm_orderno_p1")
    @Expose
    private String dmmOrdernoP1;
    @SerializedName("dmm_orderno_p2")
    @Expose
    private String dmmOrdernoP2;
    @SerializedName("dmm_orderno_p3")
    @Expose
    private String dmmOrdernoP3;
    @SerializedName("notecardp1")
    @Expose
    private String notecardp1;
    @SerializedName("notecardp2")
    @Expose
    private String notecardp2;
    @SerializedName("notecardp3")
    @Expose
    private String notecardp3;
    @SerializedName("notecard_account")
    @Expose
    private String notecardAccount;
    @SerializedName("notecard_campaign")
    @Expose
    private String notecardCampaign;
    @SerializedName("notecard_api")
    @Expose
    private String notecardApi;
    @SerializedName("sendfaxp1")
    @Expose
    private String sendfaxp1;
    @SerializedName("sendfaxp2")
    @Expose
    private String sendfaxp2;
    @SerializedName("sendfaxp3")
    @Expose
    private String sendfaxp3;
    @SerializedName("freeloaderp1")
    @Expose
    private String freeloaderp1;
    @SerializedName("freeloaderp2")
    @Expose
    private String freeloaderp2;
    @SerializedName("freeloaderp3")
    @Expose
    private String freeloaderp3;
    @SerializedName("webformp1")
    @Expose
    private String webformp1;
    @SerializedName("webformp2")
    @Expose
    private String webformp2;
    @SerializedName("webformp3")
    @Expose
    private String webformp3;

    public SettingInfo(SettingInfo setting)
    {
        setWdtID(setting.getWdtID());
        setUserid(setting.getUserid());
        setUserId(setting.getUserId());
        setUseridHash(setting.getUseridHash());
        setProfilePhoto(setting.getProfilePhoto());
        setEnrichPhone(setting.getEnrichPhone());
        setEnrichPhoneForwardSms(setting.getEnrichPhoneForwardSms());
        setEnrichPhoneForwardCall(setting.getEnrichPhoneForwardCall());
        setFaxService(setting.getFaxService());
        setFaxEmail(setting.getFaxEmail());
        setWebhookCrm(setting.getWebhookCrm());
        setWebhookCrmEndpoint(setting.getWebhookCrmEndpoint());
        setWebhookShare(setting.getWebhookShare());
        setWebhookShareEndpoint(setting.getWebhookShareEndpoint());
        setWebhookDrip(setting.getWebhookDrip());
        setWebhookDripP1(setting.getWebhookDripP1());
        setWebhookDripP2(setting.getWebhookDripP2());
        setWebhookDripP3(setting.getWebhookDripP3());
        setWebhookDripEndpoint(setting.getWebhookDripEndpoint());
        setWebhookDripEndpointP1(setting.getWebhookDripEndpointP1());
        setWebhookDripEndpointP2(setting.getWebhookDripEndpointP2());
        setWebhookDripEndpointP3(setting.getWebhookDripEndpointP3());
        setSmsdailyopenp1(setting.getSmsdailyopenp1());
        setSmsdailyopenp2(setting.getSmsdailyopenp2());
        setSmsdailyopenp3(setting.getSmsdailyopenp3());
        setSmsdailyclosep1(setting.getSmsdailyclosep1());
        setSmsdailyclosep2(setting.getSmsdailyclosep2());
        setSmsdailyclosep3(setting.getSmsdailyclosep3());
        setSmssundayopenp1(setting.getSmssundayopenp1());
        setSmssundayopenp2(setting.getSmssundayopenp2());
        setSmssundayopenp3(setting.getSmssundayopenp3());
        setSmssundayclosep1(setting.getSmssundayclosep1());
        setSmssundayclosep2(setting.getSmssundayclosep2());
        setSmssundayclosep3(setting.getSmssundayclosep3());
        setUsermobile(setting.getUsermobile());
        setProfilenamep1(setting.getProfilenamep1());
        setProfiledescp1(setting.getProfiledescp1());
        setProfilenamep2(setting.getProfilenamep2());
        setProfiledescp2(setting.getProfiledescp2());
        setProfilenamep3(setting.getProfilenamep3());
        setProfiledescp3(setting.getProfiledescp3());
        setSmsnotifications(setting.getSmsnotifications());
        setEmailnotifications(setting.getEmailnotifications());
        setSmsp1(setting.getSmsp1());
        setSmsp2(setting.getSmsp2());
        setSmsp3(setting.getSmsp3());
        setImgsmsp1(setting.getImgsmsp1());
        setImgsmsp2(setting.getImgsmsp2());
        setImgsmsp3(setting.getImgsmsp3());
        setSmsimagep1(setting.getSmsimagep1());
        setSmsimagep2(setting.getSmsimagep2());
        setSmsimagep3(setting.getSmsimagep3());
        setSmsmessagep1(setting.getSmsmessagep1());
        setSmsmessagep2(setting.getSmsmessagep2());
        setSmsmessagep3(setting.getSmsmessagep3());
        setMvmp1(setting.getMvmp1());
        setMvmp2(setting.getMvmp2());
        setMvmp3(setting.getMvmp3());
        setMvmp12(setting.getMvmp12());
        setMvmp22(setting.getMvmp22());
        setMvmp32(setting.getMvmp32());
        setMvmp13(setting.getMvmp13());
        setMvmp23(setting.getMvmp23());
        setMvmp33(setting.getMvmp33());
        setMvmcid(setting.getMvmcid());
        setMvmCalleridId(setting.getMvmCalleridId());
        setMvmFilenameP1(setting.getMvmFilenameP1());
        setMvmFilenameP2(setting.getMvmFilenameP2());
        setMvmFilenameP3(setting.getMvmFilenameP3());
        setMvmSchedule(setting.getMvmSchedule());
        setMvmJwtExp(setting.getMvmJwtExp());
        setMvmJwt(setting.getMvmJwt());
        setRvmIdP11(setting.getRvmIdP11());
        setRvmIdP12(setting.getRvmIdP12());
        setRvmIdP13(setting.getRvmIdP13());
        setRvmIdP21(setting.getRvmIdP21());
        setRvmIdP22(setting.getRvmIdP22());
        setRvmIdP23(setting.getRvmIdP23());
        setRvmIdP31(setting.getRvmIdP31());
        setRvmIdP32(setting.getRvmIdP32());
        setRvmIdP33(setting.getRvmIdP33());
        setMvm1p1(setting.getMvm1p1());
        setMvm1p3(setting.getMvm1p3());
        setMvm1p2(setting.getMvm1p2());
        setMvmdd1p1(setting.getMvmdd1p1());
        setMvmdd1p2(setting.getMvmdd1p2());
        setMvmdd1p3(setting.getMvmdd1p3());
        setMvmdt1p1(setting.getMvmdt1p1());
        setMvmdt1p2(setting.getMvmdt1p2());
        setMvmdt1p3(setting.getMvmdt1p3());
        setMvm2p1(setting.getMvm2p1());
        setMvm2p2(setting.getMvm2p2());
        setMvm2p3(setting.getMvm2p3());
        setMvmdd2p1(setting.getMvmdd2p1());
        setMvmdd2p2(setting.getMvmdd2p2());
        setMvmdd2p3(setting.getMvmdd2p3());
        setMvmdt2p1(setting.getMvmdt2p1());
        setMvmdt2p2(setting.getMvmdt2p2());
        setMvmdt2p3(setting.getMvmdt2p3());
        setMvm3p1(setting.getMvm3p1());
        setMvm3p2(setting.getMvm3p2());
        setMvm3p3(setting.getMvm3p3());
        setMvmdd3p1(setting.getMvmdd3p1());
        setMvmdd3p2(setting.getMvmdd3p2());
        setMvmdd3p3(setting.getMvmdd3p3());
        setMvmdt3p1(setting.getMvmdt3p1());
        setMvmdt3p2(setting.getMvmdt3p2());
        setMvmdt3p3(setting.getMvmdt3p3());
        setLinkedin(setting.getLinkedin());
        setLinkedinUser(setting.getLinkedinUser());
        setLinkedinPass(setting.getLinkedinPass());
        setFacebook(setting.getFacebook());
        setFacebookuser(setting.getFacebookuser());
        setFacebookpass(setting.getFacebookpass());
        setFacebookurl(setting.getFacebookurl());
        setGooglecontacts(setting.getGooglecontacts());
        setGooglecontactsClientId(setting.getGooglecontactsClientId());
        setGooglecontactsClientSecret(setting.getGooglecontactsClientSecret());
        setGooglecontactsRedirectUri(setting.getGooglecontactsRedirectUri());
        setGooglecontactsSimpleApiKey(setting.getGooglecontactsSimpleApiKey());
        setGooglecontactgroup(setting.getGooglecontactgroup());
        setPdfmerged(setting.getPdfmerged());
        setFollowupreminders(setting.getFollowupreminders());
        setDbaccess(setting.getDbaccess());
        setEmaildripp1(setting.getEmaildripp1());
        setEmaildripp2(setting.getEmaildripp2());
        setEmaildripp3(setting.getEmaildripp3());
        setCrm(setting.getCrm());
        setPostcardp1(setting.getPostcardp1());
        setPostcardp2(setting.getPostcardp2());
        setPostcardp3(setting.getPostcardp3());
        setDmmCustomeremail(setting.getDmmCustomeremail());
        setDmmIdcust(setting.getDmmIdcust());
        setDmmApikey(setting.getDmmApikey());
        setDmmOrdernoP1(setting.getDmmOrdernoP1());
        setDmmOrdernoP2(setting.getDmmOrdernoP2());
        setDmmOrdernoP3(setting.getDmmOrdernoP3());
        setNotecardp1(setting.getNotecardp1());
        setNotecardp2(setting.getNotecardp2());
        setNotecardp3(setting.getNotecardp3());
        setNotecardAccount(setting.getNotecardAccount());
        setNotecardCampaign(setting.getNotecardCampaign());
        setNotecardApi(setting.getNotecardApi());
        setSendfaxp1(setting.getSendfaxp1());
        setSendfaxp2(setting.getSendfaxp2());
        setSendfaxp3(setting.getSendfaxp3());
        setFreeloaderp1(setting.getFreeloaderp1());
        setFreeloaderp2(setting.getFreeloaderp2());
        setFreeloaderp3(setting.getFreeloaderp3());
        setWebformp1(setting.getWebformp1());
        setWebformp2(setting.getWebformp2());
        setWebformp3(setting.getWebformp3());

    }


    public int getWdtID() {
        return wdtID;
    }

    public void setWdtID(int wdtID) {
        this.wdtID = wdtID;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUseridHash() {
        return useridHash;
    }

    public void setUseridHash(String useridHash) {
        this.useridHash = useridHash;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getEnrichPhone() {
        return enrichPhone;
    }

    public void setEnrichPhone(String enrichPhone) {
        this.enrichPhone = enrichPhone;
    }

    public String getEnrichPhoneForwardSms() {
        return enrichPhoneForwardSms;
    }

    public void setEnrichPhoneForwardSms(String enrichPhoneForwardSms) {
        this.enrichPhoneForwardSms = enrichPhoneForwardSms;
    }

    public String getEnrichPhoneForwardCall() {
        return enrichPhoneForwardCall;
    }

    public void setEnrichPhoneForwardCall(String enrichPhoneForwardCall) {
        this.enrichPhoneForwardCall = enrichPhoneForwardCall;
    }

    public String getFaxService() {
        return faxService;
    }

    public void setFaxService(String faxService) {
        this.faxService = faxService;
    }

    public String getFaxEmail() {
        return faxEmail;
    }

    public void setFaxEmail(String faxEmail) {
        this.faxEmail = faxEmail;
    }

    public String getWebhookCrm() {
        return webhookCrm;
    }

    public void setWebhookCrm(String webhookCrm) {
        this.webhookCrm = webhookCrm;
    }

    public String getWebhookCrmEndpoint() {
        return webhookCrmEndpoint;
    }

    public void setWebhookCrmEndpoint(String webhookCrmEndpoint) {
        this.webhookCrmEndpoint = webhookCrmEndpoint;
    }

    public String getWebhookShare() {
        return webhookShare;
    }

    public void setWebhookShare(String webhookShare) {
        this.webhookShare = webhookShare;
    }

    public String getWebhookShareEndpoint() {
        return webhookShareEndpoint;
    }

    public void setWebhookShareEndpoint(String webhookShareEndpoint) {
        this.webhookShareEndpoint = webhookShareEndpoint;
    }

    public String getWebhookDrip() {
        return webhookDrip;
    }

    public void setWebhookDrip(String webhookDrip) {
        this.webhookDrip = webhookDrip;
    }

    public String getWebhookDripP1() {
        return webhookDripP1;
    }

    public void setWebhookDripP1(String webhookDripP1) {
        this.webhookDripP1 = webhookDripP1;
    }

    public String getWebhookDripP2() {
        return webhookDripP2;
    }

    public void setWebhookDripP2(String webhookDripP2) {
        this.webhookDripP2 = webhookDripP2;
    }

    public String getWebhookDripP3() {
        return webhookDripP3;
    }

    public void setWebhookDripP3(String webhookDripP3) {
        this.webhookDripP3 = webhookDripP3;
    }

    public String getWebhookDripEndpoint() {
        return webhookDripEndpoint;
    }

    public void setWebhookDripEndpoint(String webhookDripEndpoint) {
        this.webhookDripEndpoint = webhookDripEndpoint;
    }

    public String getWebhookDripEndpointP1() {
        return webhookDripEndpointP1;
    }

    public void setWebhookDripEndpointP1(String webhookDripEndpointP1) {
        this.webhookDripEndpointP1 = webhookDripEndpointP1;
    }

    public String getWebhookDripEndpointP2() {
        return webhookDripEndpointP2;
    }

    public void setWebhookDripEndpointP2(String webhookDripEndpointP2) {
        this.webhookDripEndpointP2 = webhookDripEndpointP2;
    }

    public String getWebhookDripEndpointP3() {
        return webhookDripEndpointP3;
    }

    public void setWebhookDripEndpointP3(String webhookDripEndpointP3) {
        this.webhookDripEndpointP3 = webhookDripEndpointP3;
    }

    public String getSmsdailyopenp1() {
        return smsdailyopenp1;
    }

    public void setSmsdailyopenp1(String smsdailyopenp1) {
        this.smsdailyopenp1 = smsdailyopenp1;
    }

    public String getSmsdailyopenp2() {
        return smsdailyopenp2;
    }

    public void setSmsdailyopenp2(String smsdailyopenp2) {
        this.smsdailyopenp2 = smsdailyopenp2;
    }

    public String getSmsdailyopenp3() {
        return smsdailyopenp3;
    }

    public void setSmsdailyopenp3(String smsdailyopenp3) {
        this.smsdailyopenp3 = smsdailyopenp3;
    }

    public String getSmsdailyclosep1() {
        return smsdailyclosep1;
    }

    public void setSmsdailyclosep1(String smsdailyclosep1) {
        this.smsdailyclosep1 = smsdailyclosep1;
    }

    public String getSmsdailyclosep2() {
        return smsdailyclosep2;
    }

    public void setSmsdailyclosep2(String smsdailyclosep2) {
        this.smsdailyclosep2 = smsdailyclosep2;
    }

    public String getSmsdailyclosep3() {
        return smsdailyclosep3;
    }

    public void setSmsdailyclosep3(String smsdailyclosep3) {
        this.smsdailyclosep3 = smsdailyclosep3;
    }

    public String getSmssundayopenp1() {
        return smssundayopenp1;
    }

    public void setSmssundayopenp1(String smssundayopenp1) {
        this.smssundayopenp1 = smssundayopenp1;
    }

    public String getSmssundayopenp2() {
        return smssundayopenp2;
    }

    public void setSmssundayopenp2(String smssundayopenp2) {
        this.smssundayopenp2 = smssundayopenp2;
    }

    public String getSmssundayopenp3() {
        return smssundayopenp3;
    }

    public void setSmssundayopenp3(String smssundayopenp3) {
        this.smssundayopenp3 = smssundayopenp3;
    }

    public String getSmssundayclosep1() {
        return smssundayclosep1;
    }

    public void setSmssundayclosep1(String smssundayclosep1) {
        this.smssundayclosep1 = smssundayclosep1;
    }

    public String getSmssundayclosep2() {
        return smssundayclosep2;
    }

    public void setSmssundayclosep2(String smssundayclosep2) {
        this.smssundayclosep2 = smssundayclosep2;
    }

    public String getSmssundayclosep3() {
        return smssundayclosep3;
    }

    public void setSmssundayclosep3(String smssundayclosep3) {
        this.smssundayclosep3 = smssundayclosep3;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getProfilenamep1() {
        return profilenamep1;
    }

    public void setProfilenamep1(String profilenamep1) {
        this.profilenamep1 = profilenamep1;
    }

    public String getProfiledescp1() {
        return profiledescp1;
    }

    public void setProfiledescp1(String profiledescp1) {
        this.profiledescp1 = profiledescp1;
    }

    public String getProfilenamep2() {
        return profilenamep2;
    }

    public void setProfilenamep2(String profilenamep2) {
        this.profilenamep2 = profilenamep2;
    }

    public String getProfiledescp2() {
        return profiledescp2;
    }

    public void setProfiledescp2(String profiledescp2) {
        this.profiledescp2 = profiledescp2;
    }

    public String getProfilenamep3() {
        return profilenamep3;
    }

    public void setProfilenamep3(String profilenamep3) {
        this.profilenamep3 = profilenamep3;
    }

    public String getProfiledescp3() {
        return profiledescp3;
    }

    public void setProfiledescp3(String profiledescp3) {
        this.profiledescp3 = profiledescp3;
    }

    public String getSmsnotifications() {
        return smsnotifications;
    }

    public void setSmsnotifications(String smsnotifications) {
        this.smsnotifications = smsnotifications;
    }

    public String getEmailnotifications() {
        return emailnotifications;
    }

    public void setEmailnotifications(String emailnotifications) {
        this.emailnotifications = emailnotifications;
    }

    public String getSmsp1() {
        return smsp1;
    }

    public void setSmsp1(String smsp1) {
        this.smsp1 = smsp1;
    }

    public String getSmsp2() {
        return smsp2;
    }

    public void setSmsp2(String smsp2) {
        this.smsp2 = smsp2;
    }

    public String getSmsp3() {
        return smsp3;
    }

    public void setSmsp3(String smsp3) {
        this.smsp3 = smsp3;
    }

    public String getImgsmsp1() {
        return imgsmsp1;
    }

    public void setImgsmsp1(String imgsmsp1) {
        this.imgsmsp1 = imgsmsp1;
    }

    public String getImgsmsp2() {
        return imgsmsp2;
    }

    public void setImgsmsp2(String imgsmsp2) {
        this.imgsmsp2 = imgsmsp2;
    }

    public String getImgsmsp3() {
        return imgsmsp3;
    }

    public void setImgsmsp3(String imgsmsp3) {
        this.imgsmsp3 = imgsmsp3;
    }

    public String getSmsimagep1() {
        return smsimagep1;
    }

    public void setSmsimagep1(String smsimagep1) {
        this.smsimagep1 = smsimagep1;
    }

    public String getSmsimagep2() {
        return smsimagep2;
    }

    public void setSmsimagep2(String smsimagep2) {
        this.smsimagep2 = smsimagep2;
    }

    public String getSmsimagep3() {
        return smsimagep3;
    }

    public void setSmsimagep3(String smsimagep3) {
        this.smsimagep3 = smsimagep3;
    }

    public String getSmsmessagep1() {
        return smsmessagep1;
    }

    public void setSmsmessagep1(String smsmessagep1) {
        this.smsmessagep1 = smsmessagep1;
    }

    public String getSmsmessagep2() {
        return smsmessagep2;
    }

    public void setSmsmessagep2(String smsmessagep2) {
        this.smsmessagep2 = smsmessagep2;
    }

    public String getSmsmessagep3() {
        return smsmessagep3;
    }

    public void setSmsmessagep3(String smsmessagep3) {
        this.smsmessagep3 = smsmessagep3;
    }

    public String getMvmp1() {
        return mvmp1;
    }

    public void setMvmp1(String mvmp1) {
        this.mvmp1 = mvmp1;
    }

    public String getMvmp2() {
        return mvmp2;
    }

    public void setMvmp2(String mvmp2) {
        this.mvmp2 = mvmp2;
    }

    public String getMvmp3() {
        return mvmp3;
    }

    public void setMvmp3(String mvmp3) {
        this.mvmp3 = mvmp3;
    }

    public String getMvmp12() {
        return mvmp12;
    }

    public void setMvmp12(String mvmp12) {
        this.mvmp12 = mvmp12;
    }

    public String getMvmp22() {
        return mvmp22;
    }

    public void setMvmp22(String mvmp22) {
        this.mvmp22 = mvmp22;
    }

    public String getMvmp32() {
        return mvmp32;
    }

    public void setMvmp32(String mvmp32) {
        this.mvmp32 = mvmp32;
    }

    public String getMvmp13() {
        return mvmp13;
    }

    public void setMvmp13(String mvmp13) {
        this.mvmp13 = mvmp13;
    }

    public String getMvmp23() {
        return mvmp23;
    }

    public void setMvmp23(String mvmp23) {
        this.mvmp23 = mvmp23;
    }

    public String getMvmp33() {
        return mvmp33;
    }

    public void setMvmp33(String mvmp33) {
        this.mvmp33 = mvmp33;
    }

    public String getMvmcid() {
        return mvmcid;
    }

    public void setMvmcid(String mvmcid) {
        this.mvmcid = mvmcid;
    }

    public String getMvmCalleridId() {
        return mvmCalleridId;
    }

    public void setMvmCalleridId(String mvmCalleridId) {
        this.mvmCalleridId = mvmCalleridId;
    }

    public String getMvmFilenameP1() {
        return mvmFilenameP1;
    }

    public void setMvmFilenameP1(String mvmFilenameP1) {
        this.mvmFilenameP1 = mvmFilenameP1;
    }

    public String getMvmFilenameP2() {
        return mvmFilenameP2;
    }

    public void setMvmFilenameP2(String mvmFilenameP2) {
        this.mvmFilenameP2 = mvmFilenameP2;
    }

    public String getMvmFilenameP3() {
        return mvmFilenameP3;
    }

    public void setMvmFilenameP3(String mvmFilenameP3) {
        this.mvmFilenameP3 = mvmFilenameP3;
    }

    public String getMvmSchedule() {
        return mvmSchedule;
    }

    public void setMvmSchedule(String mvmSchedule) {
        this.mvmSchedule = mvmSchedule;
    }

    public String getMvmJwtExp() {
        return mvmJwtExp;
    }

    public void setMvmJwtExp(String mvmJwtExp) {
        this.mvmJwtExp = mvmJwtExp;
    }

    public String getMvmJwt() {
        return mvmJwt;
    }

    public void setMvmJwt(String mvmJwt) {
        this.mvmJwt = mvmJwt;
    }

    public String getRvmIdP11() {
        return rvmIdP11;
    }

    public void setRvmIdP11(String rvmIdP11) {
        this.rvmIdP11 = rvmIdP11;
    }

    public String getRvmIdP12() {
        return rvmIdP12;
    }

    public void setRvmIdP12(String rvmIdP12) {
        this.rvmIdP12 = rvmIdP12;
    }

    public String getRvmIdP13() {
        return rvmIdP13;
    }

    public void setRvmIdP13(String rvmIdP13) {
        this.rvmIdP13 = rvmIdP13;
    }

    public String getRvmIdP21() {
        return rvmIdP21;
    }

    public void setRvmIdP21(String rvmIdP21) {
        this.rvmIdP21 = rvmIdP21;
    }

    public String getRvmIdP22() {
        return rvmIdP22;
    }

    public void setRvmIdP22(String rvmIdP22) {
        this.rvmIdP22 = rvmIdP22;
    }

    public String getRvmIdP23() {
        return rvmIdP23;
    }

    public void setRvmIdP23(String rvmIdP23) {
        this.rvmIdP23 = rvmIdP23;
    }

    public String getRvmIdP31() {
        return rvmIdP31;
    }

    public void setRvmIdP31(String rvmIdP31) {
        this.rvmIdP31 = rvmIdP31;
    }

    public String getRvmIdP32() {
        return rvmIdP32;
    }

    public void setRvmIdP32(String rvmIdP32) {
        this.rvmIdP32 = rvmIdP32;
    }

    public String getRvmIdP33() {
        return rvmIdP33;
    }

    public void setRvmIdP33(String rvmIdP33) {
        this.rvmIdP33 = rvmIdP33;
    }

    public String getMvm1p1() {
        return mvm1p1;
    }

    public void setMvm1p1(String mvm1p1) {
        this.mvm1p1 = mvm1p1;
    }

    public String getMvm1p3() {
        return mvm1p3;
    }

    public void setMvm1p3(String mvm1p3) {
        this.mvm1p3 = mvm1p3;
    }

    public String getMvm1p2() {
        return mvm1p2;
    }

    public void setMvm1p2(String mvm1p2) {
        this.mvm1p2 = mvm1p2;
    }

    public int getMvmdd1p1() {
        return mvmdd1p1;
    }

    public void setMvmdd1p1(int mvmdd1p1) {
        this.mvmdd1p1 = mvmdd1p1;
    }

    public int getMvmdd1p2() {
        return mvmdd1p2;
    }

    public void setMvmdd1p2(int mvmdd1p2) {
        this.mvmdd1p2 = mvmdd1p2;
    }

    public int getMvmdd1p3() {
        return mvmdd1p3;
    }

    public void setMvmdd1p3(int mvmdd1p3) {
        this.mvmdd1p3 = mvmdd1p3;
    }

    public String getMvmdt1p1() {
        return mvmdt1p1;
    }

    public void setMvmdt1p1(String mvmdt1p1) {
        this.mvmdt1p1 = mvmdt1p1;
    }

    public String getMvmdt1p2() {
        return mvmdt1p2;
    }

    public void setMvmdt1p2(String mvmdt1p2) {
        this.mvmdt1p2 = mvmdt1p2;
    }

    public String getMvmdt1p3() {
        return mvmdt1p3;
    }

    public void setMvmdt1p3(String mvmdt1p3) {
        this.mvmdt1p3 = mvmdt1p3;
    }

    public String getMvm2p1() {
        return mvm2p1;
    }

    public void setMvm2p1(String mvm2p1) {
        this.mvm2p1 = mvm2p1;
    }

    public String getMvm2p2() {
        return mvm2p2;
    }

    public void setMvm2p2(String mvm2p2) {
        this.mvm2p2 = mvm2p2;
    }

    public String getMvm2p3() {
        return mvm2p3;
    }

    public void setMvm2p3(String mvm2p3) {
        this.mvm2p3 = mvm2p3;
    }

    public int getMvmdd2p1() {
        return mvmdd2p1;
    }

    public void setMvmdd2p1(int mvmdd2p1) {
        this.mvmdd2p1 = mvmdd2p1;
    }

    public int getMvmdd2p2() {
        return mvmdd2p2;
    }

    public void setMvmdd2p2(int mvmdd2p2) {
        this.mvmdd2p2 = mvmdd2p2;
    }

    public int getMvmdd2p3() {
        return mvmdd2p3;
    }

    public void setMvmdd2p3(int mvmdd2p3) {
        this.mvmdd2p3 = mvmdd2p3;
    }

    public String getMvmdt2p1() {
        return mvmdt2p1;
    }

    public void setMvmdt2p1(String mvmdt2p1) {
        this.mvmdt2p1 = mvmdt2p1;
    }

    public String getMvmdt2p2() {
        return mvmdt2p2;
    }

    public void setMvmdt2p2(String mvmdt2p2) {
        this.mvmdt2p2 = mvmdt2p2;
    }

    public String getMvmdt2p3() {
        return mvmdt2p3;
    }

    public void setMvmdt2p3(String mvmdt2p3) {
        this.mvmdt2p3 = mvmdt2p3;
    }

    public String getMvm3p1() {
        return mvm3p1;
    }

    public void setMvm3p1(String mvm3p1) {
        this.mvm3p1 = mvm3p1;
    }

    public String getMvm3p2() {
        return mvm3p2;
    }

    public void setMvm3p2(String mvm3p2) {
        this.mvm3p2 = mvm3p2;
    }

    public String getMvm3p3() {
        return mvm3p3;
    }

    public void setMvm3p3(String mvm3p3) {
        this.mvm3p3 = mvm3p3;
    }

    public int getMvmdd3p1() {
        return mvmdd3p1;
    }

    public void setMvmdd3p1(int mvmdd3p1) {
        this.mvmdd3p1 = mvmdd3p1;
    }

    public int getMvmdd3p2() {
        return mvmdd3p2;
    }

    public void setMvmdd3p2(int mvmdd3p2) {
        this.mvmdd3p2 = mvmdd3p2;
    }

    public int getMvmdd3p3() {
        return mvmdd3p3;
    }

    public void setMvmdd3p3(int mvmdd3p3) {
        this.mvmdd3p3 = mvmdd3p3;
    }

    public String getMvmdt3p1() {
        return mvmdt3p1;
    }

    public void setMvmdt3p1(String mvmdt3p1) {
        this.mvmdt3p1 = mvmdt3p1;
    }

    public String getMvmdt3p2() {
        return mvmdt3p2;
    }

    public void setMvmdt3p2(String mvmdt3p2) {
        this.mvmdt3p2 = mvmdt3p2;
    }

    public String getMvmdt3p3() {
        return mvmdt3p3;
    }

    public void setMvmdt3p3(String mvmdt3p3) {
        this.mvmdt3p3 = mvmdt3p3;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getLinkedinUser() {
        return linkedinUser;
    }

    public void setLinkedinUser(String linkedinUser) {
        this.linkedinUser = linkedinUser;
    }

    public String getLinkedinPass() {
        return linkedinPass;
    }

    public void setLinkedinPass(String linkedinPass) {
        this.linkedinPass = linkedinPass;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFacebookuser() {
        return facebookuser;
    }

    public void setFacebookuser(String facebookuser) {
        this.facebookuser = facebookuser;
    }

    public String getFacebookpass() {
        return facebookpass;
    }

    public void setFacebookpass(String facebookpass) {
        this.facebookpass = facebookpass;
    }

    public String getFacebookurl() {
        return facebookurl;
    }

    public void setFacebookurl(String facebookurl) {
        this.facebookurl = facebookurl;
    }

    public String getGooglecontacts() {
        return googlecontacts;
    }

    public void setGooglecontacts(String googlecontacts) {
        this.googlecontacts = googlecontacts;
    }

    public String getGooglecontactsClientId() {
        return googlecontactsClientId;
    }

    public void setGooglecontactsClientId(String googlecontactsClientId) {
        this.googlecontactsClientId = googlecontactsClientId;
    }

    public String getGooglecontactsClientSecret() {
        return googlecontactsClientSecret;
    }

    public void setGooglecontactsClientSecret(String googlecontactsClientSecret) {
        this.googlecontactsClientSecret = googlecontactsClientSecret;
    }

    public String getGooglecontactsRedirectUri() {
        return googlecontactsRedirectUri;
    }

    public void setGooglecontactsRedirectUri(String googlecontactsRedirectUri) {
        this.googlecontactsRedirectUri = googlecontactsRedirectUri;
    }

    public String getGooglecontactsSimpleApiKey() {
        return googlecontactsSimpleApiKey;
    }

    public void setGooglecontactsSimpleApiKey(String googlecontactsSimpleApiKey) {
        this.googlecontactsSimpleApiKey = googlecontactsSimpleApiKey;
    }

    public String getGooglecontactgroup() {
        return googlecontactgroup;
    }

    public void setGooglecontactgroup(String googlecontactgroup) {
        this.googlecontactgroup = googlecontactgroup;
    }

    public String getPdfmerged() {
        return pdfmerged;
    }

    public void setPdfmerged(String pdfmerged) {
        this.pdfmerged = pdfmerged;
    }

    public String getFollowupreminders() {
        return followupreminders;
    }

    public void setFollowupreminders(String followupreminders) {
        this.followupreminders = followupreminders;
    }

    public String getDbaccess() {
        return dbaccess;
    }

    public void setDbaccess(String dbaccess) {
        this.dbaccess = dbaccess;
    }

    public String getEmaildripp1() {
        return emaildripp1;
    }

    public void setEmaildripp1(String emaildripp1) {
        this.emaildripp1 = emaildripp1;
    }

    public String getEmaildripp2() {
        return emaildripp2;
    }

    public void setEmaildripp2(String emaildripp2) {
        this.emaildripp2 = emaildripp2;
    }

    public String getEmaildripp3() {
        return emaildripp3;
    }

    public void setEmaildripp3(String emaildripp3) {
        this.emaildripp3 = emaildripp3;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getPostcardp1() {
        return postcardp1;
    }

    public void setPostcardp1(String postcardp1) {
        this.postcardp1 = postcardp1;
    }

    public String getPostcardp2() {
        return postcardp2;
    }

    public void setPostcardp2(String postcardp2) {
        this.postcardp2 = postcardp2;
    }

    public String getPostcardp3() {
        return postcardp3;
    }

    public void setPostcardp3(String postcardp3) {
        this.postcardp3 = postcardp3;
    }

    public String getDmmCustomeremail() {
        return dmmCustomeremail;
    }

    public void setDmmCustomeremail(String dmmCustomeremail) {
        this.dmmCustomeremail = dmmCustomeremail;
    }

    public String getDmmIdcust() {
        return dmmIdcust;
    }

    public void setDmmIdcust(String dmmIdcust) {
        this.dmmIdcust = dmmIdcust;
    }

    public String getDmmApikey() {
        return dmmApikey;
    }

    public void setDmmApikey(String dmmApikey) {
        this.dmmApikey = dmmApikey;
    }

    public String getDmmOrdernoP1() {
        return dmmOrdernoP1;
    }

    public void setDmmOrdernoP1(String dmmOrdernoP1) {
        this.dmmOrdernoP1 = dmmOrdernoP1;
    }

    public String getDmmOrdernoP2() {
        return dmmOrdernoP2;
    }

    public void setDmmOrdernoP2(String dmmOrdernoP2) {
        this.dmmOrdernoP2 = dmmOrdernoP2;
    }

    public String getDmmOrdernoP3() {
        return dmmOrdernoP3;
    }

    public void setDmmOrdernoP3(String dmmOrdernoP3) {
        this.dmmOrdernoP3 = dmmOrdernoP3;
    }

    public String getNotecardp1() {
        return notecardp1;
    }

    public void setNotecardp1(String notecardp1) {
        this.notecardp1 = notecardp1;
    }

    public String getNotecardp2() {
        return notecardp2;
    }

    public void setNotecardp2(String notecardp2) {
        this.notecardp2 = notecardp2;
    }

    public String getNotecardp3() {
        return notecardp3;
    }

    public void setNotecardp3(String notecardp3) {
        this.notecardp3 = notecardp3;
    }

    public String getNotecardAccount() {
        return notecardAccount;
    }

    public void setNotecardAccount(String notecardAccount) {
        this.notecardAccount = notecardAccount;
    }

    public String getNotecardCampaign() {
        return notecardCampaign;
    }

    public void setNotecardCampaign(String notecardCampaign) {
        this.notecardCampaign = notecardCampaign;
    }

    public String getNotecardApi() {
        return notecardApi;
    }

    public void setNotecardApi(String notecardApi) {
        this.notecardApi = notecardApi;
    }

    public String getSendfaxp1() {
        return sendfaxp1;
    }

    public void setSendfaxp1(String sendfaxp1) {
        this.sendfaxp1 = sendfaxp1;
    }

    public String getSendfaxp2() {
        return sendfaxp2;
    }

    public void setSendfaxp2(String sendfaxp2) {
        this.sendfaxp2 = sendfaxp2;
    }

    public String getSendfaxp3() {
        return sendfaxp3;
    }

    public void setSendfaxp3(String sendfaxp3) {
        this.sendfaxp3 = sendfaxp3;
    }

    public String getFreeloaderp1() {
        return freeloaderp1;
    }

    public void setFreeloaderp1(String freeloaderp1) {
        this.freeloaderp1 = freeloaderp1;
    }

    public String getFreeloaderp2() {
        return freeloaderp2;
    }

    public void setFreeloaderp2(String freeloaderp2) {
        this.freeloaderp2 = freeloaderp2;
    }

    public String getFreeloaderp3() {
        return freeloaderp3;
    }

    public void setFreeloaderp3(String freeloaderp3) {
        this.freeloaderp3 = freeloaderp3;
    }

    public String getWebformp1() {
        return webformp1;
    }

    public void setWebformp1(String webformp1) {
        this.webformp1 = webformp1;
    }

    public String getWebformp2() {
        return webformp2;
    }

    public void setWebformp2(String webformp2) {
        this.webformp2 = webformp2;
    }

    public String getWebformp3() {
        return webformp3;
    }

    public void setWebformp3(String webformp3) {
        this.webformp3 = webformp3;
    }


}