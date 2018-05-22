package enrich.and.com.ui.fragments;

 import android.Manifest;
 import android.content.ContentProviderOperation;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.provider.ContactsContract;
 import android.support.v4.app.ActivityCompat;
 import android.support.v4.content.ContextCompat;
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
 import java.util.ArrayList;

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
 import enrich.and.com.ui.EditCardDetailsActivity;
 import enrich.and.com.ui.EditMagicVoicemailActivity;
import enrich.and.com.ui.EditSMSImageActivity;
import enrich.and.com.ui.EditSMSMessageActivity;
 import enrich.and.com.ui.customviews.ContactInfoView;
 import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.ui.customviews.ProfileSelectorItemView;
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

public class CardDetailsFragment extends BaseFragment{

    private ContactInfo contactInfo;

    @BindView(R.id.contactInfoView)
    ContactInfoView contactInfoView;

    @BindView(R.id.imgViewHeaderCard)
    ImageView imgViewHeaderCard;

    @BindView(R.id.txtContactName)
    TextView txtContactName;

    @BindView(R.id.txtHeaderTitle)
    TextView txtHeaderTitle;

    @BindView(R.id.txtHeaderCompany)
    TextView txtHeaderCompany;

    @BindView(R.id.imgFrontCard)
    ImageView imgFrontCardView;

    @BindView(R.id.imgBackCard)
    ImageView imgBackCardView;

    @BindView(R.id.txtEnrichDataLink)
    TextView txtEnrichDataLink;

    @BindView(R.id.txtCapturedAt)
    TextView txtCapturedAt;

    private EnrichAPIService apiService;

    //----------- Amazon S3 Upload--------------------------------------//
    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;

    private final int MY_PERMISSION_REQUEST_WRITE_CONTACTS = 10;

