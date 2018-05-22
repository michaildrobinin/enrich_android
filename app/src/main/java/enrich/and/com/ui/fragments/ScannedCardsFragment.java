package enrich.and.com.ui.fragments;

 import android.content.Intent;
import android.os.Bundle;
 import android.os.Handler;
 import android.support.v7.widget.CardView;
 import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
import android.widget.TextView;

 import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
 import com.amazonaws.services.s3.model.CannedAccessControlList;
 import com.squareup.picasso.Picasso;

 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 import java.io.File;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.StringTokenizer;

 import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
 import enrich.and.com.api.APIManager;
 import enrich.and.com.api.AddUpdateContactResponse;
 import enrich.and.com.api.CardImageResponse;
 import enrich.and.com.api.EnrichAPIService;
 import enrich.and.com.app.AWSConstants;
 import enrich.and.com.app.AWSUtil;
 import enrich.and.com.app.AppConstants;
 import enrich.and.com.app.MainApplication;
 import enrich.and.com.interfaces.OnCurrentProfileBarCollapseListener;
import enrich.and.com.interfaces.OnProfileItemSelectedListener;
 import enrich.and.com.models.CardImage;
 import enrich.and.com.models.ContactInfo;
 import enrich.and.com.models.ContactModel;
 import enrich.and.com.ui.AddMyContactActivity;
 import enrich.and.com.ui.EditMagicVoicemailActivity;
import enrich.and.com.ui.EditSMSImageActivity;
import enrich.and.com.ui.EditSMSMessageActivity;
import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.ui.customviews.ProfileSelectorItemView;
 import enrich.and.com.utils.util;
 import okhttp3.ResponseBody;
 import retrofit2.Call;
 import retrofit2.Callback;
 import retrofit2.HttpException;
 import retrofit2.Response;
 import retrofit2.Retrofit;
 import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
 import retrofit2.converter.gson.GsonConverterFactory;
 import rx.Observable;
 import rx.Subscriber;
 import rx.Subscription;
 import rx.android.schedulers.AndroidSchedulers;
 import rx.schedulers.Schedulers;

public class ScannedCardsFragment extends BaseFragment implements OnCurrentProfileBarCollapseListener ,OnProfileItemSelectedListener {

    @BindView(R.id.currentProfileBarView)
    CurrentProfileBarView currentProfileBarView;

    @BindView(R.id.profileListLayout)
    LinearLayout profileListLayout;

    @BindView(R.id.profileSelectorDivider)
    View profileSelectorDivider;

    @BindView(R.id.edtEnrichTags)
    EditText edtEnrichTags;

    @BindView(R.id.edtEnrichNotes)
    EditText edtEnrichNotes;

    @BindView(R.id.imgFrontCard)
    ImageView imgFrontCard;

    @BindView(R.id.imgBackCard)
    ImageView imgBackCard;

    @BindView(R.id.frontCardLayout)
    CardView frontCardLayout;

    @BindView(R.id.backCardLayout)
    CardView backCardLayout;


    private ContactInfo contactInfo;

    ArrayList<ProfileSelectorItemView> profileItemsList = new ArrayList<>();

    private int nSelectedProfileIndex = 0;

    private EnrichAPIService apiService;


    private String strUploadingFrontCardImageFileName = "";
    private String strUploadingBackCardImageFileName = "";

    //----------- Amazon S3 Upload--------------------------------------//
    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;

    public ScannedCardsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactInfo = MainApplication.getInstance().getCurrentContactInfo();
        if(contactInfo == null)
        {
            contactInfo = new ContactInfo();
            MainApplication.getInstance().setCurrentContactInfo(contactInfo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.fragment_cards, container, false);
            ButterKnife.bind(this, m_rootView);
            currentProfileBarView.setOnCurrentProfileBarCollapseListener(this);

            profileItemsList = new ArrayList<ProfileSelectorItemView>();

            profileItemsList.add(new ProfileSelectorItemView(getContainer() , 0 , MainApplication.getInstance().getProfileByIndex(0), this));
            profileItemsList.add(new ProfileSelectorItemView(getContainer() , 1 , MainApplication.getInstance().getProfileByIndex(1) , this));
            profileItemsList.add(new ProfileSelectorItemView(getContainer() , 2 , MainApplication.getInstance().getProfileByIndex(2) , this));
        }

