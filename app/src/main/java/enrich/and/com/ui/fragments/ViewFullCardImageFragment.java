package enrich.and.com.ui.fragments;

 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.os.AsyncTask;
 import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
 import android.util.Log;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
import android.widget.TextView;

 import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
 import com.amazonaws.services.s3.model.CannedAccessControlList;
 import com.squareup.picasso.Picasso;

 import java.io.File;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.List;

 import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
 import enrich.and.com.api.APIManager;
 import enrich.and.com.api.CardImageResponse;
 import enrich.and.com.api.EnrichAPIService;
 import enrich.and.com.api.GetMyContactsResponse;
 import enrich.and.com.app.AWSConstants;
 import enrich.and.com.app.AWSUtil;
 import enrich.and.com.app.AppConstants;
 import enrich.and.com.app.MainApplication;
 import enrich.and.com.interfaces.OnCurrentProfileBarCollapseListener;
import enrich.and.com.interfaces.OnProfileItemSelectedListener;
 import enrich.and.com.models.CardImage;
 import enrich.and.com.models.ContactInfo;
 import enrich.and.com.models.ContactModel;
 import enrich.and.com.ui.EditMagicVoicemailActivity;
import enrich.and.com.ui.EditSMSImageActivity;
import enrich.and.com.ui.EditSMSMessageActivity;
import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.ui.customviews.ProfileSelectorItemView;
 import enrich.and.com.utils.ImagePicker;
 import enrich.and.com.utils.util;
 import okhttp3.ResponseBody;
 import retrofit2.Call;
 import retrofit2.Callback;
 import retrofit2.HttpException;
 import retrofit2.Response;
 import retrofit2.Retrofit;
 import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
 import rx.Observable;
 import rx.Subscriber;
 import rx.Subscription;
 import rx.android.schedulers.AndroidSchedulers;
 import rx.schedulers.Schedulers;

public class ViewFullCardImageFragment extends BaseFragment{

    @BindView(R.id.txtHeaderTitle)
    TextView txtHeaderTitle;

    @BindView(R.id.imgCardImage)
    ImageView imgCardImage;

    private boolean isFrontCard = true;
    private String photoPath = "";

    private String strCurrentPhotoFilePath = "";

    private Bitmap currentBitmap = null;

    //----------- Amazon S3 Upload--------------------------------------//
    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;

    private EnrichAPIService apiService;


