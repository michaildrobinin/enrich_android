package enrich.and.com.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.api.APIManager;
import enrich.and.com.api.EnrichAPIService;
import enrich.and.com.api.SettingResponse;
import enrich.and.com.app.AWSConstants;
import enrich.and.com.app.AWSUtil;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.models.SettingInfo;
import enrich.and.com.models.UserProfile;
import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.utils.ImagePicker;
import enrich.and.com.utils.util;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditSMSImageActivity extends BaseActivity{

    @BindView(R.id.currentProfileBarView)
    CurrentProfileBarView currentProfileBarView;

    @BindView(R.id.imgSmsImage)
    ImageView imgSmsImage;

    @BindView(R.id.imgRotateImage)
    ImageView imgRotateImage;

    private int nSelectedProfileIndex = 0;
    private UserProfile profile;

    private String strNewImageFileName = "";
    private String strNewImageFileFullPath = "";
    private String uploadedImageFileUrl = "";
    private String loadedImageFilePath = "";

    private Bitmap newImageBitmap = null;


    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    private static final int CROP_IMAGE_ID = 134; // the number doesn't matter


    //----------- Amazon S3 Upload--------------------------------------//
    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;

    private EnrichAPIService apiService;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_sms_image);
        ButterKnife.bind(this);

        imgRotateImage.setVisibility(View.GONE);

        nSelectedProfileIndex = MainApplication.getInstance().getCurrentProfileIndex();
        profile = MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex);

        currentProfileBarView.setProfile(profile);

        // Initializes TransferUtility, always do this before using it.
        transferUtility = AWSUtil.getTransferUtility(this);

    }

    @OnClick(R.id.imgSmsImage)
    public void onClickImageToTakePicture()
    {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @OnClick(R.id.imgRotateImage)
    public void onClickcButtonRotateImage()
    {
        if(strNewImageFileName.equalsIgnoreCase(""))
            return;
        if(newImageBitmap == null)
            return;

        newImageBitmap = ImagePicker.rotate(newImageBitmap , 90);
        imgSmsImage.setImageBitmap(newImageBitmap);
        imgSmsImage.invalidate();
    }

    @OnClick(R.id.btnUploadNewImage)
    public void onClickUploadImageButton()
    {
        if(strNewImageFileName.equalsIgnoreCase(""))
            return;
        if(newImageBitmap == null)
            return;
        strNewImageFileFullPath = "";
        uploadedImageFileUrl = "";
        showProgressDialog("" , false , null);
        new BitmapSaveTask().execute();
    }

    class BitmapSaveTask extends AsyncTask<Void , Void , Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params) {
            //strNewImageFileFullPath = util.saveImageToExternalStorage(newImageBitmap, strNewImageFileName);

            float rate = (float)250/newImageBitmap.getWidth();
            int thumbHeight = (int)(rate*newImageBitmap.getHeight());

            Bitmap thumnailBitmap = ThumbnailUtils.extractThumbnail(newImageBitmap , 250, thumbHeight);
            strNewImageFileFullPath = util.saveImageToExternalStorage(thumnailBitmap, strNewImageFileName);
            if(strNewImageFileFullPath.equalsIgnoreCase(""))
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result)
            {
                showErrorMessage("Failed to save image" , "Failed to Save Image , try again....");
                hideProgressDialog();
                return;
            }

            System.out.println("------" + strNewImageFileFullPath+"------");

            hideProgressDialog();
            uploadProfileImage(strNewImageFileFullPath);
        }
    };

    private void uploadProfileImage(String strImgFilePath)
    {
        this.uploadedImageFileUrl = "";
        File file = new File(strImgFilePath);
        showProgressDialog("Uploading..." , false , null);
        TransferObserver observer = transferUtility.upload(AWSConstants.BUCKET_NAME, "app-images/"+file.getName(),
                file, CannedAccessControlList.PublicRead);
        observer.setTransferListener(transferListener);

    }

    TransferListener transferListener = new TransferListener() {
        @Override
        public void onStateChanged(int id, TransferState state) {

            if(state.toString().compareTo("COMPLETED") == 0)
            {
                System.out.println("--------"+state.toString());
                uploadedImageFileUrl = AppConstants.API_PROFILE_IMAGE_FILE_BASE_URL+strNewImageFileName;
                System.out.println("--------"+uploadedImageFileUrl);
                hideProgressDialog();

                if(uploadedImageFileUrl.equalsIgnoreCase(""))
                {
                    EditSMSImageActivity.this.finish();
                    return;
                }

                SettingInfo newSetting = new SettingInfo(MainApplication.getInstance().getCurrentSettingInfo());

                switch(nSelectedProfileIndex)
                {
                    case 0:
                        newSetting.setSmsimagep1(uploadedImageFileUrl);
                        newSetting.setImgsmsp1(uploadedImageFileUrl);
                        break;
                    case 1:
                        newSetting.setSmsimagep2(uploadedImageFileUrl);
                        newSetting.setImgsmsp2(uploadedImageFileUrl);
                        break;
                    case 2:
                        newSetting.setSmsimagep3(uploadedImageFileUrl);
                        newSetting.setImgsmsp3(uploadedImageFileUrl);
                        break;
                }

                saveUserSettings(newSetting);
            }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            System.out.println("--------"+bytesCurrent + "/"+bytesTotal);

        }

        @Override
        public void onError(int id, Exception ex) {
            System.out.println("--------Error ="+ex.toString());
            hideProgressDialog();
            showErrorMessage("Failed to upload","Failed to upload image file to server...");
        }
    };

    @OnClick(R.id.btnSave)
    public void onClickSaveButton()
    {
        //upload file to s3 bucket
        if(strNewImageFileName.equalsIgnoreCase(""))
            return;
        if(newImageBitmap == null)
            return;
        strNewImageFileFullPath = "";
        uploadedImageFileUrl = "";
        showProgressDialog("" , false , null);
        new BitmapSaveTask().execute();


    }

    @Override
    public void onResume() {
        super.onResume();
        if(loadedImageFilePath.equalsIgnoreCase(""))
        {
            if(profile.getProfileSmsImagePath().equalsIgnoreCase(""))
            {
                imgSmsImage.setImageResource(R.drawable.gray_border_rectangle);
            }
            else
            {
                Picasso.with(EditSMSImageActivity.this).load(profile.getProfileSmsImagePath()).placeholder(R.drawable.gray_border_rectangle).into(imgSmsImage);
            }
        }
        else
        {
            File file = new File(loadedImageFilePath);
            newImageBitmap = util.decodeFile(file , 1000 , 1000);
            if(newImageBitmap == null)
            {
                loadedImageFilePath = "";
                strNewImageFileName = "";
            }
            else
            {
                imgSmsImage.setImageBitmap(newImageBitmap);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        imgSmsImage.setImageBitmap(null);
        if(newImageBitmap != null)
        {
            try {
                newImageBitmap.recycle();
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                newImageBitmap = null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                if(resultCode == RESULT_OK)
                {
                    loadedImageFilePath = ImagePicker.getImageFullPath(this , resultCode , data);
                    Intent intent = new Intent(EditSMSImageActivity.this , SmsImageCropActivity.class);
                    intent.putExtra("imagepath" , loadedImageFilePath);
                    startActivityForResult(intent , CROP_IMAGE_ID);
                }
                // TODO use bitmap
                break;
            case CROP_IMAGE_ID:
                if(resultCode == RESULT_OK && data!=null)
                {
                    String filePath = data.getStringExtra("imagepath");
                    loadedImageFilePath = filePath;
                    newImageBitmap = util.decodeFile(new File(filePath) , 1000 , 1000);
                    if(newImageBitmap != null) {
                        SimpleDateFormat fmtOut = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
                        String date = fmtOut.format(Calendar.getInstance().getTime());
                        strNewImageFileName = "enRICH"+date+"_id"+MainApplication.getInstance().getCurrentUserInfo().getId()+"_p"+profile.getProfileIndex()+".jpg";

                        imgRotateImage.setVisibility(View.VISIBLE);
                        imgSmsImage.setImageBitmap(newImageBitmap);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void saveUserSettings(SettingInfo settingInfo)
    {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<SettingResponse>().createGsonConverter(SettingResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        showProgressDialog("" , false , null);
        Observable<SettingResponse> call = apiService.updateSettingInfo(settingInfo);
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SettingResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException)e;
                            Response response = exception.response();
                            String errorMsg = response.message();
                            showErrorMessage("Error" , errorMsg , null);
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(SettingResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            hideProgressDialog();
                            showErrorMessage("Error" , "Failed to save settings." , null);
                            return;
                        }
                        SettingInfo setting = response.getData();
                        MainApplication.getInstance().setCurrentSettingInfo(setting);
                        hideProgressDialog();
                        EditSMSImageActivity.this.finish();
                    }
                });
    }
}
