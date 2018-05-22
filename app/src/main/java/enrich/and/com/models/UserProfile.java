package enrich.and.com.models;

public class UserProfile {
    private int profileIndex = 1;
    private String profileName = "";
    private String profileDescription = "";
    private String profilePhotoPath = "";
    private String profileSmsMessage = "";
    private String profileVoiceMailPath = "";
    private String profileSmsImagePath = "";

    public UserProfile()
    {

    }
    public UserProfile(int profileIndex , String profileName , String profileDescription , String profilePhotoPath,
                            String profileSmsMessage , String profileVoiceMailPath , String profileSmsImagePath)
    {
        this.profileIndex = profileIndex;
        this.profileName = profileName;
        this.profileDescription = profileDescription;
        this.profilePhotoPath = profilePhotoPath;
        this.profileSmsMessage = profileSmsMessage;
        this.profileVoiceMailPath = profileVoiceMailPath;
        this.profileSmsImagePath = profileSmsImagePath;
    }

    public void setProfileIndex(int profileIndex){this.profileIndex = profileIndex;}
    public int getProfileIndex(){return this.profileIndex;}

    public void setProfileName(String profileName){this.profileName = profileName;}
    public String getProfileName(){return this.profileName;}

    public void setProfileDescription(String profileDescription){this.profileDescription = profileDescription;}
    public String getProfileDescription(){return this.profileDescription;}

    public void setProfilePhotoPath(String profilePhotoPath){this.profilePhotoPath = profilePhotoPath;}
    public String getProfilePhotoPath(){return this.profilePhotoPath;}

    public void setProfileSmsMessage(String profileSmsMessage){this.profileSmsMessage = profileSmsMessage;}
    public String getProfileSmsMessage(){return this.profileSmsMessage;}

    public void setProfileVoiceMailPath(String profileVoiceMailPath){this.profileVoiceMailPath = profileVoiceMailPath;}
    public String getProfileVoiceMailPath(){return this.profileVoiceMailPath;}

    public void setProfileSmsImagePath(String profileSmsImagePath){this.profileSmsImagePath = profileSmsImagePath;}
    public String getProfileSmsImagePath(){return this.profileSmsImagePath;}
}