    public ViewFullCardImageFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.fragment_full_view_card_screen, container, false);
            ButterKnife.bind(this, m_rootView);

        }

        // Initializes TransferUtility, always do this before using it.
        transferUtility = AWSUtil.getTransferUtility(getContainer());

        isFrontCard = getMyArguments().getBoolean("isFrontCard" , true);
        photoPath = getMyArguments().getString("photoPath" , "");



        if(isFrontCard)
            txtHeaderTitle.setText(getResources().getString(R.string.str_front_image));
        else
            txtHeaderTitle.setText(getResources().getString(R.string.str_back_image));



        return m_rootView;
    }

    private void loadPhotoFile()
    {
        if (photoPath.startsWith("/")) {
            strCurrentPhotoFilePath = photoPath;
            File photoFile = new File(strCurrentPhotoFilePath);
            if(photoFile.exists())
                currentBitmap = util.decodeFile(photoFile , 2000 , 2000);
            else
                currentBitmap = null;

            updateUi();

        }
        else//download image file from server
        {
            String fileName = photoPath.substring(photoPath.lastIndexOf("/")+1);
            getContainer().showProgressDialog("" , true , null);
            downloadImageFile(fileName);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUi()
    {
        //util.showCardImageViewWithPicasso(getContext() , photoPath , imgCardImage);
        if(currentBitmap != null)
        {
            imgCardImage.setImageBitmap(currentBitmap);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadPhotoFile();
    }

    @Override
    public void onPause() {
        super.onPause();
        imgCardImage.setImageBitmap(null);
        if(currentBitmap != null)
        {
            try {
                currentBitmap.recycle();
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                currentBitmap = null;
            }
        }
    }

    @OnClick(R.id.btnCancel)
    public void onClickButtonCancel()
    {
        onBackPressed();
    }

    @OnClick(R.id.btnDone)
    public void onClickButtonDone()
    {
        getContainer().showProgressDialog("" , false , null);
        new SaveRotatedImageTask().execute();
    }

    @OnClick(R.id.btnCamera)
    public void onClickButtonCamera()
    {
        onBackPressed();
        MainApplication.getInstance().getFragmentChanger().showCameraFragment(isFrontCard);
    }

    @OnClick(R.id.btnRotateImage)
    public void onClickButtonRotateImage()
    {
        if(currentBitmap == null)
            return;

        currentBitmap = ImagePicker.rotate(currentBitmap, 90);
        imgCardImage.setImageBitmap(currentBitmap);
        imgCardImage.invalidate();
    }

    class SaveRotatedImageTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            if(currentBitmap == null)
                return null;
            SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
            String strTime = format.format(Calendar.getInstance().getTime());

            String fileName = strTime.replace(' ', '_').replace(":", "_") + ".jpg";
            String strNewImageFileFullPath = util.saveImageToExternalStorage(currentBitmap, fileName);

            return strNewImageFileFullPath;
        }

        @Override
        protected void onPostExecute(String strResultPath) {
            getContainer().hideProgressDialog();
            if(strResultPath != null) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd'th'-HH:mm yyyy");
                String capturedDateTime = dateFormat.format(calendar.getTime());
                if(isFrontCard) {
                    CardImage frontCardImage = MainApplication.getInstance().getCurrentContactInfo().frontCardImage;
                    if(MainApplication.getInstance().getCurrentContactInfo().contact.getId()>0)
                    {
                        if(frontCardImage.getId()>0)
                        {
                            //upload and save
                            uploadImageFile(strResultPath , MainApplication.getInstance().getCurrentContactInfo().generateImageFileName(frontCardImage));
                        }
                        else
                        {
                            uploadImageFile(strResultPath , MainApplication.getInstance().getCurrentContactInfo().generateImageFileName(frontCardImage));
                        }
                    }
                    else
                    {
                        MainApplication.getInstance().getCurrentContactInfo().setFrontCardImagePath(strResultPath);
                        onBackPressed();
                    }

                }
                else {
                    CardImage backCardImage = MainApplication.getInstance().getCurrentContactInfo().backCardImage;
                    if(MainApplication.getInstance().getCurrentContactInfo().contact.getId()>0)
                    {
                        if(MainApplication.getInstance().getCurrentContactInfo().backCardImage.getId()>0)
                        {
                            //upload and save
                            uploadImageFile(strResultPath , MainApplication.getInstance().getCurrentContactInfo().generateImageFileName(backCardImage));
                        }
                        else
                        {
                            uploadImageFile(strResultPath , MainApplication.getInstance().getCurrentContactInfo().generateImageFileName(backCardImage));

                        }
                    }
                    else
                    {
                        MainApplication.getInstance().getCurrentContactInfo().setBackCardImagePath(strResultPath);
                        onBackPressed();
                    }

                }

            }
        }
    };
    TransferListener imageTransferListener = new TransferListener() {
        @Override
        public void onStateChanged(int id, TransferState state) {

            if(state.toString().compareTo("COMPLETED") == 0)
            {
                System.out.println("--------"+state.toString());
                CardImage image = null;
                if(isFrontCard)
                    image = MainApplication.getInstance().getCurrentContactInfo().frontCardImage;
                else
                    image = MainApplication.getInstance().getCurrentContactInfo().backCardImage;
                getContainer().hideProgressDialog();
                addUpdateCardImageUrl(image);
            }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

        }

        @Override
        public void onError(int id, Exception ex) {
            getContainer().hideProgressDialog();
            getContainer().showErrorMessage("Error" , "Failed to Upload image" , null);
        }
    };

    private void addUpdateCardImageUrl(final CardImage image)
    {
        final CardImage uploadImg = new CardImage(image);
        String keyName = MainApplication.getInstance().getCurrentContactInfo().generateImageFileName(uploadImg);
        String downloableUrl = AppConstants.API_CARD_IMAGE_FILE_BASE_URL+keyName;
        uploadImg.setKeyName(keyName);
        uploadImg.setImageUrl(downloableUrl);

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<CardImageResponse>().createGsonConverter(CardImageResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        getContainer().showProgressDialog("" , false , null);
        Observable<CardImageResponse> call = apiService.addCardImage(uploadImg);
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CardImageResponse>() {
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

                        }
                        getContainer().hideProgressDialog();
                        getContainer().showErrorMessage("Error", "Failed to upload image" , null);
                    }

                    @Override
                    public void onNext(CardImageResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            getContainer().hideProgressDialog();

                            return;
                        }


                        image.setInfo(response.getData());

                        getContainer().hideProgressDialog();
                        onBackPressed();
                    }
                });
    }

    private void uploadImageFile(String filePath , String uploadFileName)
    {
        File file = new File(filePath);
        getContainer().showProgressDialog("Uploading Image..." , false , null);

        TransferObserver observer = transferUtility.upload(AWSConstants.SCANNER_BUCKET_NAME, uploadFileName,
                file, CannedAccessControlList.PublicRead);
        observer.setTransferListener(imageTransferListener);
    }

    private void downloadImageFile(final String fileName)
    {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://s3.amazonaws.com")
                .addCallAdapterFactory(rxAdapter)
                .build();
        EnrichAPIService downloadService = retrofit.create(EnrichAPIService.class);

        getContainer().showProgressDialog("Downloading Image..." , false , null);

        Call<ResponseBody> call = downloadService.downloadScannerImageFileUrlAsync(fileName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    boolean writtenToDisk = util.writeResponseBodyToDisk(response, util.SCANNERS_FOLDER_NAME, fileName);

                    if(writtenToDisk)
                    {
                        strCurrentPhotoFilePath = util.getAppUsingFilePath(util.SCANNERS_FOLDER_NAME , fileName);
                        File photoFile = new File(strCurrentPhotoFilePath);
                        if(photoFile.exists())
                            currentBitmap = util.decodeFile(photoFile , 2000 , 2000);
                        else
                            currentBitmap = null;

                        updateUi();
                    }

                    Log.d("Download Card Image", "file download was a success? " + writtenToDisk);
                }
                getContainer().hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                getContainer().hideProgressDialog();
                currentBitmap = null;

                updateUi();
            }

        });

    }
}