        nSelectedProfileIndex = MainApplication.getInstance().getCurrentProfileIndex();
        currentProfileBarView.setProfile(MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex));


        profileListLayout.removeAllViews();
        for(int i=0;i<profileItemsList.size(); i++)
        {
            profileItemsList.get(i).showViewValues();
            profileListLayout.addView(profileItemsList.get(i));
        }

        // Initializes TransferUtility, always do this before using it.
        transferUtility = AWSUtil.getTransferUtility(getContainer());

        updateUi();

        updateCheckboxes();

        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCardImageViews();
        updateCheckboxes();
    }

    private void updateUi()
    {
        if(currentProfileBarView.isCollapsed()) {
            profileListLayout.setVisibility(View.GONE);
            profileSelectorDivider.setVisibility(View.GONE);
        }
        else {
            profileListLayout.setVisibility(View.VISIBLE);
            profileSelectorDivider.setVisibility(View.VISIBLE);
        }


    }

    private void updateCardImageViews()
    {
        if(contactInfo == null) return;

        util.showCardImageViewWithPicasso(getContainer() , contactInfo.getFrontCardImagePath() , imgFrontCard);
        util.showCardImageViewWithPicasso(getContainer() , contactInfo.getBackCardImagePath() , imgBackCard);

    }

    private void updateCheckboxes()
    {
        for(int i = 0;i < profileItemsList.size(); i++)
        {
            if(nSelectedProfileIndex == i)
                profileItemsList.get(i).updateCheckBox(true);
            else
                profileItemsList.get(i).updateCheckBox(false);
        }
    }

    @OnClick(R.id.btnMenu)
    public void onClickMenu()
    {
        getContainer().onSlidingMenuClicked();
    }


    @Override
    public void onProfileListCollapsed(boolean isCollapsed) {
        if(isCollapsed) {
            profileListLayout.setVisibility(View.GONE);
            profileSelectorDivider.setVisibility(View.GONE);
        }
        else {
            profileListLayout.setVisibility(View.VISIBLE);
            profileSelectorDivider.setVisibility(View.VISIBLE);
        }
    }

    //Profile Selected
    @Override
    public void onSelected(int index) {
        nSelectedProfileIndex = index;
        updateCheckboxes();
        currentProfileBarView.setCollapsed(true);
        onProfileListCollapsed(true);
        currentProfileBarView.setProfile(MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex));
    }

    @OnClick(R.id.btnSubmit)
    public void onClickSubmitButton()
    {
        //contactInfo.contact.setTags(edtEnrichTags.getText().toString().trim());
        //contactInfo.contact.setNotes(edtEnrichNotes.getText().toString().trim());
        //addUpdateContact(contactInfo);

        strUploadingFrontCardImageFileName = "";
        strUploadingBackCardImageFileName = "";

        if(contactInfo.frontCardImage.getImageUrl().startsWith("/"))//local file, then upload
        {
            uploadCardImageFile(true , contactInfo.frontCardImage.getImageUrl());
        }
        else
        {
            if(contactInfo.backCardImage.getImageUrl().startsWith("/")) //local file, then upload
            {
                uploadCardImageFile(false , contactInfo.backCardImage.getImageUrl());
            }
            else
            {
                //sendTranscription();
            }
        }
    }

    @OnClick(R.id.btnCancel)
    public void onClickCancelButton()
    {
        onBackPressed();
    }


    @OnClick(R.id.frontCardLayout)
    public void onClickFrontCardLayout()
    {
        //if card image is empty then goes to camera screen
        if(contactInfo.getFrontCardImagePath().equalsIgnoreCase(""))
        {
            MainApplication.getInstance().getFragmentChanger().showCameraFragment(true);
        }
        else
        {
            MainApplication.getInstance().getFragmentChanger().showViewFullImageCardFragment(true , contactInfo.getFrontCardImagePath());
        }
    }

    @OnClick(R.id.backCardLayout)
    public void onClickBackCardLayout()
    {
        //if card image is empty then goes to camera screen
        if(contactInfo.getBackCardImagePath().equalsIgnoreCase(""))
        {
            MainApplication.getInstance().getFragmentChanger().showCameraFragment(false);
        }
        else
        {
            MainApplication.getInstance().getFragmentChanger().showViewFullImageCardFragment(false, contactInfo.getBackCardImagePath());
        }
    }

    private void sendTranscription()
    {
        String strTags = edtEnrichTags.getText().toString();
        String strNotes = edtEnrichNotes.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TRANSCRIPTION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        getContainer().showProgressDialog("Transcribing..." , false ,null);
        JSONObject jsonMsg = new JSONObject();
        try {
            jsonMsg.put("client_id" , String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId()));
            jsonMsg.put("profile_id" , String.valueOf(nSelectedProfileIndex+1));
            jsonMsg.put("uploader_email" , MainApplication.getInstance().getCurrentUserInfo().getEmail());
            jsonMsg.put("bucket" , AWSConstants.SCANNER_BUCKET_NAME);
            jsonMsg.put("side_a_key" , strUploadingFrontCardImageFileName);
            if(strUploadingBackCardImageFileName.equalsIgnoreCase(""))
                jsonMsg.put("side_b_key" , "no_card_image.jpg");
            else
                jsonMsg.put("side_b_key" , strUploadingBackCardImageFileName);
            jsonMsg.put("notes" , strNotes);
            JSONArray tagsArray = new JSONArray();
            StringTokenizer tokenizer = new StringTokenizer(strTags , ",");
            while(tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();
                if(!token.trim().equalsIgnoreCase(""))
                    tagsArray.put(token);
            }

            jsonMsg.put("tags" , tagsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> result = apiService.sendTranscription(jsonMsg.toString());
        //Call<ResponseBody> result = apiService.sendTranscription("{\"client_id\":\"will\",\"profile_id\":\"w2\",\"uploader_email\":\"willwoodlief@gmail.com\",\"bucket\":\"enrich-scanner\",\"side_a_key\":\"img1a_id1_p2_20161004.jpg\",\"side_b_key\":\"img2a_id1_p1_20161005.jpg\",\"notes\":\"this is an example json used to send in the body of the sns, here is it filled with some data\",\"tags\":[\"important!\",\"New Lead\"]}");

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getContainer().hideProgressDialog();
                try {

                    String strResult = response.body().toString();
                    System.out.println("----------"+strResult+"-------------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler(getContainer().getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getContainer().hideProgressDialog();
                getContainer().showErrorMessage("Error" , "Failed to send transcription request!");
                t.printStackTrace();
            }

        });

    }
    private void addUpdateContact(final ContactInfo contactInfo)
    {

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<AddUpdateContactResponse>().createGsonConverter(AddUpdateContactResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        getContainer().showProgressDialog("" , false , null);
        Observable<AddUpdateContactResponse> call = apiService.addUpdateContact(contactInfo.contact);
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AddUpdateContactResponse>() {
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
                            getContainer().showErrorMessage("Error" , "Failed to submit new contact." ,null);
                        }
                        getContainer().hideProgressDialog();
                    }

                    @Override
                    public void onNext(AddUpdateContactResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            getContainer().hideProgressDialog();
                            getContainer().showErrorMessage("Error" , "Failed to submit new contact." ,null);
                            return;
                        }

                        ContactModel contact = (ContactModel)response.getData();
                        getContainer().hideProgressDialog();
                        contactInfo.contact = contact;
                        contactInfo.frontCardImage.setHtJobId(contact.getId());
                        contactInfo.backCardImage.setHtJobId(contact.getId());

                        if(contactInfo.frontCardImage.getImageUrl().startsWith("/"))//local file, then upload
                        {
                            uploadCardImageFile(true , contactInfo.frontCardImage.getImageUrl());
                        }
                        else
                        {
                            if(contactInfo.backCardImage.getImageUrl().startsWith("/")) //local file, then upload
                            {
                                uploadCardImageFile(false , contactInfo.backCardImage.getImageUrl());
                            }
                            else
                            {
                                goToMyContactsFragment();
                            }
                        }
                    }
                });
    }

    private void uploadCardImageFile(boolean isFrontCard, String imageFilePath)
    {
        File file = new File(imageFilePath);
        String uploadFileName = "";
        if(isFrontCard) {
            uploadFileName = contactInfo.generateImageFileName(contactInfo.frontCardImage);
            strUploadingFrontCardImageFileName = uploadFileName;

        }
        else {
            uploadFileName = contactInfo.generateImageFileName(contactInfo.backCardImage);
            strUploadingBackCardImageFileName = uploadFileName;
        }

        getContainer().showProgressDialog("Uploading Image..." , false , null);

        TransferObserver observer = transferUtility.upload(AWSConstants.SCANNER_BUCKET_NAME, uploadFileName,
                file, CannedAccessControlList.PublicRead);
        observer.setTransferListener(new ImageTransferListener(isFrontCard , uploadFileName));
    }

    private void addUpdateCardImageUrl(final boolean isFrontCard , final CardImage image , String uploadedFileName)
    {
        final CardImage uploadImg = new CardImage(image);

        String downloableUrl = AppConstants.API_CARD_IMAGE_FILE_BASE_URL+uploadedFileName;
        uploadImg.setKeyName(uploadedFileName);
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
                        if(isFrontCard)
                            contactInfo.frontCardImage = new CardImage(image);
                        else
                            contactInfo.backCardImage = new CardImage(image);

                        getContainer().hideProgressDialog();
                        if(isFrontCard && contactInfo.backCardImage.getImageUrl().startsWith("/"))//if back image is local file,then upload
                        {
                            uploadCardImageFile(false , contactInfo.backCardImage.getImageUrl());
                            return;
                        }
                        else if(isFrontCard)
                        {
                            goToMyContactsFragment();
                            return;
                        }
                        if(!isFrontCard)
                        {
                            goToMyContactsFragment();
                        }
                    }
                });
    }

    class ImageTransferListener implements TransferListener {
        private boolean isFrontCard = false;
        private String uploadedFileName = "";

        public ImageTransferListener(boolean isFrontCard , String uploadedFileName)
        {
            this.isFrontCard = isFrontCard;
            this.uploadedFileName = uploadedFileName;
        }

        @Override
        public void onStateChanged(int id, TransferState state) {

            if(state.toString().compareTo("COMPLETED") == 0)
            {
                System.out.println("--------"+state.toString());

                getContainer().hideProgressDialog();

                CardImage image = null;

                if(isFrontCard) {
                    image = MainApplication.getInstance().getCurrentContactInfo().frontCardImage;
                    if(contactInfo.backCardImage.getImageUrl().startsWith("/"))//if back image is local file,then upload
                    {
                        uploadCardImageFile(false , contactInfo.backCardImage.getImageUrl());
                        return;
                    }
                    sendTranscription();
                }
                else {
                    image = MainApplication.getInstance().getCurrentContactInfo().backCardImage;
                    sendTranscription();
                }
                //addUpdateCardImageUrl(isFrontCard , image , uploadedFileName);
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


    private void goToMyContactsFragment()
    {
        onBackPressed();
        MainApplication.getInstance().getFragmentChanger().showMyContactsFragment();
    }

}
