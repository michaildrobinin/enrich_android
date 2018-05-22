package enrich.and.com.api;

import java.util.Map;

import enrich.and.com.models.CardImage;
import enrich.and.com.models.ContactModel;
import enrich.and.com.models.SettingInfo;
import enrich.and.com.models.SmsMsgTestNowRequestBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface FileUploadService {
    @Multipart
    @POST("/upload")//@POST("/141bw881")//
    Call<ResponseBody> uploadAudioFile(@Part MultipartBody.Part file, @PartMap() Map<String, RequestBody> partMa);
    /*Call<ResponseBody> uploadAudioFile(@Part MultipartBody.Part file, @Query("account_id") String accountid,
                                       @Query("authhash") String authhash ,
                                       @Query("audioFile") String audioFileName);*/

    /*@Multipart
    @POST("/1h0d75i1")
    Call<ResponseBody> uploadAudioFile(@Part("file") MultipartBody.Part file, @Query("account_id") String accountid,
                                       @Query("authhash") String authhash ,
                                       @Query("audioFile") String audioFileName);*/

}