    public CardDetailsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.fragment_card_details, container, false);
            ButterKnife.bind(this, m_rootView);
        }

        // Initializes TransferUtility, always do this before using it.
        transferUtility = AWSUtil.getTransferUtility(getContainer());

        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainApplication.getInstance().getCurrentContactInfo() != null) {
            this.contactInfo = MainApplication.getInstance().getCurrentContactInfo();
        }
        else
        {
            this.contactInfo = new ContactInfo();
            MainApplication.getInstance().setCurrentContactInfo(this.contactInfo);
        }
        contactInfoView.setViewValuesFromContactInfo(this.contactInfo);

        String strCardPath = this.contactInfo.getFrontCardImagePath();
        if(strCardPath.equalsIgnoreCase(""))
            strCardPath = this.contactInfo.getBackCardImagePath();

        util.showCardImageViewWithPicasso(getContext() , strCardPath , imgViewHeaderCard);

        util.showCardImageViewWithPicasso(getContext() , contactInfo.getFrontCardImagePath() , imgFrontCardView);

        util.showCardImageViewWithPicasso(getContext() , contactInfo.getBackCardImagePath() , imgBackCardView);

        txtContactName.setText(contactInfo.getFullName());
        txtHeaderTitle.setText(contactInfo.contact.getTitle());
        txtHeaderCompany.setText(contactInfo.contact.getCompany());

        String link = "https://app.enri.ch/webview.php?email="+MainApplication.getInstance().getCurrentUserInfo().getEmail()+"&url="+contactInfo.contact.getWebsite();
        String htmlText = "<a href=\""+link+"\">"+getResources().getString(R.string.str_contactinfo_enrich_data_hint)+"</a> ";
        //util.setTextViewHTML(txtEnrichDataLink , htmlText);
        txtEnrichDataLink.setText(Html.fromHtml(htmlText));
        txtEnrichDataLink.setMovementMethod(LinkMovementMethod.getInstance());

        if(contactInfo.frontCapturedDate.equalsIgnoreCase(""))
        {
            txtCapturedAt.setText("Captured on "+contactInfo.backCapturedDate);
        }
        else
        {
            txtCapturedAt.setText("Captured on "+contactInfo.frontCapturedDate);
        }

        if(contactInfo.frontCardImage.getImageUrl().startsWith("/"))//local file, then upload
        {
            uploadCardImageFile(true , contactInfo.frontCardImage.getImageUrl());
        }
        else {
            if (contactInfo.backCardImage.getImageUrl().startsWith("/")) //local file, then upload
            {
                uploadCardImageFile(false, contactInfo.backCardImage.getImageUrl());
            }
        }
    }

    @OnClick(R.id.btnMenu)
    public void onClickMenu()
    {
        getContainer().onSlidingMenuClicked();
    }


    @OnClick(R.id.btnUpload)
    public void onClickButtonUpload()
    {
        //---------Export contact to the local phone contacts
        //check add contacts permission
        int recordPermissionCheck = ContextCompat.checkSelfPermission(getContainer(),
                Manifest.permission.WRITE_CONTACTS);
        if(recordPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("SPLASH_SCREEN" , "Record Audio Permission denied");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getContainer(),
                    Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(getContainer(),
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSION_REQUEST_WRITE_CONTACTS);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getContainer(),
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSION_REQUEST_WRITE_CONTACTS);

            }
            return;
        }

        new ExportContactToLocalPhoneTask().execute(contactInfo.contact);
        //----------------------------------------------------
        /*if(contactInfo.frontCardImage.getImageUrl().startsWith("/"))//local file, then upload
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
                addUpdateContact(contactInfo);
            }
        }*/
    }
    class ExportContactToLocalPhoneTask extends AsyncTask<ContactModel , Void , Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getContainer().showProgressDialog("Exporting..." ,false ,null);
        }

        @Override
        protected void onPostExecute(Boolean bResult) {
            super.onPostExecute(bResult);
            getContainer().hideProgressDialog();
            if(bResult)
                getContainer().showErrorMessage("Success!" , "Contact is exported to your local address book successfully.");
            else
                getContainer().showErrorMessage("Success!" , "Contact is exported to your local address book successfully.");
        }

        @Override
        protected Boolean doInBackground(ContactModel... contact) {
            boolean res =  addContact(contact[0]);
            return res;

        }
    }

    private boolean addContact(ContactModel contact)
    {
        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        );

        String displayName = contact.getFname()+" "+contact.getMname()+" "+contact.getLname();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        displayName).build()
        );

        //------------------------------------------------------ Mobile Number
        if(contact.getCellPhone()!=null)
        {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getCellPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build()
            );
        }

        //------------------------------------------------------ Home Numbers
        if(contact.getHomePhone() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getHomePhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if(contact.getWorkPhone() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getWorkPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if(contact.getEmail() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, contact.getEmail())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if(contact.getCompany() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, contact.getCompany())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    //.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try
        {
           getContainer().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //  Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btnEdit)
    public void onClickButtonEdit()
    {
        Intent intent = new Intent(getContainer() , EditCardDetailsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.frontCardLayout)
    public void onClickFrontCardImageView()
    {
        if(!contactInfo.getFrontCardImagePath().equalsIgnoreCase("") && !contactInfo.getFrontCardImagePath().contains("no_card_image.jpg"))
            MainApplication.getInstance().getFragmentChanger().showViewFullImageCardFragment(true , contactInfo.getFrontCardImagePath());
        else
            MainApplication.getInstance().getFragmentChanger().showCameraFragment(true);
    }

    @OnClick(R.id.backCardLayout)
    public void onClickBackCardImageView()
    {
        if(!contactInfo.getBackCardImagePath().equalsIgnoreCase("") && !contactInfo.getBackCardImagePath().contains("no_card_image.jpg"))
            MainApplication.getInstance().getFragmentChanger().showViewFullImageCardFragment(false , contactInfo.getBackCardImagePath());
        else
            MainApplication.getInstance().getFragmentChanger().showCameraFragment(false);
    }

    private void uploadCardImageFile(boolean isFrontCard, String imageFilePath)
    {
        File file = new File(imageFilePath);
        String uploadFileName = "";
        if(isFrontCard)
            uploadFileName = contactInfo.generateImageFileName(contactInfo.frontCardImage);
        else
            uploadFileName = contactInfo.generateImageFileName(contactInfo.backCardImage);

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
                        /*else if(isFrontCard)
                        {
                            addUpdateContact(contactInfo);
                            return;
                        }
                        if(!isFrontCard)
                        {
                            addUpdateContact(contactInfo);
                        }*/
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
                CardImage image = null;
                if(isFrontCard)
                    image = MainApplication.getInstance().getCurrentContactInfo().frontCardImage;
                else
                    image = MainApplication.getInstance().getCurrentContactInfo().backCardImage;
                getContainer().hideProgressDialog();
                addUpdateCardImageUrl(isFrontCard , image , uploadedFileName);
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
                            getContainer().showErrorMessage("Error" , errorMsg , null);
                        }
                        getContainer().hideProgressDialog();
                        getContainer().showErrorMessage("Error", "Failed to update contact" , null);
                    }

                    @Override
                    public void onNext(AddUpdateContactResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            getContainer().hideProgressDialog();

                            return;
                        }

                        ContactModel contact = (ContactModel)response.getData();
                        contactInfo.contact = contact;
                        onBackPressed();
                    }
                });
    }
}
