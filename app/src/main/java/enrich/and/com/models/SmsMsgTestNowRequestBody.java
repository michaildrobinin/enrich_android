package enrich.and.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SmsMsgTestNowRequestBody implements Serializable{
    public String userid;
    public String profile;

    public SmsMsgTestNowRequestBody()
    {

    }

    public void setUserid(String id){this.userid = id;}
    public String getUserid(){return this.userid;}

    public void setProfile(String profileId){this.profile = profileId;}
    public String getProfile(){return this.profile;}
}
