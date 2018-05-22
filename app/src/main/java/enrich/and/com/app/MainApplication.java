package enrich.and.com.app;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import enrich.and.com.database.MySharedPreferenceManager;
import enrich.and.com.models.ContactInfo;
import enrich.and.com.models.Product;
import enrich.and.com.models.SettingInfo;
import enrich.and.com.models.UserInfo;
import enrich.and.com.models.UserProfile;
import enrich.and.com.utils.FragmentChanger;


public class MainApplication extends Application {

    private static MainApplication mInstance;
    private static Context mContext;
    private FragmentChanger m_fragChanger;

    private static UserInfo currentUserInfo;
    private static ContactInfo currentContactInfo;
    private static SettingInfo currentSettingInfo;

    private ArrayList<UserProfile> profiles = new ArrayList<UserProfile>();
    private List<Product> products = new ArrayList<Product>();

    private String currentPhotoPath = "";

    public static MainApplication getInstance()
    {
        return mInstance;
    }

    public static Context getAppContext()
    {
        return mContext;
    }

    private String strUserIdHash = "";

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mContext = getApplicationContext();

        m_fragChanger = FragmentChanger.getInstance();

    }

    public String getUserIdHash()
    {
        return this.strUserIdHash;
    }

    public void setUserIdHash(String userIdHash)
    {this.strUserIdHash = userIdHash;}


    public FragmentChanger getFragmentChanger() {
        return m_fragChanger;
    }

    public void setCurrentContactInfo(ContactInfo contactInfo)
    {
        currentContactInfo = contactInfo;
    }
    public ContactInfo getCurrentContactInfo()
    {
        return this.currentContactInfo;
    }

    public void setCurrentUserInfo(UserInfo userInfo)
    {
        this.currentUserInfo = userInfo;
    }
    public UserInfo getCurrentUserInfo()
    {
        return this.currentUserInfo;
    }

    public void setCurrentSettingInfo(SettingInfo settingInfo){
        currentSettingInfo = settingInfo;
        profiles = new ArrayList<UserProfile>();
        UserProfile profile1 = new UserProfile(1 ,
                settingInfo.getProfilenamep1() ,
                settingInfo.getProfiledescp1() ,
                settingInfo.getSmsimagep1(),
                settingInfo.getSmsmessagep1() ,
                settingInfo.getMvm1p1() ,
                settingInfo.getSmsimagep1());
        UserProfile profile2 = new UserProfile(2 ,
                settingInfo.getProfilenamep2() ,
                settingInfo.getProfiledescp2() ,
                settingInfo.getSmsimagep2(),
                settingInfo.getSmsmessagep2() ,
                settingInfo.getMvm1p2() ,
                settingInfo.getSmsimagep2());
        UserProfile profile3 = new UserProfile(3 ,
                settingInfo.getProfilenamep3() ,
                settingInfo.getProfiledescp3() ,
                settingInfo.getSmsimagep3(),
                settingInfo.getSmsmessagep3() ,
                settingInfo.getMvm1p3() ,
                settingInfo.getSmsimagep3());

        profiles.add(profile1);
        profiles.add(profile2);
        profiles.add(profile3);
    }
    public SettingInfo getCurrentSettingInfo()
    {
        return this.currentSettingInfo;
    }

    public String getCurrentPhotoPath(){return this.currentPhotoPath;}
    public void setCurrentPhotoPath(String photoPath){this.currentPhotoPath = photoPath;}

    public ArrayList<UserProfile> getProfileList(){return this.profiles;}
    public UserProfile getProfileByIndex(int index){return this.profiles.get(index);}


    public List<Product> getProductsList(){return this.products;}
    public void setProductsList(List<Product> products){this.products = products;}


    public int getCurrentProfileIndex(){
        MySharedPreferenceManager sf = new MySharedPreferenceManager(this);
        return sf.getValueInt(MySharedPreferenceManager.ProfileIndex , 0);
    }
    public void setCurrentProfileIndex(int index)
    {
        MySharedPreferenceManager sf = new MySharedPreferenceManager(this);
        sf.setValueInt(MySharedPreferenceManager.ProfileIndex , index);
    }

}
