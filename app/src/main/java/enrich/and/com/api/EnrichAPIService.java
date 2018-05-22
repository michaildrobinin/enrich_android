package enrich.and.com.api;

import enrich.and.com.models.CardImage;
import enrich.and.com.models.ContactModel;
import enrich.and.com.models.SettingInfo;
import enrich.and.com.models.SmsMsgTestNowRequestBody;
import enrich.and.com.models.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface EnrichAPIService {
    @GET("/processes/processes/mobile.php?")
    Observable<LoginResponse> login(@Query("username") String userName , @Query("password") String password);

    @GET("/enrich_mobile/WzWpdatatable4/getSettingsNew/{userid}")
    Observable<SettingResponse> getSettingInfo(@Path("userid") String uid);

    @POST("/enrich_mobile/WzWpdatatable4/updateSettingNew")
    Observable<SettingResponse> updateSettingInfo(@Body SettingInfo settingInfo);

    @GET("/enrich_mobile/DapUsers/getAvailableCardsTest/{userid}")
    Observable<GetMyContactsResponse> getMyContacts(@Path("userid") String uid);

    @Multipart
    @POST("user/updateprofile")
    Observable<ResponseBody> uploadAudioFile(@Part MultipartBody.Part audioFile);

    @POST("/hooks/catch/772315/t2hhp8/")
    Call<ResponseBody> sendSmsMsgTestNow(@Body SmsMsgTestNowRequestBody body);

    @POST("/enrich_mobile/HtImages/addNewImage")
    Observable<CardImageResponse> addCardImage(@Body CardImage user);

    @POST("/enrich_mobile/HtJobs/addNewContact")
    Observable<AddUpdateContactResponse> addUpdateContact(@Body ContactModel model);

    @GET("/pages/upload_from_bucket.php?")
    Call<ResponseBody> sendTranscription(@Query("Message") String message);

    @GET("/private/setting/pages/activate_mvm_campaign.php?")
    Call<ResponseBody> activateMVMCampaign(@Query("user_id") String userId , @Query("audio_filename") String audioFileName);

    @GET("/enrich_mobile/HtJobs/deleteContact/{deleteIndexString}")
    Observable<DeleteContactResponse> deleteContacts(@Path("deleteIndexString") String indexes);

    @GET("/{account_id}/{fileName}")
    Call<ResponseBody> downloadAudioFileUrlAsync(@Path("account_id") String strAccountId , @Path("fileName") String fileName);

    @GET("/enrich-scanner/{fileName}")
    Call<ResponseBody> downloadScannerImageFileUrlAsync(@Path("fileName") String fileName);

}